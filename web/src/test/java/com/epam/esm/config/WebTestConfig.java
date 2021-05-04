package com.epam.esm.config;

import com.epam.esm.assemblers.GiftCertificateModelAssembler;
import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.assemblers"})
public class WebTestConfig implements WebMvcConfigurer {




}
