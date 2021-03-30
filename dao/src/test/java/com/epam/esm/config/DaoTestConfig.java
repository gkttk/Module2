package com.epam.esm.config;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mappers.GiftCertificateRowMapper;
import com.epam.esm.mappers.TagRowMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:hikari-test.properties")
@ComponentScan(basePackages = {"com.epam.esm.dao"})
public class DaoTestConfig {

    private final static String HIKARI_PROPERTIES_PATH = "/hikari-test.properties";


    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new DataSourceTransactionManager(getDataSource());
    }

    @Bean
    public RowMapper<GiftCertificate> getGiftCertificateRowMapper() {
        return new GiftCertificateRowMapper();
    }

    @Bean
    public RowMapper<Tag> getTagRowMapper() {
        return new TagRowMapper();
    }


    @Bean
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES_PATH);
        return new HikariDataSource(config);

    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

}
