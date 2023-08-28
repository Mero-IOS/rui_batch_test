package io.ioslab.rui.integration.step;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_URL;
import static io.ioslab.rui.utils.TestUtils.mockChunkContext;
import static io.ioslab.rui.utils.TestConstants.CSV_DATE_AS_VALID_JOB_PARAMETER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mockStatic;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {RuiBatchApplication.class})
@ActiveProfiles("test")
@SpringBatchTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
public class StepParametersValidationIntegrationTest {

    @Autowired
    Job ruiBatchJob;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    private static final String ANY_PARAM = "SOME_STRING";
    ChunkContext chunkContextMock;
    @TempDir
    private Path tempOut;

    JobParameters jobParameters;

    @BeforeEach
    void setParametersValidationTasklet() throws IOException {
        chunkContextMock = mockChunkContext();
        jobParameters = new JobParametersBuilder().addString(PARAMETER_URL, ANY_PARAM)
                                                  .addString(PARAMETER_DATE_CSV,
                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                             tempOut.toString())
                                                  .toJobParameters();
    }

    @Test
    void execute_noUrlOrInput_DoesFail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            assertThat(
                jobLauncherTestUtils.launchStep("parametersValidationStep", new JobParameters())
                                    .getExitStatus()
                                    .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void execute_anyUrlAndDate_DoesComplete() {
        assertThat(jobLauncherTestUtils.launchStep("parametersValidationStep", jobParameters)
                                       .getExitStatus()
                                       .getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    void execute_anyUrlAndNotADate_DoesFail() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobParameters = new JobParametersBuilder().addString(PARAMETER_URL, ANY_PARAM)
                                                      .addString(PARAMETER_DATE_CSV, ANY_PARAM)
                                                      .addString(PARAMETER_OUTPUT_PATH,
                                                                 tempOut.toString())
                                                      .toJobParameters();
            assertThat(jobLauncherTestUtils.launchStep("parametersValidationStep", jobParameters)
                                           .getExitStatus()
                                           .getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void execute_noUrlEmptyInput_DoesComplete() {
        jobParameters = new JobParametersBuilder().addString(PARAMETER_INPUT_PATH, "")
                                                  .addString(PARAMETER_DATE_CSV,
                                                             CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                  .addString(PARAMETER_OUTPUT_PATH,
                                                             tempOut.toString())
                                                  .toJobParameters();
        assertThat(jobLauncherTestUtils.launchStep("parametersValidationStep", jobParameters)
                                       .getExitStatus()
                                       .getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    void execute_noUrlEmptyInput_DoesNotPutInputInContext() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class)) {
            jobParameters = new JobParametersBuilder().addString(PARAMETER_INPUT_PATH, "")
                                                      .addString(PARAMETER_DATE_CSV,
                                                                 CSV_DATE_AS_VALID_JOB_PARAMETER)
                                                      .addString(PARAMETER_OUTPUT_PATH,
                                                                 tempOut.toString())
                                                      .toJobParameters();
            assertThat(jobLauncherTestUtils.launchStep("parametersValidationStep", jobParameters)
                                           .getExecutionContext()
                                           .get(PARAMETER_INPUT_PATH)).isNull();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {PARAMETER_DATE_CSV, PARAMETER_URL})
    void execute_anyValidDate_DoesPutDateAndUrlInContext(String value) throws Exception {
        assertThat(jobLauncherTestUtils.launchStep("parametersValidationStep", jobParameters)
                                       .getExecutionContext()
                                       .get(value)).isNotNull();
    }

}
