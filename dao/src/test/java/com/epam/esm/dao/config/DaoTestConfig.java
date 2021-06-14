package com.epam.esm.dao.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@Configuration
@ComponentScan("com.epam.esm")
@EnableJpaRepositories("com.epam.esm.dao.domain")
@EntityScan(basePackages = {"com.epam.esm.entity"})
public class DaoTestConfig {
}

