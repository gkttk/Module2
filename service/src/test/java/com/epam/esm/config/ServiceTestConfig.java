package com.epam.esm.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm"})
public class ServiceTestConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
