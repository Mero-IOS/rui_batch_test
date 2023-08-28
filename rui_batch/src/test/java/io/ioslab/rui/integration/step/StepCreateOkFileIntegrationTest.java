package io.ioslab.rui.integration.step;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.RuiBatchApplication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
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
class StepCreateOkFileIntegrationTest {
    @Autowired
    Job ruiBatchJob;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @TempDir
    Path tempDir;

    ExecutionContext executionContext;
    @BeforeEach
    void setParams() {
        executionContext = new ExecutionContext();
        executionContext.put(PARAMETER_OUTPUT_PATH, tempDir.toString());
    }

    @Test
    void execute_AnyDirectory_doesComplete() throws Exception {
        JobExecution shouldComplete = jobLauncherTestUtils.launchStep("createOkFileStep",
                                                                      executionContext);
        assertThat(shouldComplete.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }

    @Test
    void execute_AnyDirectory_doesCreateOkFile() throws Exception {
        jobLauncherTestUtils.launchStep("createOkFileStep",
                                                                      executionContext);
        assertThat(tempDir.toFile().listFiles()).anyMatch(file -> file.getName().equals("ok"));
    }

    @Test
    void execute_AnyDirectory_doesRethrowExceptions() throws Exception {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            when(Files.notExists(any())).thenReturn(true);
            when(Files.createFile(any())).thenThrow(new IOException("TEST_EXCEPTION"));
            JobExecution shouldFail = jobLauncherTestUtils.launchStep("createOkFileStep",
                                                                      executionContext);
            assertThat(shouldFail.getExitStatus().getExitCode()).isEqualTo("FAILED");
        }
    }

}
