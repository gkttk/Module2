package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.factory.GiftCertificateCriteriaFactory;
import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.sorting.GiftCertificateSortingHelper;
import com.epam.esm.sorting.SortingHelper;
import com.epam.esm.validator.EntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    private final SortingHelper<GiftCertificate> sortingHelper;

    private final EntityValidator<GiftCertificate> validator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, ModelMapper modelMapper, TagDao tagDao,
                                      CertificateTagsDao certificateTagsDao, GiftCertificateSortingHelper sortingHelper,
                                      EntityValidator<GiftCertificate> giftCertificateEntityValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
        this.tagDao = tagDao;
        this.certificateTagsDao = certificateTagsDao;
        this.sortingHelper = sortingHelper;
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
     * @return List of GiftCertificateDao.
     * @throws GiftCertificateException if there is no entity in database.
     * @since 1.0
     */

    @Override
    public List<GiftCertificateDto> findAllForQuery(Map<String, String[]> reqParams) {

        List<GiftCertificate> foundCertificates = giftCertificateDao.findBy(reqParams);

        if (!foundCertificates.isEmpty() && reqParams.containsKey(ApplicationConstants.SORT_FIELDS_KEY)) {
            String[] sortFields = reqParams.get(ApplicationConstants.SORT_FIELDS_KEY);
            String[] orders = reqParams.get(ApplicationConstants.ORDER_KEY);

            foundCertificates = sortingHelper.getSorted(sortFields, orders, foundCertificates);
        }

        return foundCertificates.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }

    /**
     * This method gets GiftCertificate entity from dao layer with given id and converts it to GiftCertificateDto.
     * The method also calls {@link #fillCertificateDtoWithTags(GiftCertificateDto)} for filling gotten GiftCertificateDto
     * with list of TagDto.
     *
     * @param id id of necessary entity.
     * @return GiftCertificateDto with id and tags.
     * @throws GiftCertificateException if there is no entity with given id in database.
     * @since 1.0
     */
    @Override
    public GiftCertificateDto findById(long id) {
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.findById(id);

        GiftCertificate foundCertificate = foundCertificateOpt.orElseThrow(() ->
                new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("Can't find a certificate with id: %d", id)));

        GiftCertificateDto giftCertificateDto = modelMapper.map(foundCertificate, GiftCertificateDto.class);
        fillCertificateDtoWithTags(giftCertificateDto);
        return giftCertificateDto;
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

        validator.validateIfEntityWithGivenNameExist(passedDto.getName());

        GiftCertificate giftCertificate = modelMapper.map(passedDto, GiftCertificate.class);
        long insertedId = giftCertificateDao.save(giftCertificate);

        GiftCertificateDto dto = findDto(insertedId);
        List<TagDto> tags = findTagsDto(passedDto.getTags(), insertedId);
        dto.setTags(tags);
        return dto;

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

        String passedDtoName = passedDto.getName();

        validator.validateIfAnotherEntityWithGivenNameExist(passedDtoName, certId);

        GiftCertificate giftCert = modelMapper.map(passedDto, GiftCertificate.class);
        giftCertificateDao.update(giftCert, certId);

        certificateTagsDao.deleteAllTagsForCertificate(certId);

        GiftCertificateDto dto = findDto(certId);
        List<TagDto> tags = findTagsDto(passedDto.getTags(), certId);
        dto.setTags(tags);
        return dto;
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

        giftCertificateDao.update(foundCert, certId); //update

        GiftCertificateDto dto = findDto(certId);

        List<TagDto> passedTags = passedDto.getTags();
        if (passedTags != null) {
            certificateTagsDao.deleteAllTagsForCertificate(certId);
            List<TagDto> tagsDto = findTagsDto(passedTags, certId);
            dto.setTags(tagsDto);
        } else {
            fillCertificateDtoWithTags(dto);
        }

        return dto;
    }


    /**
     * This method deletes GiftCertificate entity with given id from db.
     *
     * @param id id of deletable GiftCertificate entity.
     * @throws GiftCertificateException if GiftCertificate entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Override
    public void delete(long id) {
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("GiftCertificate with id: %d doesn't exist in DB", id));
        }
    }

    /**
     * This method link a tag with a GiftCertificate entity and, if given tag is not present in db, creates it.
     *
     * @param tagDto        tag for linking with GiftCertificate entity.
     * @param certificateId id of GiftCertificate entity.
     * @return Tag entity from db with id.
     * @since 1.0
     */
    private Tag addTagToCertificate(TagDto tagDto, long certificateId) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagDto.getName());
        Tag tag;
        if (foundTagOpt.isPresent()) {
            tag = foundTagOpt.get();
        } else {
            Tag tagEntity = modelMapper.map(tagDto, Tag.class);
            tag = tagDao.save(tagEntity);
        }
        Long tagId = tag.getId();
        certificateTagsDao.save(certificateId, tagId);
        return tag;
    }

    /**
     * This method fills GiftCertificateDto with tags.
     *
     * @param giftCertificateDto GiftCertificateDto for filling.
     * @since 1.0
     */
    private void fillCertificateDtoWithTags(GiftCertificateDto giftCertificateDto) {
        long certificateId = giftCertificateDto.getId();
        Map<String, String[]> params = Collections.singletonMap(ApplicationConstants.CERTIFICATE_ID_KEY, new String[]{String.valueOf(certificateId)});
        List<Tag> tags = tagDao.findBy(params);
        List<TagDto> tagsDto = tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        giftCertificateDto.setTags(tagsDto);
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

    /**
     * This method finds a GiftCertificate entity and converts it to GiftCertificateDto.
     *
     * @param certId GiftCertificate entity's id.
     * @return GiftCertificateDto
     * @throws GiftCertificateException when there is no entity with given {@param certId} in DB.
     */
    private GiftCertificateDto findDto(long certId) {
        Optional<GiftCertificate> certOpt = giftCertificateDao.findById(certId);
        GiftCertificate certEntity = certOpt.orElseThrow(() ->
                new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("GiftCertificate with id: %d is not found", certId)));
        return modelMapper.map(certEntity, GiftCertificateDto.class);
    }


    /**
     * This method adds each {@param passedTags} in DB and bounds it with GiftCertificate with id = {@param certId}. Or, if a tag is already
     * present in DB, it just bounds it with GiftCertificate.
     *
     * @param passedTags tags for checking.
     * @param certId     GiftCertificate entity's id.
     * @return list of passed TagDto with id for current GiftCertificate
     */
    private List<TagDto> findTagsDto(List<TagDto> passedTags, long certId) {
        if (passedTags == null) {
            return null;
        }
        Set<TagDto> setPassedTags = new HashSet<>(passedTags);
        return setPassedTags.stream()
                .peek(tagDto -> {
                    Tag tag = addTagToCertificate(tagDto, certId);
                    tagDto.setId(tag.getId());
                })
                .collect(Collectors.toList());
    }


}
