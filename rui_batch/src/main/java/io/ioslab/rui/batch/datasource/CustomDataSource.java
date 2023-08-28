package io.ioslab.rui.batch.datasource;

import io.ioslab.rui.common.model.ini.MySqlModel;
import io.ioslab.rui.common.service.ini.IniReader;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class CustomDataSource {

    @Bean
    @Profile("default")
    public DataSource getDataSource() {
        MySqlModel dataSourceModel = IniReader.getIniReaderder().getMySqlModelFromIni();
        return DataSourceBuilder.create()
                .url(dataSourceModel.getUrl())
                .username(dataSourceModel.getUsername())
                .password(dataSourceModel.getPassword())
                .driverClassName(dataSourceModel.getDriverClassName())
                .build();
    }

    @Bean
    @Profile("test")
    public DataSource getDataSourceTest() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:rui_ivass_test")
                .username("sa")
                .password("")
                .driverClassName("org.h2.Driver")
                .build();
    }
}
