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
        certificate1 = new GiftCertificate(1L, "certificate1", "descripiton1",
                new BigDecimal("1.5"), 10, "2021-03-29T11:24:04.643", "2021-03-29T11:30:18.653");
        certificate2 = new GiftCertificate(2L, "certificate2", "descripiton2",
                new BigDecimal("2.5"), 20, "2021-02-29T11:24:04.643", "2021-02-29T11:30:18.653");
        certificate3 = new GiftCertificate(3L, "certificate3", "descripiton3",
                new BigDecimal("3.5"), 30, "2021-01-29T11:24:04.643", "2021-01-29T11:30:18.653");
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
    public void testFindAllByTagNameShouldReturnEntityListWhenEntitiesWithGivenTagNameArePresentInDb() {
        //given
        String tagName = "tag2";
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2);
        //when
        List<GiftCertificate> result = giftCertificateDao.findAllByTagName(tagName);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testFindAllByTagNameShouldReturnEmptyListWhenEntitiesWithGivenTagNameAreNotPresentInDb() {
        //given
        String tagName = "incorrectTagName";
        List<GiftCertificate> expected = Collections.emptyList();
        //when
        List<GiftCertificate> result = giftCertificateDao.findAllByTagName(tagName);
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
    public void testFindAllShouldReturnEntityListWhenEntityArePresentInDb() {
        //given
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2, certificate3);
        //when
        List<GiftCertificate> result = giftCertificateDao.findAll();
        //then
        assertEquals(result, expected);
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveShouldReturnEntityWithNewId() {
        //given
        GiftCertificate savedEntity = new GiftCertificate(null, "certificate4", "descripiton4",
                new BigDecimal("4.5"), 40, "2020-03-29T11:24:04.643", "2020-03-29T11:30:18.653");
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
        GiftCertificate entityWithNewFields = new GiftCertificate(null, "certificate4", "descripiton4",
                new BigDecimal("4.5"), 40, null, "2020-03-29T11:30:18.653");
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
