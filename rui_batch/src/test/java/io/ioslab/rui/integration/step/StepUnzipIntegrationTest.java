package io.ioslab.rui.integration.step;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_INPUT_PATH;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.RuiBatchApplication;
import io.ioslab.rui.batch.utility.SendEmailError;
import io.ioslab.rui.utils.TestConstants;
import io.ioslab.rui.utils.TestConstants.DomainObjectEnumerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {RuiBatchApplication.class})
@ActiveProfiles("test")
@SpringBatchTest
@TestPropertySource(properties = "spring.batch.job.enabled=false")
class StepUnzipIntegrationTest {

    @Autowired
    Job ruiBatchJob;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    ExecutionContext executionContext;

    Resource inputZip = new ClassPathResource("unzipTaskletMockZips/MOCKDATA.zip");

    Resource emptyEntryInputZip = new ClassPathResource(
        "unzipTaskletMockZips/ZERO_BYTES_ENTRY.zip");

    Path zipClonedToOutputPath;
    String outputPath;

    @BeforeEach
    void setParams() throws IOException {
        outputPath = Files.createTempDirectory("OUTPUT_TEST").toString();
        zipClonedToOutputPath = Paths.get(outputPath)
                                     .resolve(
                                         "DATI_RUI_" + TestConstants.CSV_FILE_NAME_DATE + ".zip");
        Files.copy(Paths.get(inputZip.getFile().getPath()), zipClonedToOutputPath);
        String pathToCsvStubs = inputZip.getFile().getPath();
        String csvDate = (TestConstants.CSV_FILE_NAME_DATE);
        executionContext = new ExecutionContext();
        executionContext.put(PARAMETER_INPUT_PATH, pathToCsvStubs);
        executionContext.put(PARAMETER_DATE_CSV, csvDate);
        executionContext.put(PARAMETER_OUTPUT_PATH, outputPath);
    }

    @Test
    void execute_fromValidInputToValidOutput_doesUnzip() throws IOException {
        jobLauncherTestUtils.setJob(ruiBatchJob);
        executionContext.put(PARAMETER_INPUT_PATH, inputZip.getFile().getPath());
        jobLauncherTestUtils.launchStep("unzipStep", executionContext);
        Files.delete(zipClonedToOutputPath);
        assertThat(new File(outputPath).listFiles()).allMatch(fileInDirectory -> {
            String fileName = fileInDirectory.getName();
            return Arrays.stream(DomainObjectEnumerator.values())
                         .anyMatch(namePart -> fileName.contains(namePart.toString()));
        });
    }

    @Test
    void execute_fromReadyOutputFolder_doesUnzip() throws IOException {
        jobLauncherTestUtils.setJob(ruiBatchJob);
        jobLauncherTestUtils.launchStep("unzipStep", executionContext);
        Files.delete(zipClonedToOutputPath);
        assertThat(new File(outputPath).listFiles()).allMatch(fileInDirectory -> {
            String fileName = fileInDirectory.getName();
            return Arrays.stream(DomainObjectEnumerator.values())
                         .anyMatch(namePart -> fileName.contains(namePart.toString()));
        });
    }

    @Test
    void execute_onIOException_doesFail() throws IOException {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenThrow(new IOException("TEST_EXCEPTION"));
            JobExecution shouldFail = jobLauncherTestUtils.launchStep("unzipStep",
                                                                      executionContext);
            assertThat(shouldFail.getExitStatus().getExitCode()).isEqualTo("FAILED");
        }
    }

    @Test
    void execute_onEmptyEntry_doesFail() throws IOException {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenReturn(emptyEntryInputZip.getInputStream());
            JobExecution shouldFail = jobLauncherTestUtils.launchStep("unzipStep",
                                                                      executionContext);
            assertThat(shouldFail.getExitStatus().getExitCode()).isEqualTo("FAILED");
        }
    }

}
