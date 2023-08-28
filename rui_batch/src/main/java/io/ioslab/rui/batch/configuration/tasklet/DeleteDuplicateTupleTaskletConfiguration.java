package io.ioslab.rui.batch.configuration.tasklet;

import io.ioslab.rui.batch.tasklet.DeleteDuplicateTupleTasklet;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.ioslab.rui.batch.utility.Costants.PARAMETER_DATE_CSV;

@Configuration
public class DeleteDuplicateTupleTaskletConfiguration {

    @Bean
    @StepScope
    public Tasklet deleteDuplicateTupleTasklet(@Value("#{jobExecutionContext['" + PARAMETER_DATE_CSV + "']}") String date,
                                               JdbcTemplate jdbcTemplate) {
        return DeleteDuplicateTupleTasklet.builder()
                                          .date(date)
                                          .jdbcTemplate(jdbcTemplate)
                                          .build();
    }
}
