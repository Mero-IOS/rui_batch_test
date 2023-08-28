package io.ioslab.rui.integration.step.csvtodbsteps;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.utils.TestUtils.makeTempDuplicateOfResource;
import static io.ioslab.rui.utils.TestConstants.COUNT_MANDATI_SQL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static io.ioslab.rui.utils.TestConstants.TRUNCATE_SQL;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.Casting;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = {RuiBatchApplication.class})
@ActiveProfiles("test")
@SpringBatchTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class StepRuiMandatiCsvToDatabaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private ExecutionContext executionContext;

    private Resource mockCsvWithValidRecords = new ClassPathResource(
        "mockCsv/mockCsvWithValidRecords");
    private Resource mockCsvWithSingleValidRecord = new ClassPathResource(
        "mockCsv/mockCsvWithSingleValidRecord");
    private Resource mockCsvWithValidAndInvalidRecords = new ClassPathResource(
        "mockCsv/mockCsvWithValidAndInvalidRecords");
    private Resource mockCsvWithOnlyFailingRecords = new ClassPathResource(
        "mockCsv/mockCsvWithOnlyFailingRecords");

    @BeforeEach
    void setParams() throws IOException {
        executionContext = new ExecutionContext();
        executionContext.put(PARAMETER_DATE_CSV, CSV_DATE_AS_VALID_JOB_PARAMETER);
        executionContext.put(PARAMETER_OUTPUT_PATH, mockCsvWithValidRecords.getFile().getPath());
    }

    @AfterEach
    void truncateTables() {
        try {
            jdbcTemplate.update(TRUNCATE_SQL);
        } catch (BadSqlGrammarException e) {
            if (e.getSql().contains("TRUNCATE")) {
                System.out.println("Tabella inesistente, nessun errore.");
            } else {
                throw e;
            }
        }
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withMockCsv_doesInsertAllValidRecordsInMockCsv() {
        jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
        assertThat(jdbcTemplate.queryForObject(COUNT_MANDATI_SQL, Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withInvalidDate_doesFailOnOpeningTheReader() {
        executionContext.put(PARAMETER_DATE_CSV, "NOT_VALID_DATE");
        JobExecution execution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                 executionContext);
        assertThat(execution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withReadSkipsExceedingSkipLimit_doesFail() throws IOException {
        executionContext.put(PARAMETER_OUTPUT_PATH,
                             mockCsvWithOnlyFailingRecords.getFile().getPath());
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                    executionContext);
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withReadSkipsExceedingSkipLimit_doesSendMail()
        throws IOException {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            executionContext.put(PARAMETER_OUTPUT_PATH,
                                 mockCsvWithOnlyFailingRecords.getFile().getPath());
            JobExecution jobExecution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                        executionContext);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("numero massimo di righe saltate raggiunto"));
        }
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withProcessingSkipsExceedingSkipLimit_doesFail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            castingMockedStatic.when(() -> Casting.castStringToDate(anyString()))
                               .thenThrow(new ParseException("TEST_EXCEPTION"));
            JobExecution execution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                     executionContext);
            assertThat(execution.getExitStatus().getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withInvalidDate_doesSendMail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            castingMockedStatic.when(() -> Casting.castStringToDate(anyString()))
                               .thenThrow(new ParseException("TEST_EXCEPTION"));
            JobExecution execution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                     executionContext);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("numero massimo di righe saltate raggiunto"));
        }
    }

    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ruiMandatiCsvToDataBaseStep_withWriteSkipsExceedingSkipLimit_doesFail() {
        JobExecution execution = jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep",
                                                                 executionContext);
        assertThat(execution.getExitStatus().getExitCode()).isEqualTo("FAILED");
    }

    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ruiMandatiCsvToDataBaseStep_withWriteSkipsExceedingSkipLimit_doesSendMail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("numero massimo di righe saltate raggiunto"));
        }
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withMockCsvContainingErrors_doesCreateScartiFile()
        throws IOException {
        Path outputForTest = makeTempDuplicateOfResource(mockCsvWithValidAndInvalidRecords);
        executionContext.put(PARAMETER_OUTPUT_PATH, outputForTest.toString());
        jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
        String scartiFileContents = new String(
            Files.readAllBytes(outputForTest.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("reader - \n");
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withMockCsvOverSkipLimit_doesNotCreateScartiFile()
        throws IOException {
        Path outputForTest = makeTempDuplicateOfResource(mockCsvWithOnlyFailingRecords);
        executionContext.put(PARAMETER_OUTPUT_PATH, outputForTest.toString());
        jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
        assertThat(outputForTest.toFile().listFiles()).noneMatch(
            file -> file.getName().contains("scarti.txt"));
    }

    @Test
    void ruiMandatiCsvToDataBaseStep_withProcessingSkipsExceedingSkipLimit_doesCreateScartiFile()
        throws IOException {
        try (MockedStatic<Casting> castingMockedStatic = mockStatic(Casting.class)) {
            castingMockedStatic.when(() -> Casting.castStringToDate(anyString()))
                               .thenThrow(new RuntimeException("TEST_EXCEPTION"))
                               .thenReturn(new Date());
            Path outputForTest = makeTempDuplicateOfResource(mockCsvWithSingleValidRecord);
            executionContext.put(PARAMETER_OUTPUT_PATH, outputForTest.toString());
            jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
            String scartiFileContents = new String(
                Files.readAllBytes(outputForTest.resolve("scarti.txt")));
            assertThat(scartiFileContents).contains("process - io.ioslab.rui.common.model.rui.Rui");
        }
    }

    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ruiMandatiCsvToDataBaseStep_withWriteSkipsExceedingSkipLimit_doesCreateScartiFile()
        throws IOException {
        Path outputForTest = makeTempDuplicateOfResource(mockCsvWithSingleValidRecord);
        executionContext.put(PARAMETER_OUTPUT_PATH, outputForTest.toString());
        jobLauncherTestUtils.launchStep("ruiMandatiCsvToDatabaseStep", executionContext);
        String scartiFileContents = new String(
            Files.readAllBytes(outputForTest.resolve("scarti.txt")));
        assertThat(scartiFileContents).contains("writer - io.ioslab.rui.common.model.rui.Ru");
    }

}