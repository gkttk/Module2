package com.epam.esm.dao.impl;

import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        certificate1.setCreateDate("2021-03-29T11:24:04.643");
        certificate1.setLastUpdateDate("2021-03-29T11:30:18.653");

        certificate2 = new GiftCertificate();
        certificate2.setId(2L);
        certificate2.setName("certificate2");
        certificate2.setDescription("description2");
        certificate2.setPrice(new BigDecimal("2.5"));
        certificate2.setDuration(20);
        certificate2.setCreateDate("2021-02-29T11:24:04.643");
        certificate2.setLastUpdateDate("2021-02-29T11:30:18.653");

        certificate3 = new GiftCertificate();
        certificate3.setId(3L);
        certificate3.setName("certificate3");
        certificate3.setDescription("description3");
        certificate3.setPrice(new BigDecimal("3.5"));
        certificate3.setDuration(30);
        certificate3.setCreateDate("2021-01-29T11:24:04.643");
        certificate3.setLastUpdateDate("2021-01-29T11:30:18.653");
    }

    @Test
    public void testGetAllSortedShouldReturnEntitySortedListWhenEntitiesArePresentInDb() {
        //given
        String[] sortingFieldNames = new String[]{"id"};
        String sortingOrder = "DESC";
        List<GiftCertificate> expected = Arrays.asList(certificate3, certificate2, certificate1);
        //when
        List<GiftCertificate> result = giftCertificateDao.getAllSorted(sortingFieldNames, sortingOrder);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetAllByTagNameShouldReturnEntityListWhenEntitiesWithGivenTagNameArePresentInDb() {
        //given
        String tagName = "tag2";
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2);
        //when
        List<GiftCertificate> result = giftCertificateDao.getAllByTagName(tagName);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetAllByTagNameShouldReturnEmptyListWhenEntitiesWithGivenTagNameAreNotPresentInDb() {
        //given
        String tagName = "incorrectTagName";
        List<GiftCertificate> expected = Collections.emptyList();
        //when
        List<GiftCertificate> result = giftCertificateDao.getAllByTagName(tagName);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetByIdShouldReturnOptionalWithEntityWhenEntityWithGivenIdIsPresentInDb() {
        //given
        Long id = certificate1.getId();
        Optional<GiftCertificate> expected = Optional.of(GiftCertificateDaoTest.certificate1);
        //when
        Optional<GiftCertificate> result = giftCertificateDao.getById(id);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetByIdShouldReturnEmptyOptionalWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long incorrectId = 100L;
        Optional<GiftCertificate> expected = Optional.empty();
        //when
        Optional<GiftCertificate> result = giftCertificateDao.getById(incorrectId);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testGetAllShouldReturnEntityListWhenEntityArePresentInDb() {
        //given
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2, certificate3);
        //when
        List<GiftCertificate> result = giftCertificateDao.getAll();
        //then
        assertEquals(result, expected);
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveShouldReturnEntityWithNewId() {
        //given
        GiftCertificate savedEntity = new GiftCertificate();
        savedEntity.setId(null);
        savedEntity.setName("certificate4");
        savedEntity.setDescription("description4");
        savedEntity.setPrice(new BigDecimal("4.5"));
        savedEntity.setDuration(40);
        savedEntity.setCreateDate("2020-03-29T11:24:04.643");
        savedEntity.setLastUpdateDate("2020-03-29T11:30:18.653");

        //when
        GiftCertificate result = giftCertificateDao.save(savedEntity);
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
    public void testUpdateShouldUpdateEntityFieldsWhenEntityWithGivenIdIsPresentInDb() {
        //given
        Long id = certificate1.getId();
        GiftCertificate entityWithNewFields = new GiftCertificate();
        entityWithNewFields.setId(null);
        entityWithNewFields.setName("certificate4");
        entityWithNewFields.setDescription("description4");
        entityWithNewFields.setPrice(new BigDecimal("4.5"));
        entityWithNewFields.setDuration(40);
        entityWithNewFields.setCreateDate(null);
        entityWithNewFields.setLastUpdateDate("2020-03-29T11:30:18.653");


        //when
        giftCertificateDao.update(entityWithNewFields, id);
        //then
        Optional<GiftCertificate> fromDb = giftCertificateDao.getById(id);
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
    public void testDeleteShouldReturnTrueWhenEntityWithGivenIdIsPresentInDb() {
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
    public void testDeleteShouldReturnFalseWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long id = 100L;
        //when
        boolean result = giftCertificateDao.delete(id);
        //then
        assertFalse(result);
    }


}
