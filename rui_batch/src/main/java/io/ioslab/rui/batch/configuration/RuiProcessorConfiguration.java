package io.ioslab.rui.batch.configuration;

import io.ioslab.rui.batch.processor.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;

@Configuration
public class RuiProcessorConfiguration {

    @Bean
    @StepScope
    public RuiCaricheProcessor processorRuiCariche(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return RuiCaricheProcessor.builder()
                .date(date)
                .build();
    }

    @Bean
    @StepScope
    public RuiCollaboratoriProcessor processorRuiCollaboratori(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return RuiCollaboratoriProcessor.builder()
                .date(date)
                .build();
    }

    @Bean
    @StepScope
    public RuiIntermediariProcessor processorRuiIntermediari(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return RuiIntermediariProcessor.builder()
                .date(date)
                .build();
    }

    @Bean
    @StepScope
    public RuiMandatiProcessor processorRuiMandati(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return RuiMandatiProcessor.builder()
                .date(date)
                .build();
    }

    @Bean
    @StepScope
    public RuiSediProcessor processorRuiSedi(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date) {
        return RuiSediProcessor.builder()
                .date(date)
                .build();
    }
}
