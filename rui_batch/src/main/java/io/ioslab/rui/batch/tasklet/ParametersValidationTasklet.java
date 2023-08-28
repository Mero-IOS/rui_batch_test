package io.ioslab.rui.batch.tasklet;

import io.ioslab.rui.batch.exception.MailErrorException;
import io.ioslab.rui.batch.utility.InsertParametersInContext;
import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static io.ioslab.rui.batch.utility.Casting.castStringToDate;

@Builder
public class ParametersValidationTasklet implements Tasklet {

    private String url;
    private String date;
    private String inputPath;
    private String outputPath;
    private InsertParametersInContext insertParametersInContext;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        insertParametersInContext = new InsertParametersInContext(chunkContext);

        insertDateInContextIfIsValid();
        insertUrlOrInputPathInContext();
        insertOutputPathInContextIfNotEmpty();

        return RepeatStatus.FINISHED;
    }

    private void insertDateInContextIfIsValid() {
        if (Objects.nonNull(castStringToDate(date)))
            insertParametersInContext.putDateCsvInContext(date);
    }

    private void insertUrlOrInputPathInContext() {
        if (Objects.isNull(inputPath) && Objects.isNull(url))
            throw new MailErrorException("percorsoInput e url non presenti!");
        else if (Objects.isNull(url) || url.isEmpty())
            insertInputPathInContextIfNotEmptyAndIsCorrect();
        else if (Objects.isNull(inputPath) || inputPath.isEmpty())
            insertUrlInContextIfNotEmpty();
        else
            throw new MailErrorException("percorsoInput e url entrambi presenti!");
    }

    private void insertInputPathInContextIfNotEmptyAndIsCorrect() {
        if (!inputPath.isEmpty() && checkIfInputPathIsValid())
            insertParametersInContext.putInputPathInContext(inputPath);
    }

    private boolean checkIfInputPathIsValid() {
        File file = new File(inputPath);
        return file.exists() && file.isFile();
    }

    private void insertUrlInContextIfNotEmpty() {
        if (!url.isEmpty())
            insertParametersInContext.putUrlInContext(url);
    }

    private void insertOutputPathInContextIfNotEmpty() throws IOException {
        if (!outputPath.isEmpty())
            insertParametersInContext.putOutputPathInContext(outputPath, date);
        else
            throw new MailErrorException("percorsoOutput non valido: " + outputPath);
    }
}
