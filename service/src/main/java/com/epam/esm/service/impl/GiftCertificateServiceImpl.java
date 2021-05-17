package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.service.GiftCertificateService} interface.
 *
 * @since 1.0
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ModelMapper modelMapper;
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final CertificateTagsDao certificateTagsDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, ModelMapper modelMapper,
                                      TagDao tagDao, CertificateTagsDao certificateTagsDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
        this.tagDao = tagDao;
        this.certificateTagsDao = certificateTagsDao;
    }

    /**
     * This method gets a list of GiftCertificateDto according to request parameters, limit and offset.
     *
     * @param reqParams parameters of a request.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of GiftCertificateDao.
     * @since 1.0
     */
    @Override
    public List<GiftCertificateDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {

        List<GiftCertificate> foundCertificates = giftCertificateDao.findBy(reqParams, limit, offset);
        return foundCertificates.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    /**
     * This method gets GiftCertificate entity from dao layer with given id and converts it to GiftCertificateDto.
     *
     * @param id id of necessary entity.
     * @return GiftCertificateDto with id and tags.
     * @throws GiftCertificateException if there is no entity with given id in database.
     * @since 1.0
     */
    @Override
    public GiftCertificateDto findById(long id) {
        GiftCertificate foundCertificate = findByIdIfExist(id);
        return modelMapper.map(foundCertificate, GiftCertificateDto.class);
    }


    /**
     * This method separate passed tags to two lists: the first one are tags for saving(these tags are not present in DB),
     * the second one are tags for linking(these tags are present in DB). Tags for saving will be set to entity and saved in DB.
     * Tags for linking will be just bound with new saved GiftCertificate.
     *
     * @param passedDto GiftCertificate for saving.
     * @return saved GiftCertificate.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto save(GiftCertificateDto passedDto) {
        //check certificate name
        checkIfEntityWithGivenNameExist(passedDto.getName());

        //collection of tags which dont exist in db
        List<Tag> tagsForSaving = new ArrayList<>();
        //collection of tags which exist in db
        List<Tag> tagsForLinking = new ArrayList<>();

        fillTagLists(tagsForSaving, tagsForLinking, passedDto.getTags());

        GiftCertificate giftCertificate = modelMapper.map(passedDto, GiftCertificate.class);
        //set to saving certificate collection of nonexistent tags in db
        giftCertificate.setTags(tagsForSaving);

        //save certificate with tags
        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);

        //for each tags in list of existing tags in db just save them in many to many table
        tagsForLinking.forEach(tag -> certificateTagsDao.save(savedCertificate.getId(), tag.getId()));

        //union all tags for returning to client
        giftCertificate.getTags().addAll(tagsForLinking);

        return modelMapper.map(savedCertificate, GiftCertificateDto.class);

    }

    /**
     * This method separate passed tags to two lists: the first one are tags for saving(these tags are not present in DB),
     * the second one are tags for linking(these tags are present in DB). Tags for saving will be set to entity and saved in DB.
     * Tags for linking will be just bound with new updated GiftCertificate.
     *
     * @param passedDto DTO contains field for updating GiftCertificate entity.
     * @param certId    id of updatable GiftCertificate entity.
     * @return GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto passedDto, long certId) {

        findByIdIfExist(certId);

        checkIfAnotherEntityWithGivenNameExist(passedDto.getName(), certId);

        certificateTagsDao.deleteAllTagLinksForCertificateId(certId);

        //collection of tags which dont exist in db
        List<Tag> tagsForSaving = new ArrayList<>();
        //collection of tags which exist in db
        List<Tag> tagsForLinking = new ArrayList<>();

        fillTagLists(tagsForSaving, tagsForLinking, passedDto.getTags());

        passedDto.setId(certId);
        GiftCertificate giftCertificate = modelMapper.map(passedDto, GiftCertificate.class);

        //set to saving certificate collection of nonexistent tags in db
        giftCertificate.setTags(tagsForSaving);

        GiftCertificate updatedCertificate = giftCertificateDao.update(giftCertificate);

        tagsForLinking.forEach(tag -> certificateTagsDao.save(updatedCertificate.getId(), tag.getId()));

        updatedCertificate.getTags().addAll(tagsForLinking);

        return modelMapper.map(updatedCertificate, GiftCertificateDto.class);
    }

    /**
     * Partial update of GiftCertificate.
     *
     * @param passedDto DTO contains field for partial updating GiftCertificate entity.
     * @param certId    id of updatable GiftCertificate entity.
     * @return updated GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto patch(GiftCertificateDto passedDto, long certId) {

        GiftCertificate foundCert = findByIdIfExist(certId);//check if id exists

        changeEntityFieldsIfPresent(foundCert, passedDto, certId);//fill fields by passed GiftCertificateDto

        List<TagDto> passedTags = passedDto.getTags();
        if (passedTags == null) {
            GiftCertificate patchedCertificate = giftCertificateDao.update(foundCert);
            return modelMapper.map(patchedCertificate, GiftCertificateDto.class);
        }

        //collection of tags which dont exist in db
        List<Tag> tagsForSaving = new ArrayList<>();
        //collection of tags which exist in db
        List<Tag> tagsForLinking = new ArrayList<>();

        fillTagLists(tagsForSaving, tagsForLinking, passedTags);

        foundCert.setTags(tagsForSaving);
        GiftCertificate patchedCertificate = giftCertificateDao.update(foundCert);//update

        tagsForLinking.forEach(tag -> certificateTagsDao.save(patchedCertificate.getId(), tag.getId()));

        patchedCertificate.getTags().addAll(tagsForLinking);

        return modelMapper.map(patchedCertificate, GiftCertificateDto.class);
    }


    /**
     * This method deletes GiftCertificate entity with given id from db.
     *
     * @param id id of deletable GiftCertificate entity.
     * @throws GiftCertificateException if GiftCertificate entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Transactional
    @Override
    public void delete(long id) {
        certificateTagsDao.deleteAllTagLinksForCertificateId(id);
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new GiftCertificateException(String.format("GiftCertificate with id: %d doesn't exist in DB", id),
                    ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, id);
        }
    }


    /**
     * This method fills {@param targetEntity} param by present fields of {@param fromDto} param and sets an update_time field.
     *
     * @param targetEntity  GiftCertificate entity from db.
     * @param fromDto       Passed GiftCertificateDto with fields for patch.
     * @param certificateId id of GiftCertificate entity from db.
     * @since 1.0
     */
    private void changeEntityFieldsIfPresent(GiftCertificate targetEntity, GiftCertificateDto fromDto, long certificateId) {
        String name = fromDto.getName();
        if (name != null) {
            checkIfAnotherEntityWithGivenNameExist(name, certificateId);
            targetEntity.setName(name);
        }
        String description = fromDto.getDescription();
        if (description != null) {
            targetEntity.setDescription(description);
        }
        BigDecimal price = fromDto.getPrice();
        if (price != null) {
            targetEntity.setPrice(price);
        }
        Integer duration = fromDto.getDuration();
        if (duration != null) {
            targetEntity.setDuration(duration);
        }

    }


    /**
     * This method fills two given lists with passed Tags depends on necessity of saving or linking the tag.
     *
     * @param tagsForSaving  list of Tags for saving.
     * @param tagsForLinking list of Tags for linking.
     * @param passedDtoTags  passed Tags.
     */
    private void fillTagLists(List<Tag> tagsForSaving, List<Tag> tagsForLinking, List<TagDto> passedDtoTags) {
        //filling previous lists
        if (passedDtoTags != null) {
            passedDtoTags.stream()
                    .distinct()
                    .map(tagDto -> modelMapper.map(tagDto, Tag.class))
                    .forEach(tag -> {
                        Optional<Tag> foundTagOpt = tagDao.findByName(tag.getName());
                        if (!foundTagOpt.isPresent()) {
                            tagsForSaving.add(tag);
                        } else {
                            Tag foundTag = foundTagOpt.get();
                            tagsForLinking.add(foundTag);
                        }
                    });
        }
    }


    /**
     * This method return an GiftCertificate if it exists.
     *
     * @param id GiftCertificate's id.
     * @return GiftCertificate entity.
     * @throws GiftCertificateException if there is no entity with given id in db.
     */
    private GiftCertificate findByIdIfExist(long id) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.findById(id);
        return certificateOpt.orElseThrow(() -> new GiftCertificateException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                id), ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, id));
    }

    /**
     * This method checks if a GiftCertificate entity with given name exists in db.
     *
     * @param certificateName name of the GiftCertificate entity.
     * @throws GiftCertificateException if there is GiftCertificate entity with given name in db.
     * @since 1.0
     */
    private void checkIfEntityWithGivenNameExist(String certificateName) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.findByName(certificateName);
        if (certificateOpt.isPresent()) {
            throw new GiftCertificateException(String.format("Gift certificate with name: %s already exits.",
                    certificateName), ApplicationConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE, certificateName);
        }
    }

    /**
     * This method checks if GiftCertificate entity with given name and another id is present in db.
     *
     * @param certificateName name of GiftCertificate entity
     * @param certificateId   id of GiftCertificate entity
     * @throws GiftCertificateException when there is another GiftCertificate entity in db with given name.
     * @since 1.0
     */
    private void checkIfAnotherEntityWithGivenNameExist(String certificateName, long certificateId) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.findByName(certificateName);
        certificateOpt.ifPresent(giftCertificate -> {
            if (!giftCertificate.getId().equals(certificateId)) {
                String foundCertificateName = giftCertificate.getName();
                throw new GiftCertificateException(String.format("Gift certificate with name: %s already exits.",
                        foundCertificateName), ApplicationConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE, foundCertificateName);
            }
        });
    }


}
