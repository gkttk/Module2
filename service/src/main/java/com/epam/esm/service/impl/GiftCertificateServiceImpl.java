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
import com.epam.esm.validator.EntityValidator;
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
    private final EntityValidator<GiftCertificate> validator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, ModelMapper modelMapper, TagDao tagDao,
                                      CertificateTagsDao certificateTagsDao, EntityValidator<GiftCertificate> giftCertificateEntityValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
        this.tagDao = tagDao;
        this.certificateTagsDao = certificateTagsDao;
        this.validator = giftCertificateEntityValidator;
    }

    /**
     * This method do request to dao layer which depends on argument of the method.
     * The method uses {@link GiftCertificateCriteriaFactory} for getting a correct {@link Criteria} which is based on passed request parameters.
     * If the argument of the method additionally contains {@link com.epam.esm.constants.ApplicationConstants} SORT_FIELDS_KEY parameter then
     * got result by {@link Criteria} will be sorted according to {@link com.epam.esm.constants.ApplicationConstants} ORDER_KEY parameter.
     * If {@link com.epam.esm.constants.ApplicationConstants} ORDER_KEY parameter is not
     * present or not equal "DESC" then sorting by ascending order.
     *
     * @param reqParams parameters of a request.
     * @param limit
     * @param offset
     * @return List of GiftCertificateDao.
     * @throws GiftCertificateException if there is no entity in database.
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
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.findById(id);

        GiftCertificate foundCertificate = certificateOpt.orElseThrow(() -> new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("Can't find a certificate with id: %d", id)));

        return modelMapper.map(foundCertificate, GiftCertificateDto.class);
    }


    private void fillTagLists(List<Tag> tagsForSaving, List<Tag> tagsForLinking, List<TagDto> passedDtoTags) {
        //filling previous lists
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

    /**
     * This method set date fields for GiftCertificate entity and saves it in database. Also the method link passed tags with the GiftCertificate
     * entity and, if the tag doesn't exist in db, creates it.
     *
     * @param passedDto GiftCertificate entity without date fields and id for saving.
     * @return GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto save(GiftCertificateDto passedDto) {
        //check certificate name
        validator.validateIfEntityWithGivenNameExist(passedDto.getName());

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

        //  GiftCertificate savedCertificate = attachTags(passedDto.getTags(), passedDto);

        return modelMapper.map(savedCertificate, GiftCertificateDto.class);

    }

    /**
     * This method does full update for GiftCertificate entity with given id.
     *
     * @param passedDto DTO contains field for updating GiftCertificate entity.
     * @param certId    id of updatable GiftCertificate entity.
     * @return GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto passedDto, long certId) {

        validator.validateAndFindByIdIfExist(certId);

        validator.validateIfAnotherEntityWithGivenNameExist(passedDto.getName(), certId);

        certificateTagsDao.deleteAllTagsForCertificate(certId);

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
     * This method partly updates GiftCertificate entity.Also the method link passed tags with the GiftCertificate
     * entity and, if the tag doesn't exist in db, creates it.
     *
     * @param passedDto DTO contains field for partial updating GiftCertificate entity.
     * @param certId    id of updatable GiftCertificate entity.
     * @return updated GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional
    public GiftCertificateDto patch(GiftCertificateDto passedDto, long certId) {

        GiftCertificate foundCert = validator.validateAndFindByIdIfExist(certId);//check if id exists

        changeEntityFieldsIfPresent(foundCert, passedDto, certId);//fill fields by passed GiftCertificateDto

        //collection of tags which dont exist in db
        List<Tag> tagsForSaving = new ArrayList<>();
        //collection of tags which exist in db
        List<Tag> tagsForLinking = new ArrayList<>();

        fillTagLists(tagsForSaving, tagsForLinking, passedDto.getTags());

        passedDto.setId(certId);
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
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("GiftCertificate with id: %d doesn't exist in DB", id));
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
            validator.validateIfAnotherEntityWithGivenNameExist(name, certificateId);
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


}
