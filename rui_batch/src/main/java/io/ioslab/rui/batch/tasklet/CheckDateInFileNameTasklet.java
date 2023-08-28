package io.ioslab.rui.batch.tasklet;

import io.ioslab.rui.batch.exception.MailErrorException;
import lombok.Builder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class CheckDateInFileNameTasklet implements Tasklet {

    private String date;
    private String outputPath;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<Path> list;
        try (Stream<Path> streamList = Files.list(Paths.get(outputPath))) {
            list = streamList
                    .filter(extension -> extension.toString().endsWith(".csv"))
                    .collect(Collectors.toList());
        }
        if (list.isEmpty())
            throw new MailErrorException("lista di file con la data: " + date + " risulta vuota");

        if (!isListElementsContainDate(list, date.replace("-", "_")))
            throw new MailErrorException("non tutti i file presentano la data: " + date);

        return RepeatStatus.FINISHED;
    }

    private boolean isListElementsContainDate(List<Path> list, String date) {
        return list.stream().allMatch(
                element -> element.toString()
                        .endsWith(date + ".csv"));
    }
}
