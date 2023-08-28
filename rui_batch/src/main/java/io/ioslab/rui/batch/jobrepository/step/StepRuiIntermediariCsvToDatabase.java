package io.ioslab.rui.batch.jobrepository.step;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.processor.RuiIntermediariProcessor;
import io.ioslab.rui.common.model.rui.RuiIntermediari;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.CHUNK_SIZE;

@Configuration
public class StepRuiIntermediariCsvToDatabase {

    @Bean
    public Step ruiIntermediariCsvToDatabaseStep(StepBuilderFactory stepBuilderFactory,
                                                 FlatFileItemReader<RuiIntermediari> readerRuiIntermediari,
                                                 RuiIntermediariProcessor processorRuiIntermediari,
                                                 JdbcBatchItemWriter<RuiIntermediari> writerRuiIntermediari,
                                                 CustomSkipPolicyListener listenerCustomSkipPolicy) {
        return stepBuilderFactory
                .get("ruiIntermediariCsvToDatabaseStep")
                .<RuiIntermediari, RuiIntermediari>chunk(CHUNK_SIZE)
                .reader(readerRuiIntermediari)
                .processor(processorRuiIntermediari)
                .writer(writerRuiIntermediari)
                .faultTolerant()
                .skipPolicy(CustomSkipPolicy.builder().build())
                .listener(listenerCustomSkipPolicy)
                .build();
    }
}
