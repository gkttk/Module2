package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Profile("test")
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:hikari-test.properties")
@ComponentScan(basePackages = {"com.epam.esm.dao"})
public class DaoTestConfig extends DaoConfig {

    private final static String HIKARI_PROPERTIES_PATH = "/hikari-test.properties";

    @Primary
    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES_PATH);
        return new HikariDataSource(config);
    }
}
