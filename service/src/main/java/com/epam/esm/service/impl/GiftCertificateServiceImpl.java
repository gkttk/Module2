package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final Converter<GiftCertificate, GiftCertificateDto> converter;//todo generics

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.converter = new GiftCertificateConverter();//todo explicit instance
    }

    @Override
    public GiftCertificateDto getById(long id) {
        GiftCertificate entity = giftCertificateDao.getById(id);
        return converter.convertToDto(entity);

    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificate> entities = giftCertificateDao.findAll();
        return entities.stream().map(converter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = converter.convertToEntity(certificate);
        giftCertificateDao.save(giftCertificate);
    }

    @Override
    public void update(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = converter.convertToEntity(certificate);
        giftCertificateDao.update(giftCertificate);
    }

    @Override
    public void delete(long id) {
        giftCertificateDao.delete(id);
    }
}
