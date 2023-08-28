package io.ioslab.rui.batch.jobrepository.step;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.processor.RuiMandatiProcessor;
import io.ioslab.rui.common.model.rui.RuiMandati;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.CHUNK_SIZE;

@Configuration
public class StepRuiMandatiCsvToDatabase {

    @Bean
    public Step ruiMandatiCsvToDatabaseStep(StepBuilderFactory stepBuilderFactory,
                                            FlatFileItemReader<RuiMandati> readerRuiMandati,
                                            RuiMandatiProcessor processorRuiMandati,
                                            JdbcBatchItemWriter<RuiMandati> writerRuiMandati,
                                            CustomSkipPolicyListener listenerCustomSkipPolicy) {
        return stepBuilderFactory
                .get("ruiMandatiCsvToDatabaseStep")
                .<RuiMandati, RuiMandati>chunk(CHUNK_SIZE)
                .reader(readerRuiMandati)
                .processor(processorRuiMandati)
                .writer(writerRuiMandati)
                .faultTolerant()
                .skipPolicy(CustomSkipPolicy.builder().build())
                .listener(listenerCustomSkipPolicy)
                .build();
    }
}
