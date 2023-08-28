package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.CreateOkFileTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;

@Configuration
public class CreateOkFileTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet createOkFileTasklet(@Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return CreateOkFileTasklet.builder()
                .outputPath(outputPath)
                .build();
    }
}
