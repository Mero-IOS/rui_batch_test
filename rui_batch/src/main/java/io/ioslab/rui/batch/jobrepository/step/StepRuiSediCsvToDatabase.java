package io.ioslab.rui.batch.jobrepository.step;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import io.ioslab.rui.batch.policies.CustomSkipPolicy;
import io.ioslab.rui.batch.processor.RuiSediProcessor;
import io.ioslab.rui.common.model.rui.RuiSedi;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.CHUNK_SIZE;

@Configuration
public class StepRuiSediCsvToDatabase {

    @Bean
    public Step ruiSediCsvToDatabaseStep(StepBuilderFactory stepBuilderFactory,
                                         FlatFileItemReader<RuiSedi> readerRuiSedi,
                                         RuiSediProcessor processorRuiSedi,
                                         JdbcBatchItemWriter<RuiSedi> writerRuiSedi,
                                         CustomSkipPolicyListener listenerCustomSkipPolicy) {
        return stepBuilderFactory
                .get("ruiSediCsvToDatabaseStep")
                .<RuiSedi, RuiSedi>chunk(CHUNK_SIZE)
                .reader(readerRuiSedi)
                .processor(processorRuiSedi)
                .writer(writerRuiSedi)
                .faultTolerant()
                .skipPolicy(CustomSkipPolicy.builder().build())
                .listener(listenerCustomSkipPolicy)
                .build();
    }
}
