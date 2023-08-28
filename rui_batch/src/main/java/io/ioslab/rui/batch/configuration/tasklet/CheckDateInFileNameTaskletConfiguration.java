package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.CheckDateInFileNameTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;
import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;

@Configuration
public class CheckDateInFileNameTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet checkDateInFileNameTasklet(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date,
                                              @Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return CheckDateInFileNameTasklet.builder()
                .date(date)
                .outputPath(outputPath)
                .build();
    }
}
