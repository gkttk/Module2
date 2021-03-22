package com.epam.esm.service.converter.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.converter.Converter;

import java.math.BigDecimal;
import java.util.Date;

public class GiftCertificateConverter implements Converter<GiftCertificate, GiftCertificateDto> {
    @Override
    public GiftCertificateDto convertToDto(GiftCertificate entity) {
        Long id = entity.getId();
        String name = entity.getName();
        String description = entity.getDescription();
        BigDecimal price = entity.getPrice();
        int duration = entity.getDuration();
        Date createDate = entity.getCreateDate();
        Date lastUpdateDate = entity.getLastUpdateDate();
        return new GiftCertificateDto(id, name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public GiftCertificate convertToEntity(GiftCertificateDto dto) {
        Long id = dto.getId();
        String name = dto.getName();
        String description = dto.getDescription();
        BigDecimal price = dto.getPrice();
        int duration = dto.getDuration();
        Date createDate = dto.getCreateDate();
        Date lastUpdateDate = dto.getLastUpdateDate();
        return new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
    }
}
