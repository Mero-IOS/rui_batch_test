package io.ioslab.rui.integration.job;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_SQL_DATE;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static io.ioslab.rui.utils.TestConstants.VALID_RECORD_PER_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.exceptions.base.MockitoAssertionError;
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
        assertThat(getJobExitCode(new JobParameters())).isEqualTo("FAILED");
    }

    @Test
    void launchJob_validJobParams_doesComplete() throws Exception {
        assertThat(getJobExitCode(jobParameters)).isEqualTo("COMPLETED");
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
    void launchJob_validJobParamsWithInputPath_doesNotDownloadZip() throws Exception {
        jobLauncherTestUtils.launchJob(jobParameters);
        assertThat(outputEmptyFolder.toFile().listFiles()).noneMatch(
            file -> file.getName().contains(".zip"));
    }

    @Test
    void launchJob_emptyOutputPath_doesFail() throws Exception {
        JobParameters emptyOutputParams = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                               CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                    .addString(
                                                                        PARAMETER_OUTPUT_PATH, "")
                                                                    .addString(PARAMETER_INPUT_PATH,
                                                                               ZIP_VALID_WITH_ERRORS.getFile()
                                                                                                    .getPath())
                                                                    .toJobParameters();
        assertThat(getJobExitCode(emptyOutputParams)).isEqualTo("FAILED");
    }

    @Test
    void launchJob_withNoInputOrLink_doesFail() throws Exception {
        JobParameters noInputOrUrlParams = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                                CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                     .addString(
                                                                         PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                                     .toJobParameters();
        assertThat(getJobExitCode(noInputOrUrlParams)).isEqualTo("FAILED");
    }

    @Test
    void launchJob_withNoInputOrUrl_doesSendMail() throws Exception {
        JobParameters noInputOrUrlParams = new JobParametersBuilder().addString(PARAMETER_DATE_CSV,
                                                                                CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                                     .addString(
                                                                         PARAMETER_OUTPUT_PATH,
                                                                         outputEmptyFolder.toString())
                                                                     .toJobParameters();
        assertThat(
            isFailMailSent(noInputOrUrlParams, "percorsoInput e url non presenti!")).isTrue();
    }

    @Test
    void launchJob_withBothInputAndUrl_doesFail() throws Exception {
        JobParameters bothInputAndUrlParams = new JobParametersBuilder().addString(
                                                                            PARAMETER_DATE_CSV, "")
                                                                        .addString(
                                                                            PARAMETER_OUTPUT_PATH,
                                                                            outputEmptyFolder.toString())
                                                                        .addString(
                                                                            PARAMETER_INPUT_PATH,
                                                                            "ANY")
                                                                        .addString(PARAMETER_URL,
                                                                                   "ANY")
                                                                        .toJobParameters();
        assertThat(getJobExitCode(bothInputAndUrlParams)).isEqualTo("FAILED");
    }

    @Test
    void launchJob_withBothInputAndUrl_doesSendMail() throws Exception {
        JobParameters bothInputAndUrlParams = new JobParametersBuilder().addString(
                                                                            PARAMETER_DATE_CSV, "")
                                                                        .addString(
                                                                            PARAMETER_OUTPUT_PATH,
                                                                            outputEmptyFolder.toString())
                                                                        .addString(
                                                                            PARAMETER_INPUT_PATH,
                                                                            "ANY")
                                                                        .addString(PARAMETER_URL,
                                                                                   "ANY")
                                                                        .toJobParameters();
        assertThat(isFailMailSent(bothInputAndUrlParams,
                                  "percorsoInput e url entrambi presenti!")).isTrue();
    }

    @Test
    void launchJob_withEmptyDate_doesFail() throws Exception {
        JobParameters emptyDateJobParameters = new JobParametersBuilder().addString(
                                                                             PARAMETER_DATE_CSV, "")
                                                                         .addString(
                                                                             PARAMETER_OUTPUT_PATH,
                                                                             outputEmptyFolder.toString())
                                                                         .addString(
                                                                             PARAMETER_INPUT_PATH,
                                                                             ZIP_VALID_WITH_ERRORS.getFile()
                                                                                                  .getPath())
                                                                         .toJobParameters();
        assertThat(getJobExitCode(emptyDateJobParameters)).isEqualTo("FAILED");
    }

    @Test
    void launchJob_withWrongDate_doesFail() throws Exception {
        String wrongDate = "2023-01-02";
        JobParameters wrongDateJobParameters = new JobParametersBuilder().addString(
                                                                             PARAMETER_DATE_CSV, wrongDate)
                                                                         .addString(
                                                                             PARAMETER_OUTPUT_PATH,
                                                                             outputEmptyFolder.toString())
                                                                         .addString(
                                                                             PARAMETER_INPUT_PATH,
                                                                             ZIP_VALID_WITH_ERRORS.getFile()
                                                                                                  .getPath())
                                                                         .toJobParameters();
        assertThat(getJobExitCode(wrongDateJobParameters)).isEqualTo("FAILED");
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
        assertThat(isFailMailSent(failingDate,
                                  "non tutti i file presentano la data: " + wrongDate)).isTrue();
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
        assertThat(isFailMailSent(failingDate, "data non valida",
                                  "Unparseable date: \"" + notADate + "\"")).isTrue();
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
        assertThat(getJobExitCode(emptyEntryInput)).isEqualTo("FAILED");
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
        assertThat(isFailMailSent(emptyEntryInput,
                                  "file: entrywihtzerobytes.txt presenta dimensione 0")).isTrue();
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
    void launchJob_withCsvOverReadSkipLimit_doesFail(DomainObjectEnumerator value)
        throws Exception {
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
        assertThat(getJobExitCode(failingValueCsvToDbStep)).isEqualTo("FAILED");
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_withCsvOverReadSkipLimit_doesSendMail(DomainObjectEnumerator value)
        throws Exception {
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
        assertThat(isFailMailSent(failingValueCsvToDbStep,
                                  "numero massimo di righe saltate raggiunto")).isTrue();
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_missingAnyCsv_doesFail(DomainObjectEnumerator value) throws Exception {
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
        assertThat(getJobExitCode(failingValueCsvToDbStep)).isEqualTo("FAILED");
    }

    @ParameterizedTest
    @EnumSource(DomainObjectEnumerator.class)
    void launchJob_missingAnyCsv_doesNotSendMail(DomainObjectEnumerator value) throws Exception {
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
        assertThat(isFailMailSent(failingValueCsvToDbStep)).isFalse();
    }

    @Test
    @Sql(scripts = "/dropSchema.sql")
    @Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void launchJob_withDroppedDomainTables_doesFail() throws Exception {
        assertThat(getJobExitCode(jobParameters)).isEqualTo("FAILED");
    }

    //Utilities
    private String getJobExitCode(JobParameters jobParameters) throws Exception {
        try (MockedStatic<SendEmailError> ignored = mockStatic(
            SendEmailError.class)) {
            return jobLauncherTestUtils.launchJob(jobParameters).getExitStatus().getExitCode();
        }
    }

    private boolean isFailMailSent(JobParameters failingParams, String message) throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingParams);
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(message));
            return true;
        } catch (MockitoAssertionError e) {
            return false;
        }
    }

    private boolean isFailMailSent(JobParameters failingParams, String message, String stackTrace)
        throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingParams);
            sendEmailErrorMockedStatic.verify(
                () -> SendEmailError.manageError(message, stackTrace));
            return true;
        } catch (MockitoAssertionError e) {
            return false;
        }
    }

    private boolean isFailMailSent(JobParameters failingParams) throws Exception {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobLauncherTestUtils.launchJob(failingParams);
            sendEmailErrorMockedStatic.verify(() -> SendEmailError.manageError(anyString()));
            return true;
        } catch (MockitoAssertionError e) {
            return false;
        }
    }

}

