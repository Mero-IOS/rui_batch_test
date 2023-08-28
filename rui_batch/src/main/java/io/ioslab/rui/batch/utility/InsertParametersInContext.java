package io.ioslab.rui.batch.utility;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.ioslab.rui.batch.utility.Costants.*;

@AllArgsConstructor
public class InsertParametersInContext {

    private ChunkContext chunkContext;

    public void putUrlInContext(String url) {
        chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put(PARAMETER_URL, url);
    }

    public void putInputPathInContext(String inputPath) {
        chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put(PARAMETER_INPUT_PATH, inputPath);
    }

    public void putDateCsvInContext(String date) {
        chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put(PARAMETER_DATE_CSV, date);
    }

    public void putOutputPathInContext(String outputPath, String date) throws IOException {
        outputPath = getOutputFolderNameWithDate(outputPath, date);
        createOutputFolder(outputPath);

        chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put(PARAMETER_OUTPUT_PATH, outputPath);
    }

    private String getOutputFolderNameWithDate(String outputPath, String date) {
        return new StringBuilder(outputPath)
                .append(File.separator)
                .append("rui_ivass")
                .append(File.separator)
                .append(date)
                .toString();
    }

    private void createOutputFolder(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path))
            Files.createDirectories(path);
    }
}
