package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
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
    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> entities = giftCertificateDao.findAll();

        if (entities.isEmpty()) {
            throw new GiftCertificateNotFoundException("There are no gift certificates in DB");
        }

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }


    @Override
    public List<GiftCertificateDto> getAllByPartOfName(String partOfName) {
        List<GiftCertificate> entities = giftCertificateDao.getAllByPartOfName(partOfName);

        if (entities.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format("There are no gift certificates in DB with names like: %s",
                    partOfName));
        }

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllSorted(List<String> sortingFieldNames, String sortingOrder) {
        List<GiftCertificate> entities = giftCertificateDao.getAllSorted(sortingFieldNames, sortingOrder);

        if (entities.isEmpty()) {
            throw new GiftCertificateNotFoundException("There are no gift certificates in DB");
        }

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllByTagName(String tagName) {
        List<GiftCertificate> certificates = giftCertificateDao.findAllByTagName(tagName);

        if (certificates.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format("There are no gift certificates with this tag name: %s in DB",
                    tagName));
        }

        return certificates.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());

    }

    @Override
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> certificate = giftCertificateDao.getById(id);

        if (!certificate.isPresent()) {
            throw new GiftCertificateNotFoundException(String.format("Can't find a certificate with id: %d", id));
        }

        GiftCertificateDto giftCertificateDto = modelMapper.map(certificate.get(), GiftCertificateDto.class);
        fillCertificateDtoWithTags(giftCertificateDto);
        return giftCertificateDto;


    }


    //transactional
    @Override
    public void save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);
        Long certificateId = savedCertificate.getId();

        List<TagDto> tags = certificate.getTags();
        if (tags != null) {
            tags.forEach(tagDto -> {
                addTagToCertificate(tagDto, certificateId);

              /*  Optional<Tag> tagFromDbOpt = tagDao.findByName(tagDto.getName());
                Long tagId;
                if (tagFromDbOpt.isPresent()) {
                    Tag tagFromDb = tagFromDbOpt.get();
                    tagId = tagFromDb.getId();
                } else {
                    Tag tagEntity = modelMapper.map(tagDto, Tag.class);
                    Tag savedTag = tagDao.save(tagEntity);
                    tagId = savedTag.getId();
                }
                certificateTagsDao.save(certificateId, tagId);*/
            });
        }

    }


    @Override
    public void update(GiftCertificateDto certificateDto, long certificateId) {

        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

        if (!certificateOpt.isPresent()) {
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                    certificateId));
        }

        GiftCertificate giftCertificate = modelMapper.map(certificateDto, GiftCertificate.class);
        giftCertificateDao.update(giftCertificate, certificateId);

        certificateTagsDao.deleteAllTagsForCertificate(certificateId);

        List<TagDto> tags = certificateDto.getTags();
        if (tags != null) {
            tags.forEach(tagDto -> addTagToCertificate(tagDto, certificateId));
        }

    }

    @Override
    public void delete(long id) {
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB"
                    , id));
        }
    }

    @Override
    public void patch(GiftCertificatePatchDto giftCertificatePatchDto, long certificateId) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

     /*   if (!certificateOpt.isPresent()) {
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                    certificateId));
        }*/

        GiftCertificate certificate = certificateOpt.orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                        certificateId)));

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

        giftCertificateDao.update(certificate, certificateId);

        List<TagDto> tags = giftCertificatePatchDto.getTags();
        if (tags != null) {
            List<Tag> certificateTags = tagDao.getAllByCertificateId(certificateId);

            tags.forEach(tagDto -> {
                String tagDtoName = tagDto.getName();
                Optional<Tag> tagFromDbOpt = tagDao.findByName(tagDtoName);

                if (tagFromDbOpt.isPresent()) {

                    Tag tagFromDb = tagFromDbOpt.get();
                    boolean isTagAlreadyAttachedToCertificate = certificateTags.stream()
                            .map(Tag::getId)
                            .anyMatch(id -> tagFromDb.getId().equals(id));
                    if (!isTagAlreadyAttachedToCertificate) {
                        certificateTagsDao.save(certificateId, tagFromDb.getId());
                    }
                } else {
                    Tag tagEntity = modelMapper.map(tagDto, Tag.class);
                    Tag savedTag = tagDao.save(tagEntity);
                    certificateTagsDao.save(certificateId, savedTag.getId());
                }
            });
        }
    }


    private void addTagToCertificate(TagDto tagDto, long certificateId) {
        Optional<Tag> tagFromDbOpt = tagDao.findByName(tagDto.getName());
        Long tagId;
        if (tagFromDbOpt.isPresent()) {
            Tag tagFromDb = tagFromDbOpt.get();
            tagId = tagFromDb.getId();
        } else {
            Tag tagEntity = modelMapper.map(tagDto, Tag.class);
            Tag savedTag = tagDao.save(tagEntity);
            tagId = savedTag.getId();
        }
        certificateTagsDao.save(certificateId, tagId);
    }


    private void fillCertificateDtoWithTags(GiftCertificateDto giftCertificateDto) {
        List<Tag> tags = tagDao.getAllByCertificateId(giftCertificateDto.getId());
        List<TagDto> tagsDto = tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        giftCertificateDto.setTags(tagsDto);
    }


}
