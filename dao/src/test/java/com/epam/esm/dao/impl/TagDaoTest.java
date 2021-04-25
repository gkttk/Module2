package com.epam.esm.dao.impl;

import com.epam.esm.ApplicationRunner;
import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ApplicationRunner.class)
@ActiveProfiles("test")
@Transactional
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    private static Tag tag1;

    @BeforeAll
    static void init() {
        tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");
    }

    @Test
    public void testFindById_OptionalWithEntity_EntityWithGivenIdIsPresentInDb() {
        //given
        long testId = tag1.getId();
        Optional<Tag> expected = Optional.of(tag1);
        //when
        Optional<Tag> result = tagDao.findById(testId);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testFindById_EmptyOptional_EntityWithGivenIdIsMotPresentInDb() {
        //given
        long testId = -1L;
        //when
        Optional<Tag> result = tagDao.findById(testId);
        //then
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByName_OptionalWithEntity_EntityWithGivenNameIsPresentInDb() {
        //given
        String testName = tag1.getName();
        Optional<Tag> expected = Optional.of(tag1);
        //when
        Optional<Tag> result = tagDao.findByName(testName);
        //then
        assertEquals(result, expected);
    }

    @Test
    public void testFindByName_EmptyOptional_EntityWithGivenNameIsNotPresentInDb() {
        //given
        String testName = "testName";
        Optional<Tag> expected = Optional.empty();
        //when
        Optional<Tag> result = tagDao.findByName(testName);
        //then
        assertFalse(result.isPresent());
    }

    @Test
    @Rollback
    public void testSave_Entity() {
        //given
        Tag newTag = new Tag();
        newTag.setName("newTagName");
        //when
        Tag result = tagDao.save(newTag);
        //then
        assertAll(() -> assertNotNull(result.getId()),
                () -> assertEquals(result.getName(), newTag.getName()));
    }

    @Test
    @Rollback
    public void testDeleteById_True_WhenEntityWasDeleted() {
        //given
        long testId = tag1.getId();
        //when
        boolean result = tagDao.deleteById(testId);
        //then
        assertTrue(result);
    }

    @Test
    @Rollback
    public void testDeleteById_False_WhenEntityWasNotDeleted() {
        //given
        long testId = -1L;
        //when
        boolean result = tagDao.deleteById(testId);
        //then
        assertFalse(result);
    }

    @Test
    public void testFindBy_ListOfEntitiesWithGivenCertificateIds_WhenEntityWithGivenCertificateIdAreExistInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        reqParams.put(ApplicationConstants.CERTIFICATE_ID_KEY, new String[]{"1"});
        int expectedSize = 2;
        //when
        List<Tag> results = tagDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListWithAllEntities_WhenEntityArePresentInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        int expectedSize = 6;
        //when
        List<Tag> results = tagDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

}
