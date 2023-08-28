package io.ioslab.rui.batch.tasklet;

import io.ioslab.rui.batch.exception.MailErrorException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Builder
public class UnzipTasklet implements Tasklet {

    private String inputPath;
    private String date;
    private String outputPath;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if (Objects.isNull(inputPath)) {
            unzip(Paths.get(getZipFileFromPath(outputPath)), Paths.get(outputPath));
        } else {
            unzip(Paths.get(inputPath), Paths.get(outputPath));
        }
        return RepeatStatus.FINISHED;
    }

    private String getZipFileFromPath(String path) {
        return new StringBuilder(path).append(File.separator)
                                      .append("DATI_RUI_")
                                      .append(date)
                                      .append(".zip")
                                      .toString();
    }

    private void unzip(Path inputPath, Path outputPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(inputPath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (Objects.nonNull(zipEntry)) {
                Path fileLocation = outputPath.resolve(zipEntry.getName());
                writeFileFromZipToLocation(zipInputStream, zipEntry, fileLocation);
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            throw new MailErrorException("errore nell'apertura dello stream del file zip",
                                         e.getMessage());
        }
    }

    private void writeFileFromZipToLocation(ZipInputStream zipInputStream, ZipEntry zipEntry,
                                            Path fileLocation) throws MailErrorException {
        byte[] buffer = createBuffer(zipEntry);
        try (OutputStream outputStream = Files.newOutputStream(fileLocation)) {
            int location;
            while ((location = zipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, location);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new MailErrorException("errore durante l'estrazione dallo zip", e.getMessage());
        }
    }

    private byte[] createBuffer(ZipEntry zipEntry) {
        if (zipEntry.getSize() == 0) {
            throw new MailErrorException("file: " + zipEntry.getName() + " presenta dimensione 0");
        } else {
            return new byte[Math.toIntExact(zipEntry.getSize())];
        }
    }

}
