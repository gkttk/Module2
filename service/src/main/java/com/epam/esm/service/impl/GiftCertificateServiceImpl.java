package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final ModelMapper modelMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, ModelMapper modelMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public GiftCertificateDto getById(long id) {
        GiftCertificate entity = giftCertificateDao.getById(id);
        return modelMapper.map(entity, GiftCertificateDto.class);

    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificate> entities = giftCertificateDao.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        giftCertificateDao.save(giftCertificate);
    }

    @Override
    public void update(GiftCertificateDto certificate, long id) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        giftCertificateDao.update(giftCertificate, id);
    }

    @Override
    public void delete(long id) {
        giftCertificateDao.delete(id);
    }
}
