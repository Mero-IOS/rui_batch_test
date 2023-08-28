package io.ioslab.rui.integration.tasklet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.configuration.tasklet.UnzipTaskletConfiguration;
import io.ioslab.rui.batch.exception.MailErrorException;
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
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class UnzipTaskletIntegrationTest {

    private Tasklet unzipTasklet;
    String outputPath;
    String inputPath;
    Path zipClonedToOutputPath;
    Resource inputZip = new ClassPathResource("unzipTaskletMockZips/MOCKDATA.zip");

    Resource emptyEntryInputZip = new ClassPathResource(
        "unzipTaskletMockZips/ZERO_BYTES_ENTRY.zip");

    @BeforeEach
    void setup() throws IOException {
        outputPath = Files.createTempDirectory("OUTPUT_TEST").toString();
        zipClonedToOutputPath = Paths.get(outputPath)
                                     .resolve(
                                         "DATI_RUI_" + TestConstants.CSV_FILE_NAME_DATE + ".zip");
        Files.copy(Paths.get(inputZip.getFile().getPath()), zipClonedToOutputPath);
        unzipTasklet = new UnzipTaskletConfiguration().unzipTasklet(inputPath,
                                                                    TestConstants.CSV_FILE_NAME_DATE,
                                                                    outputPath);
    }

    @Test
    void execute_fromValidInputToValidOutput_doesUnzip() throws Exception {
        unzipTasklet.execute(mock(StepContribution.class), mock(ChunkContext.class));
        Files.delete(zipClonedToOutputPath);
        assertThat(new File(outputPath).listFiles()).allMatch(fileInDirectory -> {
            String fileName = fileInDirectory.getName();
            return Arrays.stream(DomainObjectEnumerator.values())
                         .anyMatch(namePart -> fileName.contains(namePart.toString()));
        });
    }

    @Test
    void execute_fromReadyOutputFolder_doesUnzip() throws Exception {
        unzipTasklet = new UnzipTaskletConfiguration().unzipTasklet(null,
                                                                    TestConstants.CSV_FILE_NAME_DATE,
                                                                    outputPath);
        unzipTasklet.execute(mock(StepContribution.class), mock(ChunkContext.class));
        Files.delete(zipClonedToOutputPath);
        assertThat(new File(outputPath).listFiles()).allMatch(fileInDirectory -> {
            String fileName = fileInDirectory.getName();
            return Arrays.stream(DomainObjectEnumerator.values())
                         .anyMatch(namePart -> fileName.contains(namePart.toString()));
        });
    }

    @Test
    void unzip_onIOException_doesThrowMailErrorException() throws IOException {
        unzipTasklet = new UnzipTaskletConfiguration().unzipTasklet(inputPath, "ANY", "ANY");
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenThrow(new IOException("TEST_EXCEPTION"));
            assertThrows(MailErrorException.class,
                         () -> unzipTasklet.execute(mock(StepContribution.class),
                                                    mock(ChunkContext.class)));
        }
    }

    @Test
    void writeFileFromZipToLocation_onIOException_doesThrowMailErrorException() throws IOException {
        unzipTasklet = new UnzipTaskletConfiguration().unzipTasklet("ANY", "ANY", "ANY");
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenReturn(inputZip.getInputStream());
            when(Files.newOutputStream(any())).thenThrow(new IOException());
            assertThrows(MailErrorException.class,
                         () -> unzipTasklet.execute(mock(StepContribution.class),
                                                    mock(ChunkContext.class)));
        }
    }

    @Test
    void writeFileFromZipToLocation_onEmptyEntry_doesThrowMailErrorException() throws IOException {
        unzipTasklet = new UnzipTaskletConfiguration().unzipTasklet("ANY", "ANY", "ANY");
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenReturn(emptyEntryInputZip.getInputStream());
            when(Files.newOutputStream(any())).thenThrow(new IOException());
            assertThrows(MailErrorException.class,
                         () -> unzipTasklet.execute(mock(StepContribution.class),
                                                    mock(ChunkContext.class)));
        }
    }

}
