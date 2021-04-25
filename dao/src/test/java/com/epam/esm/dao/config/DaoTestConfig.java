/*
package com.epam.esm.dao.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Profile("test")
//@TestConfiguration
//@EnableTransactionManagement
//@PropertySource("classpath:hikari-test.properties")
*/
/*@PropertySource("classpath:application-test.properties")
@ComponentScan("com.epam.esm")*//*

@Configuration
public class DaoTestConfig {

  */
/*  @Bean
    // @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(ApplicationConstants.HIKARI_PROPERTIES_TEST_PATH);
        return new HikariDataSource(config);

        //return DataSourceBuilder.create().build();
    }*//*



    @Bean
    @Primary
    public DataSourceProperties getDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }


}
*/
