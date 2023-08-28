package io.ioslab.rui.batch.jobrepository.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepCreateOkFile {

    @Bean
    public Step createOkFileStep(StepBuilderFactory stepBuilderFactory,
                                 Tasklet createOkFileTasklet) {
        return stepBuilderFactory
                .get("createOkFileStep")
                .tasklet(createOkFileTasklet)
                .build();
    }
}
