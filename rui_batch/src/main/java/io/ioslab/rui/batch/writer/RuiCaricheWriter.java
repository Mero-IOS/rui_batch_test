package io.ioslab.rui.batch.writer;

import io.ioslab.rui.common.model.rui.RuiCariche;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static io.ioslab.rui.batch.utility.SqlQuery.INSERT_RUI_CARICHE;

@Configuration
public class RuiCaricheWriter {

    @Bean
    @StepScope
    public JdbcBatchItemWriter<RuiCariche> writerRuiCariche(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RuiCariche>()
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_RUI_CARICHE.getQuery())
                .dataSource(dataSource)
                .build();
    }
}
