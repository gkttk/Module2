package com.epam.esm.dao.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("test")
@EnableTransactionManagement
@EnableAutoConfiguration
@Configuration
@ComponentScan("com.epam.esm")
@EntityScan(basePackages = {"com.epam.esm.entity"})
public class DaoTestConfig {
}

