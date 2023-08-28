package io.ioslab.rui.unit.tasklet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.tasklet.UnzipTasklet;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class UnzipTaskletUnitTest {

    private UnzipTasklet unzipTasklet;
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
        unzipTasklet = UnzipTasklet.builder()
                                   .date(TestConstants.CSV_FILE_NAME_DATE)
                                   .inputPath(inputPath)
                                   .outputPath(outputPath)
                                   .build();
    }

    @Test
    void execute_fromInputZipToOutput_doesUnzip() throws IOException {
        Files.delete(zipClonedToOutputPath);
        unzipTasklet = UnzipTasklet.builder()
                                   .date(TestConstants.CSV_FILE_NAME_DATE)
                                   .inputPath(inputZip.getFile().getPath())
                                   .outputPath(outputPath)
                                   .build();
        unzipTasklet.execute(mock(StepContribution.class), mock(ChunkContext.class));
        assertThat(new File(outputPath).listFiles()).allMatch(fileInDirectory -> {
            String fileName = fileInDirectory.getName();
            return Arrays.stream(DomainObjectEnumerator.values())
                         .anyMatch(namePart -> fileName.contains(namePart.toString()));
        });
    }

    @Test
    void execute_fromOutputFolderItself_doesUnzip() throws IOException {
        unzipTasklet = UnzipTasklet.builder()
                                   .date(TestConstants.CSV_FILE_NAME_DATE)
                                   .outputPath(outputPath)
                                   .build();
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
        unzipTasklet = UnzipTasklet.builder().date("ANY").outputPath("ANY").build();
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
        unzipTasklet = UnzipTasklet.builder()
                                   .date("ANY")
                                   .outputPath("ANY")
                                   .inputPath("ANY")
                                   .build();
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
        unzipTasklet = UnzipTasklet.builder()
                                   .date("ANY")
                                   .outputPath("ANY")
                                   .inputPath("ANY")
                                   .build();
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class);
             MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
                 SendEmailError.class)) {
            when(Files.newInputStream(any())).thenReturn(emptyEntryInputZip.getInputStream());
            assertThrows(MailErrorException.class,
                         () -> unzipTasklet.execute(mock(StepContribution.class),
                                                    mock(ChunkContext.class)));
        }
    }

}
