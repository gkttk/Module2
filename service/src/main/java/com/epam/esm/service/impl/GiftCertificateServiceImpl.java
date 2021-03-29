package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dateprovider.DateProvider;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.EntityNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED,
        readOnly = true)
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
            throw new EntityNotFoundException("There are no gift certificates in DB");
        }

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllByPartOfDescription(String partOfDescription) {
        List<GiftCertificate> entities = giftCertificateDao.getAllByPartOfDescription(partOfDescription);

        if (entities.isEmpty()) {
            throw new EntityNotFoundException(String.format("There are no gift certificates in DB with description like: %s",
                    partOfDescription));
        }
        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllByPartOfName(String partOfName) {
        List<GiftCertificate> entities = giftCertificateDao.getAllByPartOfName(partOfName);

        if (entities.isEmpty()) {
            throw new EntityNotFoundException(String.format("There are no gift certificates in DB with names like: %s",
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
            throw new EntityNotFoundException("There are no gift certificates in DB");
        }
        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllByTagName(String tagName) {
        List<GiftCertificate> certificates = giftCertificateDao.findAllByTagName(tagName);

        if (certificates.isEmpty()) {
            throw new EntityNotFoundException(String.format("There are no gift certificates with this tag name: %s in DB",
                    tagName));
        }
        return certificates.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(id);

        GiftCertificate giftCertificate = certificateOpt.orElseThrow(() -> new EntityNotFoundException(String.format("Can't find a certificate with id: %d",
                id)));

        GiftCertificateDto giftCertificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        fillCertificateDtoWithTags(giftCertificateDto);
        return giftCertificateDto;

    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        String currentTime = DateProvider.getNowAsString();
        giftCertificate.setCreateDate(currentTime);
        giftCertificate.setLastUpdateDate(currentTime);
        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);
        Long certificateId = savedCertificate.getId();

        List<TagDto> tags = certificate.getTags();
        if (tags != null) {
            tags.forEach(tagDto -> {
                addTagToCertificate(tagDto, certificateId);
            });
        }
        GiftCertificateDto certificateDto = modelMapper.map(savedCertificate, GiftCertificateDto.class);
        fillCertificateDtoWithTags(certificateDto);

        return certificateDto;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto update(GiftCertificateDto certificateDto, long certificateId) {

        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

        if (!certificateOpt.isPresent()) {
            throw new EntityNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                    certificateId));
        }

        String currentTime = DateProvider.getNowAsString();
        certificateDto.setLastUpdateDate(currentTime);

        GiftCertificate giftCertificate = modelMapper.map(certificateDto, GiftCertificate.class);
        giftCertificateDao.update(giftCertificate, certificateId);

        certificateTagsDao.deleteAllTagsForCertificate(certificateId);

        List<TagDto> tags = certificateDto.getTags();
        if (tags != null) {
            tags.forEach(tagDto -> {
                Tag tag = addTagToCertificate(tagDto, certificateId);
                tagDto.setId(tag.getId());
            });
        }

        return certificateDto;

    }

    @Override
    public void delete(long id) {
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new EntityNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB", id));
        }
    }

    //todo return dto
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto patch(GiftCertificatePatchDto giftCertificatePatchDto, long certificateId) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

        GiftCertificate certificate = certificateOpt.orElseThrow(() ->
                new EntityNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB",
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

        String currentTime = DateProvider.getNowAsString();
        certificate.setLastUpdateDate(currentTime);

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

        Optional<GiftCertificate> patchedEntityOpt = giftCertificateDao.getById(certificateId);
        GiftCertificate patchedEntity = patchedEntityOpt.get();
        return modelMapper.map(patchedEntity, GiftCertificateDto.class);

    }


    private Tag addTagToCertificate(TagDto tagDto, long certificateId) {
        Optional<Tag> tagFromDbOpt = tagDao.findByName(tagDto.getName());
        Long tagId;
        Tag tag;
        if (tagFromDbOpt.isPresent()) {
            tag = tagFromDbOpt.get();
        } else {
            Tag tagEntity = modelMapper.map(tagDto, Tag.class);
            tag = tagDao.save(tagEntity);
        }
        tagId = tag.getId();
        certificateTagsDao.save(certificateId, tagId);
        return tag;
    }


    private void fillCertificateDtoWithTags(GiftCertificateDto giftCertificateDto) {
        List<Tag> tags = tagDao.getAllByCertificateId(giftCertificateDto.getId());
        List<TagDto> tagsDto = tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        giftCertificateDto.setTags(tagsDto);
    }


}
