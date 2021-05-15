package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    private static GiftCertificate certificate1;

    @BeforeAll
    static void init() {
        certificate1 = new GiftCertificate();
        certificate1.setId(1L);
        certificate1.setName("1name");
        certificate1.setDescription("1description20Symbols");
        certificate1.setPrice(new BigDecimal("100.00"));
        certificate1.setDuration(10);
    }

    @Test
    public void testFindById_EntityWithGivenId_WhenEntityWithGivenIdIsPresentInDb() {
        //given
        long testId = certificate1.getId();
        //when
        Optional<GiftCertificate> resultOpt = giftCertificateDao.findById(testId);
        //then
        assertTrue(resultOpt.isPresent());
        resultOpt.ifPresent(result -> {
            assertAll(() -> assertEquals(result.getName(), certificate1.getName()),
                    () -> assertEquals(result.getDescription(), certificate1.getDescription()),
                    () -> assertEquals(result.getPrice(), certificate1.getPrice()),
                    () -> assertEquals(result.getDuration(), certificate1.getDuration()));
        });
    }

    @Test
    public void testFindById_EmptyOptional_WhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long testId = -1L;
        //when
        Optional<GiftCertificate> resultOpt = giftCertificateDao.findById(testId);
        //then
        assertFalse(resultOpt.isPresent());
    }

    @Test
    public void testFindByName_EntityWithGivenName_WhenEntityWithGivenNameIsPresentInDb() {
        //given
        String testName = certificate1.getName();
        //when
        Optional<GiftCertificate> resultOpt = giftCertificateDao.findByName(testName);
        //then
        assertTrue(resultOpt.isPresent());
        resultOpt.ifPresent(result -> {
            assertAll(() -> assertEquals(result.getName(), certificate1.getName()),
                    () -> assertEquals(result.getDescription(), certificate1.getDescription()),
                    () -> assertEquals(result.getPrice(), certificate1.getPrice()),
                    () -> assertEquals(result.getDuration(), certificate1.getDuration()));
        });
    }

    @Test
    public void testFindByName_EmptyOptional_WhenEntityWithGivenNameIsNotPresentInDb() {
        //given
        String testName = "testName";
        //when
        Optional<GiftCertificate> resultOpt = giftCertificateDao.findByName(testName);
        //then
        assertFalse(resultOpt.isPresent());
    }

    @Test
    @Rollback
    public void testSave_Entity() {
        //given
        GiftCertificate testCertificate = new GiftCertificate();
        testCertificate.setName("testName");
        testCertificate.setDescription("testDescription");
        testCertificate.setPrice(new BigDecimal("130"));
        testCertificate.setDuration(20);
        //when
        GiftCertificate resultCertificate = giftCertificateDao.save(testCertificate);
        //then
        assertAll(() -> assertNotNull(resultCertificate.getId()),
                () -> assertEquals(resultCertificate.getName(), testCertificate.getName()),
                () -> assertEquals(resultCertificate.getDescription(), testCertificate.getDescription()),
                () -> assertEquals(resultCertificate.getPrice(), testCertificate.getPrice()),
                () -> assertEquals(resultCertificate.getDuration(), testCertificate.getDuration()));
    }

    @Test
    @Rollback
    public void testUpdate_Entity() {
        //given
        GiftCertificate testCertificate = new GiftCertificate();
        testCertificate.setId(certificate1.getId());
        testCertificate.setName("testName");
        testCertificate.setDescription("testDescription");
        testCertificate.setPrice(new BigDecimal("130"));
        testCertificate.setDuration(20);
        //when
        GiftCertificate resultCertificate = giftCertificateDao.update(testCertificate);
        //then
        assertAll(() -> assertEquals(resultCertificate.getId(), certificate1.getId()),
                () -> assertEquals(resultCertificate.getName(), testCertificate.getName()),
                () -> assertEquals(resultCertificate.getDescription(), testCertificate.getDescription()),
                () -> assertEquals(resultCertificate.getPrice(), testCertificate.getPrice()),
                () -> assertEquals(resultCertificate.getDuration(), testCertificate.getDuration()));
    }

    @Test
    public void testFindBy_ListOfAllEntities_ThereAreEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        int expectedSize = 6;
        //when
        List<GiftCertificate> results = giftCertificateDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListOfEntitiesWithGivenPartOfNames_ThereAreEntitiesWithGivenPartOfNamesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.singletonMap(ApplicationConstants.NAMES_PART_KEY, new String[]{"1n", "2n"});
        int expectedSize = 2;
        //when
        List<GiftCertificate> results = giftCertificateDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListOfEntitiesWithGivenPartOfDescription_ThereAreEntitiesWithGivenPartOfDescriptionInDb() {
        //given
        Map<String, String[]> reqParams = Collections.singletonMap(ApplicationConstants.DESCRIPTION_PART_KEY, new String[]{"1d", "2d"});
        int expectedSize = 2;
        //when
        List<GiftCertificate> results = giftCertificateDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListOfEntitiesWithGivenTagNames_ThereAreEntitiesWithGivenTagNamesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.singletonMap(ApplicationConstants.TAG_NAMES_KEY, new String[]{"tag2"});
        int expectedSize = 2;
        //when
        List<GiftCertificate> results = giftCertificateDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListOfEntitiesWithGivenTagNamesAndQuery_ThereAreEntitiesWithGivenTagNamesAndQueryInDb() {
        //given
        Map<String, String[]> reqParams = Collections.singletonMap(ApplicationConstants.TAG_NAMES_KEY, new String[]{"or:tag1,tag2"});
        int expectedSize = 4;
        //when
        List<GiftCertificate> results = giftCertificateDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

}
