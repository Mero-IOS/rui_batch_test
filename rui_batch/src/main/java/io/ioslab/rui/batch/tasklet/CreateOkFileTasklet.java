package io.ioslab.rui.batch.tasklet;

import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Builder
public class CreateOkFileTasklet implements Tasklet {

    private String outputPath;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path filePath = Paths.get(outputPath + File.separator + "ok");
        if (Files.notExists(filePath))
            Files.createFile(filePath);

        return RepeatStatus.FINISHED;
    }
}
