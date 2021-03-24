package com.epam.esm.config;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.converter.impl.TagConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.service"})
public class ServiceConfig {

    @Bean
    public Converter<GiftCertificate, GiftCertificateDto> getGiftCertificateConverter() {
        return new GiftCertificateConverter();
    }

    @Bean
    public Converter<Tag, TagDto> getTagConverter() {
        return new TagConverter();
    }

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

}
