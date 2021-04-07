package com.epam.esm.dao.impl;

import com.epam.esm.criteria.certificates.AllGiftCertificateCriteria;
import com.epam.esm.criteria.certificates.TagNamesGiftCertificateCriteria;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
@ActiveProfiles("test")
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    private TagNamesGiftCertificateCriteria tagNamesGiftCertificateCriteria;

    @Autowired
    private AllGiftCertificateCriteria allGiftCertificateCriteria;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static GiftCertificate certificate1;
    private static GiftCertificate certificate2;
    private static GiftCertificate certificate3;


    @BeforeAll
    static void init() {



        certificate1 = new GiftCertificate();
        certificate1.setId(1L);
        certificate1.setName("certificate1");
        certificate1.setDescription("description1");
        certificate1.setPrice(new BigDecimal("1.5"));
        certificate1.setDuration(10);

        certificate1.setCreateDate(LocalDateTime.parse("2021-03-29 11:24:04", formatter));
        certificate1.setLastUpdateDate(LocalDateTime.parse("2021-03-29 11:30:18", formatter));

        certificate2 = new GiftCertificate();
        certificate2.setId(2L);
        certificate2.setName("certificate2");
        certificate2.setDescription("description2");
        certificate2.setPrice(new BigDecimal("2.5"));
        certificate2.setDuration(20);
        certificate2.setCreateDate(LocalDateTime.parse("2021-02-29 11:24:04", formatter));
        certificate2.setLastUpdateDate(LocalDateTime.parse("2021-02-29 11:30:18", formatter));

        certificate3 = new GiftCertificate();
        certificate3.setId(3L);
        certificate3.setName("certificate3");
        certificate3.setDescription("description3");
        certificate3.setPrice(new BigDecimal("3.5"));
        certificate3.setDuration(30);
        certificate3.setCreateDate(LocalDateTime.parse("2021-01-29 11:24:04", formatter));
        certificate3.setLastUpdateDate(LocalDateTime.parse("2021-01-29 11:30:18", formatter));
    }


    @Test
    public void testFindBy_UsingTagNamesCriteria_ListEntities() {
        //given
        String[] params = new String[]{"tag3"};
        CriteriaFactoryResult<GiftCertificate> factoryResult = new CriteriaFactoryResult<>(tagNamesGiftCertificateCriteria, params);
        List<GiftCertificate> expected = Collections.singletonList(certificate1);
        //when
        List<GiftCertificate> result = giftCertificateDao.findBy(factoryResult);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testFindBy_UsingAllCriteria_ListEntities() {
        //given
        CriteriaFactoryResult<GiftCertificate> factoryResult = new CriteriaFactoryResult<>(allGiftCertificateCriteria, null);
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2, certificate3);
        //when
        List<GiftCertificate> result = giftCertificateDao.findBy(factoryResult);
        //then
        assertEquals(result, expected);
    }


    @Test
    public void testFindByName_EntityWithGivenNameIsPresentInDb_ReturnOptionalWithEntity() {
        //given
        String name = certificate1.getName();
        Optional<GiftCertificate> expectedResult = Optional.of(certificate1);
        //when
        Optional<GiftCertificate> result = giftCertificateDao.findByName(name);
        //then
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindByName_EntityWithGivenNameIsNotPresentInDb_ReturnEmptyOptional() {
        //given
        String name = "incorrectName";
        Optional<GiftCertificate> expectedResult = Optional.empty();
        //when
        Optional<GiftCertificate> result = giftCertificateDao.findByName(name);
        //then
        assertEquals(result, expectedResult);
    }


    @Test
    public void testFindById_EntityWithGivenIdIsPresentInDb_ReturnOptionalWithEntity() {
        //given
        Long id = certificate1.getId();
        Optional<GiftCertificate> expected = Optional.of(GiftCertificateDaoTest.certificate1);
        //when
        Optional<GiftCertificate> result = giftCertificateDao.findById(id);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testFindById_EntityWithGivenIdIsNotPresentInDb_ReturnEmptyOptional() {
        //given
        long incorrectId = 100L;
        Optional<GiftCertificate> expected = Optional.empty();
        //when
        Optional<GiftCertificate> result = giftCertificateDao.findById(incorrectId);
        //then
        assertEquals(result, expected);
    }

    @Test
    @Transactional
    @Rollback
    @Disabled
    public void testSave_ReturnEntityWithNewId() {
        //given
        GiftCertificate savedEntity = new GiftCertificate();
        savedEntity.setId(null);
        savedEntity.setName("certificate4");
        savedEntity.setDescription("description4");
        savedEntity.setPrice(new BigDecimal("4.5"));
        savedEntity.setDuration(40);
        savedEntity.setCreateDate(LocalDateTime.parse("2020-03-29 11:24:04", formatter));
        savedEntity.setLastUpdateDate(LocalDateTime.parse("2020-03-29 11:30:18", formatter));
        //when
        GiftCertificate result = giftCertificateDao.findById(giftCertificateDao.save(savedEntity)).orElse(null);
                //then
        assertNotNull(savedEntity.getId());
        assertAll(
                () -> assertEquals(result.getName(), savedEntity.getName()),
                () -> assertEquals(result.getDescription(), savedEntity.getDescription()),
                () -> assertEquals(result.getPrice(), savedEntity.getPrice()),
                () -> assertEquals(result.getDuration(), savedEntity.getDuration()),
                () -> assertEquals(result.getCreateDate(), savedEntity.getCreateDate()),
                () -> assertEquals(result.getLastUpdateDate(), savedEntity.getLastUpdateDate())
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate_EntityWithGivenIdIsPresentInDb_UpdateEntityFields() {
        //given
        Long id = certificate1.getId();
        GiftCertificate entityWithNewFields = new GiftCertificate();
        entityWithNewFields.setId(null);
        entityWithNewFields.setName("certificate4");
        entityWithNewFields.setDescription("description4");
        entityWithNewFields.setPrice(new BigDecimal("4.5"));
        entityWithNewFields.setDuration(40);
        entityWithNewFields.setCreateDate(null);
        entityWithNewFields.setLastUpdateDate(LocalDateTime.parse("2021-03-29 11:30:18", formatter));

        //when
        giftCertificateDao.update(entityWithNewFields, id);
        //then
        Optional<GiftCertificate> fromDb = giftCertificateDao.findById(id);
        GiftCertificate giftCertificateFromDb = fromDb.get();
        assertAll(
                () -> assertEquals(entityWithNewFields.getName(), giftCertificateFromDb.getName()),
                () -> assertEquals(entityWithNewFields.getDescription(), giftCertificateFromDb.getDescription()),
                () -> assertEquals(entityWithNewFields.getPrice(), giftCertificateFromDb.getPrice()),
                () -> assertEquals(entityWithNewFields.getDuration(), giftCertificateFromDb.getDuration()),
                () -> assertEquals(entityWithNewFields.getLastUpdateDate(), giftCertificateFromDb.getLastUpdateDate())
        );
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete_EntityWithGivenIdIsPresentInDb_ReturnTrue() {
        //given
        Long id = certificate1.getId();
        //when
        boolean result = giftCertificateDao.delete(id);
        //then
        assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete_EntityWithGivenIdIsNotPresentInDb_ReturnFalse() {
        //given
        long id = 100L;
        //when
        boolean result = giftCertificateDao.delete(id);
        //then
        assertFalse(result);
    }


}
