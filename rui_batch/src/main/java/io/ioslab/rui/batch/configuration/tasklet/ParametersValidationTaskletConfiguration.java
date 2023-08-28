package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.ParametersValidationTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.*;

@Configuration
public class ParametersValidationTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet parametersValidationTasklet(@Value("#{jobParameters['" + PARAMETER_URL + "']}") String url,
                                               @Value("#{jobParameters['" + PARAMETER_DATE_CSV + "']}") String date,
                                               @Value("#{jobParameters['" + PARAMETER_INPUT_PATH + "']}") String inputPath,
                                               @Value("#{jobParameters['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return ParametersValidationTasklet.builder()
                .url(url)
                .date(date)
                .inputPath(inputPath)
                .outputPath(outputPath)
                .build();
    }
}
