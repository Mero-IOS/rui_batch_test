package io.ioslab.rui.batch.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateSource {

    @Bean
    @Profile("default")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(new CustomDataSource().getDataSource());
    }

    @Bean
    @Profile("test")
    public JdbcTemplate jdbcTemplateTest() {
        return new JdbcTemplate(new CustomDataSource().getDataSourceTest());
    }
}
