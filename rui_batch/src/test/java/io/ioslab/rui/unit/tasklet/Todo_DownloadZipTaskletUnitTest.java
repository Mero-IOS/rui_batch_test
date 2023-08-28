package io.ioslab.rui.unit.tasklet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.ioslab.rui.batch.tasklet.DownLoadZipTasklet;
import io.ioslab.rui.batch.utility.SendEmailError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Todo_DownloadZipTaskletUnitTest {

    private DownLoadZipTasklet downLoadZipTasklet;
    @TempDir
    private Path tempOut;
    private final Resource mockZip = new ClassPathResource(
        "unzipTaskletMockZips/MOCKDATA.zip");


    @Mock
    URL mockUrl;

    @BeforeEach
    void setDownLoadZipTasklet() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(mockUrl.openStream()).thenReturn(mock(InputStream.class));
        downLoadZipTasklet = DownLoadZipTasklet.builder()
                                               .url("https://www.any_url.com")
                                               .date("ANY_DATE")
                                               .outputPath(tempOut.toString())
                                               .build();
    }

    @SneakyThrows
    @Test
    @Disabled
    void execute_withAnyUrl_doesDownload() {
        try (MockedStatic<SendEmailError> sendEmailErrorMockedStatic = mockStatic(
            SendEmailError.class);
             MockedStatic<Channels> channelsMockedStatic = mockStatic(Channels.class);
       ) {
            when(Channels.newChannel(mockUrl.openStream())).thenReturn(mockZip.readableChannel());
            downLoadZipTasklet.execute(mock(StepContribution.class), mock(ChunkContext.class));
        }
    }

    //TODO why not return inputZip?

}
