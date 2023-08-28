package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.UnzipTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.*;

@Configuration
public class UnzipTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet unzipTasklet(@Value("#{jobExecutionContext['" + PARAMETER_INPUT_PATH + "']}") String inputPath,
                                @Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date,
                                @Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return UnzipTasklet.builder()
                .inputPath(inputPath)
                .date(date)
                .outputPath(outputPath)
                .build();
    }
}
