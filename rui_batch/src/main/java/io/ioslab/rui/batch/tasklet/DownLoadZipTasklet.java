package io.ioslab.rui.batch.tasklet;

import io.ioslab.rui.batch.exception.MailErrorException;
import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Builder
public class DownLoadZipTasklet implements Tasklet {

    private String url;
    private String date;
    private String outputPath;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        URL castedUrl = new URL(url);
        File nameFileToDownload = new File(outputPath + File.separator + "DATI_RUI_" + date + ".zip");

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(castedUrl.openStream())) {
            downloadFile(readableByteChannel, nameFileToDownload);
        } catch (Exception e) {
            throw new MailErrorException("errore durante il collegamento con l'url: " + url, e.getMessage());
        }

        return RepeatStatus.FINISHED;
    }

    private void downloadFile(ReadableByteChannel readableByteChannel, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Integer.MAX_VALUE);
        } catch (IOException e) {
            throw new MailErrorException("errore durante il download del file", e.getMessage());
        }
    }
}
