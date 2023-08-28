package io.ioslab.rui.batch.jobrepository.step;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.processor.RuiCollaboratoriProcessor;
import io.ioslab.rui.common.model.rui.RuiCollaboratori;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.CHUNK_SIZE;

@Configuration
public class StepRuiCollaboratoriCsvToDatabase {

    @Bean
    public Step ruiCollaboratoriCsvToDatabaseStep(StepBuilderFactory stepBuilderFactory,
                                                  FlatFileItemReader<RuiCollaboratori> readerRuiCollaboratori,
                                                  RuiCollaboratoriProcessor processorRuiCollaboratori,
                                                  JdbcBatchItemWriter<RuiCollaboratori> writerRuiCollaboratori,
                                                  CustomSkipPolicyListener listenerCustomSkipPolicy) {
        return stepBuilderFactory
                .get("ruiCollaboratoriCsvToDatabaseStep")
                .<RuiCollaboratori, RuiCollaboratori>chunk(CHUNK_SIZE)
                .reader(readerRuiCollaboratori)
                .processor(processorRuiCollaboratori)
                .writer(writerRuiCollaboratori)
                .faultTolerant()
                .skipPolicy(CustomSkipPolicy.builder().build())
                .listener(listenerCustomSkipPolicy)
                .build();
    }
}