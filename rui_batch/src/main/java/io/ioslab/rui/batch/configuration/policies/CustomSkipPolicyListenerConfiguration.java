package io.ioslab.rui.batch.configuration.policies;

import io.ioslab.rui.batch.listener.CustomSkipPolicyListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_OUTPUT_PATH;

@Configuration
public class CustomSkipPolicyListenerConfiguration {

    @Bean
    @StepScope
    public CustomSkipPolicyListener listenerCustomSkipPolicy(@Value("#{jobExecutionContext['" + PARAMETER_OUTPUT_PATH + "']}") String outputPath) {
        return new CustomSkipPolicyListener(outputPath);
    }
}
