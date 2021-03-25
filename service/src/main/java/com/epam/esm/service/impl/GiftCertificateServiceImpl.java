package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {


    private final ModelMapper modelMapper;
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final CertificateTagsDao certificateTagsDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, ModelMapper modelMapper, TagDao tagDao,
                                      CertificateTagsDao certificateTagsDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
        this.tagDao = tagDao;
        this.certificateTagsDao = certificateTagsDao;
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificate> entities = giftCertificateDao.findAll();

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(certificateDto -> {
                    List<Tag> tags = tagDao.getAllByCertificateId(certificateDto.getId());
                    List<TagDto> tagsDto = tags.stream()
                            .map(tag -> modelMapper.map(tag, TagDto.class))
                            .collect(Collectors.toList());
                    certificateDto.setTags(tagsDto);
                }).collect(Collectors.toList());

      /*  return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());*/
    }



    @Override
    public GiftCertificateDto getById(long id) {

        Optional<GiftCertificate> certificate = giftCertificateDao.getById(id);

        if (certificate.isPresent()) {
            GiftCertificate giftCertificate = certificate.get();
            return modelMapper.map(giftCertificate, GiftCertificateDto.class);
        }
        return null;

    }



    //transactional
    @Override
    public void save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);
        Long certificateId = savedCertificate.getId();

        List<TagDto> tags = certificate.getTags();
        if (tags != null) {
            tags.forEach(tag -> {
                Tag tagEntity = modelMapper.map(tag, Tag.class);
                Tag savedTag = tagDao.save(tagEntity);
                Long tagId = savedTag.getId();
                certificateTagsDao.save(certificateId, tagId);
            });
        }


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

    @Override
    public void patch(GiftCertificatePatchDto giftCertificatePatchDto, long id) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(id);
        if (certificateOpt.isPresent()) {
            GiftCertificate certificate = certificateOpt.get();
            String name = giftCertificatePatchDto.getName();
            if (name != null) {
                certificate.setName(name);
            }
            String description = giftCertificatePatchDto.getDescription();
            if (description != null) {
                certificate.setDescription(description);
            }
            BigDecimal price = giftCertificatePatchDto.getPrice();
            if (price != null) {
                certificate.setPrice(price);
            }

            Integer duration = giftCertificatePatchDto.getDuration();
            if (duration != null) {
                certificate.setDuration(duration);
            }

            giftCertificateDao.update(certificate, id);

        }
    }
}
