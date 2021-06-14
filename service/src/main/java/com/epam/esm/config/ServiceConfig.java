package com.epam.esm.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm"})
public class ServiceConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
       /* Converter<String, UserRole> userRoleConverter = context -> context.getSource() == null ? null : UserRole.searchRole(context.getSource());

        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.using(userRoleConverter).map(UserDto::getRole, User::setRole));*/
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();

        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
                .serializeAllExcept("password");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("passwordFilter", theFilter);

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setFilterProvider(filters)
                .registerModule(module);
    }
}
