package com.epam.esm.dao.config;

import com.epam.esm.constants.ApplicationConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@Profile("test")
@Configuration
@EnableTransactionManagement
//@PropertySource("classpath:hikari-test.properties")
@PropertySource("classpath:application-integrationtest.properties")
@ComponentScan("com.epam.esm")
public class DaoTestConfig {




    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {


       /*return  DataSourceBuilder.create().build();*/
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
