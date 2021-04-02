package com.epam.esm.dao.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Profile("test")
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:hikari-test.properties")
@ComponentScan(basePackages = {"com.epam.esm.dao", "com.epam.esm.mappers"})
public class DaoTestConfig {

    private final static String HIKARI_PROPERTIES_PATH = "/hikari-test.properties";

    @Primary
    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES_PATH);
        return new HikariDataSource(config);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }


}
