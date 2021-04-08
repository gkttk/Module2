package com.epam.esm.dao.config;

import com.epam.esm.constants.ApplicationConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Profile("test")
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:hikari-test.properties")
@ComponentScan("com.epam.esm")
public class DaoTestConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(ApplicationConstants.HIKARI_PROPERTIES_TEST_PATH);
        return new HikariDataSource(config);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


}
