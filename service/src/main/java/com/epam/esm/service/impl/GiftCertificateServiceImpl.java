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
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.exceptions.IllegalRequestParameterException;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of {@link com.epam.esm.service.GiftCertificateService} interface.
 *
 * @since 1.0
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED,
        readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final static String NAMES_PART_KEY = "namesPart";
    private final static String DESCRIPTION_PART_KEY = "descriptionsPart";
    private final static String TAG_NAMES_KEY = "tagNames";
    private final static String SORT_FIELDS_KEY = "sortFields";
    private final static String ORDER_KEY = "order";

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

    /**
     * This method do request to dao layer which depends on argument of the method.
     * If the argument of the method contains @see {@link #NAMES_PART_KEY} than will be called {@link #getAllByPartOfNames(String[]) method}.
     * If the argument of the method contains @see {@link #DESCRIPTION_PART_KEY} than will be called {@link #getAllByPartOfDescriptions(String[]) method}.
     * If the argument of the method contains @see {@link #TAG_NAMES_KEY} than will be called {@link #getAllByTagNames(String[]) method}.
     * If the argument of the method contains @see {@link #SORT_FIELDS_KEY} than will be called {@link #getAllSorted(String[], String)} method}.
     *
     * @param reqParams parameters of a request.
     * @return List of GiftCertificateDao.
     * @throws IllegalRequestParameterException if argument of the method contains {@link #SORT_FIELDS_KEY} key and
     *                                          doesn't contain {@link #ORDER_KEY} key at the same time.
     * @since 1.0
     */
    @Override
    public List<GiftCertificateDto> findAllForQuery(Map<String, String[]> reqParams) {
        for (Map.Entry<String, String[]> entry : reqParams.entrySet()) {
            String key = entry.getKey();
            switch (key) {
                case NAMES_PART_KEY: {
                    String[] namesPart = entry.getValue();
                    return getAllByPartOfNames(namesPart);
                }
                case DESCRIPTION_PART_KEY: {
                    String[] descriptionsPart = entry.getValue();
                    return getAllByPartOfDescriptions(descriptionsPart);
                }
                case TAG_NAMES_KEY: {
                    String[] tagNames = entry.getValue();
                    return getAllByTagNames(tagNames);
                }
                case SORT_FIELDS_KEY: {
                    String[] sortFields = entry.getValue();
                    String[] orders = reqParams.get(ORDER_KEY);
                    if (orders == null ||
                            (!orders[0].equalsIgnoreCase("desc") && !orders[0].equalsIgnoreCase("asc"))) {
                        throw new IllegalRequestParameterException("There is no correct order-parameter in request");
                    }
                    return getAllSorted(sortFields, orders[0]);
                }
            }
        }

        return getAll();
    }


    /**
     * This method gets GiftCertificate entity from dao layer with given id and converts it to GiftCertificateDto.
     * The method also calls {@link #fillCertificateDtoWithTags(GiftCertificateDto)} for filling gotten GiftCertificateDto
     * with list of TagDto.
     *
     * @param id id of necessary entity.
     * @return GiftCertificateDto with id and tags.
     * @throws GiftCertificateNotFoundException if there is no entity with given id in database.
     * @since 1.0
     */
    @Override
    public GiftCertificateDto findById(long id) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(id);

        GiftCertificate giftCertificate = certificateOpt.orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format("Can't find a certificate with id: %d", id)));

        GiftCertificateDto giftCertificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        fillCertificateDtoWithTags(giftCertificateDto);
        return giftCertificateDto;
    }

    /**
     * This method set date fields for GiftCertificate entity and saves it in database. Also the method link passed tags with the GiftCertificate
     * entity and, if the tag doesn't exist in db, creates it.
     *
     * @param certificate GiftCertificate entity without date fields and id for saving.
     * @return GiftCertificateDto with id and tags.
     * @since 1.0
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto save(GiftCertificateDto certificate) {
        GiftCertificate giftCertificate = modelMapper.map(certificate, GiftCertificate.class);
        String currentTime = DateProvider.getNowAsString();
        giftCertificate.setCreateDate(currentTime);
        giftCertificate.setLastUpdateDate(currentTime); //init dto

        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);

        Long certificateId = savedCertificate.getId();

        List<TagDto> tags = certificate.getTags();
        if (tags != null) {
            tags.forEach(tagDto -> {
                addTagToCertificate(tagDto, certificateId);
            });
        }                                           //check passed tags

        GiftCertificateDto savedCertificateDto = modelMapper.map(savedCertificate, GiftCertificateDto.class);
        fillCertificateDtoWithTags(savedCertificateDto);

        return savedCertificateDto;
    }


    /**
     * This method does full update for GiftCertificate entity with given id.
     *
     * @param certificateDto DTO contains field for updating GiftCertificate entity.
     * @param certificateId  id of updatable GiftCertificate entity.
     * @return GiftCertificateDto with id and tags.
     * @throws GiftCertificateNotFoundException if GiftCertificate entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto update(GiftCertificateDto certificateDto, long certificateId) {

        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

        certificateOpt.orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB", certificateId)));

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

        certificateDto.setId(certificateId);
        certificateDto.setCreateDate(certificateOpt.get().getCreateDate());
        return certificateDto;

    }


    /**
     * This method deletes GiftCertificate entity with given id from db.
     *
     * @param id id of deletable GiftCertificate entity.
     * @throws GiftCertificateNotFoundException if GiftCertificate entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Override
    public void delete(long id) {
        boolean isDeleted = giftCertificateDao.delete(id);
        if (!isDeleted) {
            throw new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB", id));
        }
    }

    /**
     * This method partly updates GiftCertificate entity.Also the method link passed tags with the GiftCertificate
     * entity and, if the tag doesn't exist in db, creates it.
     *
     * @param giftCertificatePatchDto DTO contains field for partial updating GiftCertificate entity.
     * @param certificateId           id of updatable GiftCertificate entity.
     * @return updated GiftCertificateDto with id and tags.
     * @throws GiftCertificateNotFoundException if GiftCertificate entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GiftCertificateDto patch(GiftCertificatePatchDto giftCertificatePatchDto, long certificateId) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.getById(certificateId);

        GiftCertificate certFromDb = certificateOpt.orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB", certificateId)));

        String name = giftCertificatePatchDto.getName();
        if (name != null) {
            certFromDb.setName(name);
        }
        String description = giftCertificatePatchDto.getDescription();
        if (description != null) {
            certFromDb.setDescription(description);
        }
        BigDecimal price = giftCertificatePatchDto.getPrice();
        if (price != null) {
            certFromDb.setPrice(price);
        }
        Integer duration = giftCertificatePatchDto.getDuration();
        if (duration != null) {
            certFromDb.setDuration(duration);
        }

        String currentTime = DateProvider.getNowAsString();
        certFromDb.setLastUpdateDate(currentTime);                  //init dto

        giftCertificateDao.update(certFromDb, certificateId);

        List<TagDto> passedTags = giftCertificatePatchDto.getTags();  //check passed tags
        List<Tag> certTags = new ArrayList<>(tagDao.getAllByCertificateId(certificateId));

        if (passedTags != null) {
            passedTags.forEach(tagDto -> {
                String tagDtoName = tagDto.getName();
                Optional<Tag> tagFromDbOpt = tagDao.getByName(tagDtoName); //check if the tag with such name in db

                if (tagFromDbOpt.isPresent()) {
                    Tag tagFromDb = tagFromDbOpt.get();
                    Long tagId = tagFromDb.getId();
                    boolean isTagAlreadyAttachedToCertificate = certTags.stream()
                            .map(Tag::getId)
                            .anyMatch(tagId::equals);
                    if (!isTagAlreadyAttachedToCertificate) {
                        certificateTagsDao.save(certificateId, tagFromDb.getId());
                        certTags.add(tagFromDb);
                    }
                } else {
                    Tag tagEntity = modelMapper.map(tagDto, Tag.class);
                    Tag savedTag = tagDao.save(tagEntity);
                    certificateTagsDao.save(certificateId, savedTag.getId());
                    certTags.add(savedTag);
                }
            });
        }

        List<TagDto> tagsDto = certTags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        GiftCertificateDto dto = modelMapper.map(certFromDb, GiftCertificateDto.class);
        dto.setTags(tagsDto);

        return dto;
    }

    /**
     * This method get all GiftCertificate entities from database and converts them to GiftCertificateDto.
     *
     * @return list of all GiftCertificateDto.
     * @throws GiftCertificateNotFoundException if there are no GiftCertificate entities in db.
     * @since 1.0
     */
    private List<GiftCertificateDto> getAll() {
        List<GiftCertificate> entities = giftCertificateDao.getAll();
        if (entities.isEmpty()) {
            throw new GiftCertificateNotFoundException("There are no gift certificates in DB");
        }

        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }

    /**
     * This method get all GiftCertificate entities by their tag names, convert them to DTO and fills
     * them with tags.
     *
     * @param tagNames array of tag names for search
     * @return list of GiftCertificateDto with given tag names for search.
     * @throws GiftCertificateNotFoundException if there are not GiftCertificate entities with such tag names in db.
     * @since 1.0
     */
    private List<GiftCertificateDto> getAllByTagNames(String[] tagNames) {

        Set<GiftCertificate> allEntities = new LinkedHashSet<>();

        Stream.of(tagNames).forEach(tagName -> {
            List<GiftCertificate> entities = giftCertificateDao.getAllByTagName(tagName);
            allEntities.addAll(entities);
        });

        if (allEntities.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format("There are no gift certificates with this tag name: %s in DB",
                    String.join(",", tagNames)));
        }
        return allEntities.stream()
                .map(certificate -> modelMapper.map(certificate, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }


    /**
     * This method get all GiftCertificate entities by parts of their descriptions, convert them to DTO and fills
     * them with tags.
     *
     * @param descriptionsPart array of description parts for search
     * @return list of GiftCertificateDto with given description parts for search.
     * @throws GiftCertificateNotFoundException if there are not GiftCertificate entities with such description parts in db.
     * @since 1.0
     */
    private List<GiftCertificateDto> getAllByPartOfDescriptions(String[] descriptionsPart) {
        Set<GiftCertificate> allEntities = new LinkedHashSet<>();

        Stream.of(descriptionsPart).forEach(descriptionPart -> {
            List<GiftCertificate> entities = giftCertificateDao.getAllByPartOfDescription(descriptionPart);
            allEntities.addAll(entities);
        });


        if (allEntities.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format("There are no gift certificates in DB with description like: %s",
                    String.join(",", descriptionsPart)));
        }
        return allEntities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }

    /**
     * This method get all GiftCertificate entities by parts of their names, convert them to DTO and fills
     * them with tags.
     *
     * @param namesPart array of names part for search
     * @return list of GiftCertificateDto with given names part for search.
     * @throws GiftCertificateNotFoundException if there are not GiftCertificate entities with such names part in db.
     * @since 1.0
     */
    private List<GiftCertificateDto> getAllByPartOfNames(String[] namesPart) {
        Set<GiftCertificate> allEntities = new LinkedHashSet<>();

        Stream.of(namesPart).forEach(namePart -> {
            List<GiftCertificate> entities = giftCertificateDao.getAllByPartOfName(namePart);
            allEntities.addAll(entities);
        });

        if (allEntities.isEmpty()) {
            throw new GiftCertificateNotFoundException(String.format("There are no gift certificates in DB with names like: %s",
                    String.join(",", namesPart)));
        }
        return allEntities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
    }


    /**
     * This method get all GiftCertificate entities from db sorted.
     *
     * @param sortingFieldNames field names for a sorting.
     * @param sortingOrder      order of the sorting.
     * @return list of all GiftCertificateDto sorted.
     * @throws GiftCertificateNotFoundException if there are not GiftCertificate entities in db.
     * @since 1.0
     */
    private List<GiftCertificateDto> getAllSorted(String[] sortingFieldNames, String sortingOrder) {
        List<GiftCertificate> entities = giftCertificateDao.getAllSorted(sortingFieldNames, sortingOrder);

        if (entities.isEmpty()) {
            throw new GiftCertificateNotFoundException("There are no gift certificates in DB");
        }
        return entities.stream()
                .map(entity -> modelMapper.map(entity, GiftCertificateDto.class))
                .peek(this::fillCertificateDtoWithTags)
                .collect(Collectors.toList());
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
        Optional<Tag> tagFromDbOpt = tagDao.getByName(tagDto.getName());
        Tag tag;
        if (tagFromDbOpt.isPresent()) {
            tag = tagFromDbOpt.get();
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
        List<Tag> tags = tagDao.getAllByCertificateId(giftCertificateDto.getId());
        List<TagDto> tagsDto = tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        giftCertificateDto.setTags(tagsDto);
    }


}
