package io.ioslab.rui.unit.tasklet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.tasklet.CreateOkFileTasklet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

class CreateOkFileTaskletUnitTest {

    @TempDir
    Path tempDir;

    @Test
    void execute_AnyDirectory_doesCreateOkFile() throws Exception {
        CreateOkFileTasklet tasklet = CreateOkFileTasklet.builder()
                                                         .outputPath(tempDir.toString())
                                                         .build();
        tasklet.execute(null, null);
        assertThat(tempDir.toFile().listFiles()).anyMatch(
            okFile -> okFile.getName().contains("ok"));
    }

    @Test
    void execute_AnyDirectory_doesRethrowExceptions() throws Exception {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            when(Files.notExists(any())).thenReturn(true);
            when(Files.createFile(any())).thenThrow(new IOException());
            CreateOkFileTasklet tasklet = CreateOkFileTasklet.builder()
                                                             .outputPath(tempDir.toString())
                                                             .build();
            assertThrows(IOException.class, () -> tasklet.execute(null, null));
        }
    }

}
