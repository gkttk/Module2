package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:dao.properties")
@ComponentScan(basePackages = {"com.epam.esm.dao"})
public class DaoConfig {

    @Value("${driver_name}")
    private String driverName;
    @Value("${login}")
    private String login;
    @Value("${password}")
    private String password;
    @Value("${url}")
    private String url;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUsername(login);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

}
