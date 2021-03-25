package com.epam.esm.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.service"})
public class ServiceConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
