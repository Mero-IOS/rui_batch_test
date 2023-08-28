package io.ioslab.rui.batch.writer;

import io.ioslab.rui.common.model.rui.RuiIntermediari;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static io.ioslab.rui.batch.utility.SqlQuery.INSERT_RUI_INTERMEDIARI;

@Configuration
public class RuiIntermediariWriter {

    @Bean
    @StepScope
    public JdbcBatchItemWriter<RuiIntermediari> writerRuiIntermediari(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RuiIntermediari>()
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_RUI_INTERMEDIARI.getQuery())
                .dataSource(dataSource)
                .build();
    }
}
