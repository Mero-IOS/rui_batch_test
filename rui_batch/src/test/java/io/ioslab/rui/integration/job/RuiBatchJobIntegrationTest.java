package io.ioslab.rui.integration.job;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.utils.TestConstants.DomainObjectEnumerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = {RuiBatchApplication.class})
@ActiveProfiles("test")
@SpringBatchTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class RuiBatchJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JobParameters jobParameters;

    private Path outputEmptyFolder;

    private final Resource ZIP_VALID_WITH_ERRORS = new ClassPathResource(
        "zipsForIntegration/VALID_DATA_WITH_WANTED_AND_FAILING.zip");

    private final Resource EMPTY_ENTRY_INPUT = new ClassPathResource(
        "unzipTaskletMockZips/ZERO_BYTES_ENTRY.zip");

    @BeforeEach
    void setContext() throws IOException {
        outputEmptyFolder = Files.createTempDirectory("TEST_OUTPUT_FOLDER");
        jobParameters = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                             outputEmptyFolder.toString())
                                                  .addString(PARAMETER_INPUT_PATH,
                                                             ZIP_VALID_WITH_ERRORS.getFile()
                                                                                  .getPath())
                                                  .toJobParameters();
    }

    @Test
    void launchJob_emptyJobParams_doesFail() throws Exception {
        assertThat(jobLauncherTestUtils.launchJob(new JobParameters())
                                       .getExitStatus()
                                       .getExitCode()).isEqualTo("FAILED");
    }

    @Test
    void launchJob_validJobParams_doesComplete() throws Exception {
        assertThat(
            jobLauncherTestUtils.launchJob(jobParameters).getExitStatus().getExitCode()).isEqualTo(
            "COMPLETED");
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_validJobParamsDifferentJobParameters_doesRemoveDuplicateInDatabase(
        DomainObjectEnumerator value) throws Exception {
        jobLauncherTestUtils.launchJob(jobParameters);
        jobParameters = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                             Files.createTempDirectory(
                                                                      "SECOND_TEST_OUTPUT_FOLDER")
                                                                  .toString())
                                                  .addString(PARAMETER_INPUT_PATH,
                                                             ZIP_VALID_WITH_ERRORS.getFile()
                                                                                  .getPath())
                                                  .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(
            jdbcTemplate.queryForObject(value.getSqlCountStatement(), Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    @Test
    void launchJob_validJobParams_doesCreateOkFileInCorrectFolder() throws Exception {
        String okFilePath = outputEmptyFolder.toString() + File.separator + "rui_ivass" +
                            File.separator + CSV_DATE_AS_SQL_DATE;
        File okFileFolder = new File(okFilePath);
        jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(okFileFolder.listFiles()).anyMatch(file -> file.getName().contains("ok"));
    }

    @Test
    void launchJob_validJobParamsWithInputPath_doesNotDownload() throws Exception {
        jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(outputEmptyFolder.toFile().listFiles()).noneMatch(
            file -> file.getName().contains(".zip"));
    }

    @Test
    void launchJob_emptyOutputPath_doesFail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                         CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                              .addString(PARAMETER_OUTPUT_PATH, "")
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         ZIP_VALID_WITH_ERRORS.getFile()
                                                                                              .getPath())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingDate)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void launchJob_withNoInputOrLink_doesFail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                         CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingDate)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void launchJob_withNoInputOrUrl_doesSendMail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                         CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingDate);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("percorsoInput e url non presenti!"));
        }
    }

    @Test
    void launchJob_withBothInputAndUrl_doesFail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV, "")
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         "ANY")
                                                              .addString(PARAMETER_URL, "ANY")
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingDate)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void launchJob_withBothInputAndUrl_doesSendMail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV, "")
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         "ANY")
                                                              .addString(PARAMETER_URL, "ANY")
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingDate);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("percorsoInput e url entrambi presenti!"));
        }
    }

    @Test
    void launchJob_withEmptyDate_doesFail() throws Exception {
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV, "")
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         ZIP_VALID_WITH_ERRORS.getFile()
                                                                                              .getPath())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingDate)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void launchJob_withWrongDate_doesSendMail() throws Exception {
        String wrongDate = "2023-01-02";
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                         wrongDate)
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         ZIP_VALID_WITH_ERRORS.getFile()
                                                                                              .getPath())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingDate);
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(
                "non tutti i file presentano la data: " + wrongDate));
        }
    }

    @Test
    void launchJob_withNotADate_doesSendMail() throws Exception {
        String notADate = "NOT_A_DATE";
        JobParameters failingDate = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                         notADate)
                                                              .addString(PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                              .addString(PARAMETER_INPUT_PATH,
                                                                         ZIP_VALID_WITH_ERRORS.getFile()
                                                                                              .getPath())
                                                              .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingDate);
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError("data non valida",
                                                                               "Unparseable date: \"" +
                                                                               notADate + "\""));
        }
    }

    @Test
    void launchJob_withEmptyEntryInput_doesFail() throws Exception {
        JobParameters emptyEntryInput = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                                             outputEmptyFolder.toString())
                                                                  .addString(PARAMETER_INPUT_PATH,
                                                                             EMPTY_ENTRY_INPUT.getFile()
                                                                                              .getPath())
                                                                  .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(emptyEntryInput)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void launchJob_withEmptyEntryInput_doesSendMail() throws Exception {
        JobParameters emptyEntryInput = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                                             outputEmptyFolder.toString())
                                                                  .addString(PARAMETER_INPUT_PATH,
                                                                             EMPTY_ENTRY_INPUT.getFile()
                                                                                              .getPath())
                                                                  .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(emptyEntryInput);
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(
                "file: entrywihtzerobytes.txt presenta dimensione 0"));
        }
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_validJobParamsWithMockZip_doesInsertAllValidCaricheInDb(
        DomainObjectEnumerator value) throws Exception {
        jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(
            jdbcTemplate.queryForObject(value.getSqlCountStatement(), Integer.class)).isEqualTo(
            VALID_RECORD_PER_CSV);
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_withCsvOverReadSkipLimit_doesFail(DomainObjectEnumerator value) throws Exception {
        Resource zipWithFailingValue = new ClassPathResource(
            "zipsForIntegration/" + value.name() + "_FAILING.zip");
        JobParameters failingValueCsvToDbStep = new JobParametersBuilder().addString(
                                                                              PARAMETER_DATE_CSV, CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                          .addString(
                                                                              PARAMETER_OUTPUT_PATH,
                                                                              outputEmptyFolder.toString())
                                                                          .addString(
                                                                              PARAMETER_INPUT_PATH,
                                                                              zipWithFailingValue.getFile()
                                                                                                 .getPath())
                                                                          .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingValueCsvToDbStep)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_withCsvOverReadSkipLimit_doesSendMail(DomainObjectEnumerator value) throws Exception {
        Resource zipWithFailingValue = new ClassPathResource(
            "zipsForIntegration/" + value.name() + "_FAILING.zip");
        JobParameters failingValueCsvToDbStep = new JobParametersBuilder().addString(
                                                                              PARAMETER_DATE_CSV, CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                          .addString(
                                                                              PARAMETER_OUTPUT_PATH,
                                                                              outputEmptyFolder.toString())
                                                                          .addString(
                                                                              PARAMETER_INPUT_PATH,
                                                                              zipWithFailingValue.getFile()
                                                                                                 .getPath())
                                                                          .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingValueCsvToDbStep);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError("numero massimo di righe saltate raggiunto"));
        }
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_withMissingAnyCsv_doesFail(DomainObjectEnumerator value) throws Exception {
        Resource zipWithFailingValue = new ClassPathResource(
            "zipsForIntegration/" + value.name() + "_MISSING.zip");
        JobParameters failingValueCsvToDbStep = new JobParametersBuilder().addString(
                                                                              PARAMETER_DATE_CSV, CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                          .addString(
                                                                              PARAMETER_OUTPUT_PATH,
                                                                              outputEmptyFolder.toString())
                                                                          .addString(
                                                                              PARAMETER_INPUT_PATH,
                                                                              zipWithFailingValue.getFile()
                                                                                                 .getPath())
                                                                          .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(jobLauncherTestUtils.launchJob(failingValueCsvToDbStep)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_withMissingAnyCsv_doesNotSendMail(DomainObjectEnumerator value) throws Exception {
        Resource zipWithFailingValue = new ClassPathResource(
            "zipsForIntegration/" + value.name() + "_MISSING.zip");
        JobParameters failingValueCsvToDbStep = new JobParametersBuilder().addString(
                                                                              PARAMETER_DATE_CSV, CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                          .addString(
                                                                              PARAMETER_OUTPUT_PATH,
                                                                              outputEmptyFolder.toString())
                                                                          .addString(
                                                                              PARAMETER_INPUT_PATH,
                                                                              zipWithFailingValue.getFile()
                                                                                                 .getPath())
                                                                          .toJobParameters();
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingValueCsvToDbStep);
            sendEmailErrorMockedStatic.verifyNoInteractions();
        }
    }

    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void launchJob_withDroppedDomainTables_doesFail() throws Exception {
        assertThat(
            jobLauncherTestUtils.launchJob(jobParameters).getExitStatus().getExitCode()).isEqualTo(
            "FAILED");
    }


}
