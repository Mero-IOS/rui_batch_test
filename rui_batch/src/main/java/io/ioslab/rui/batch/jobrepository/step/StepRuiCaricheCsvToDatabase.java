package io.ioslab.rui.batch.jobrepository.step;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.processor.RuiCaricheProcessor;
import io.ioslab.rui.common.model.rui.RuiCariche;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.CHUNK_SIZE;

@Configuration
public class StepRuiCaricheCsvToDatabase {

    @Bean
    public Step ruiCaricheCsvToDatabaseStep(StepBuilderFactory stepBuilderFactory,
                                            FlatFileItemReader<RuiCariche> readerRuiCariche,
                                            RuiCaricheProcessor processorRuiCariche,
                                            JdbcBatchItemWriter<RuiCariche> writerRuiCariche,
                                            CustomSkipPolicyListener listenerCustomSkipPolicy) {
        return stepBuilderFactory
                .get("ruiCaricheCsvToDatabaseStep")
                .<RuiCariche, RuiCariche>chunk(CHUNK_SIZE)
                .reader(readerRuiCariche)
                .processor(processorRuiCariche)
                .writer(writerRuiCariche)
                .faultTolerant()
                .skipPolicy(CustomSkipPolicy.builder().build())
                .listener(listenerCustomSkipPolicy)
                .build();
    }
}
