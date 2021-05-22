package com.epam.esm.domain.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.dto.TagDto;
import com.epam.esm.domain.dto.bundles.TagDtoBundle;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.domain.exceptions.TagException;
import com.epam.esm.domain.exceptions.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private UserDao userDao;

    @Mock
    private CertificateTagsDao certificateTagsDao;


    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagDto testDto;
    private Tag testEntity;
    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;

    @BeforeEach
    void init() {
        testDto = new TagDto();
        testDto.setId(100L);
        testDto.setName("dto");
        testEntity = new Tag();
        testEntity.setId(100L);
        testEntity.setName("entity");
    }

    @Test
    public void testFindById_ReturnDto_EntityWithGivenIdIsPresentInDb() {
        //given
        long id = testEntity.getId();
        when(tagDao.findById(id)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findById(id);
        //then
        verify(tagDao).findById(id);
        verify(modelMapper).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }


    @Test
    public void testFindById_ThrowException_EntityWithGivenIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDao.findById(tagId)).thenThrow(TagException.class);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.findById(tagId));
        verify(tagDao).findById(tagId);
    }


    @Test
    public void testFindAllForQuery_BundleOfDto_EntitiesArePresentInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();

        List<Tag> expectedEntitiesList = Arrays.asList(testEntity, testEntity, testEntity);
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto, testDto);

        when(tagDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(expectedEntitiesList);
        when(modelMapper.map(testEntity, TagDto.class)).thenReturn(testDto);
        long expectedSize = 3;
        when(tagDao.count()).thenReturn(expectedSize);
        TagDtoBundle expectedBundle = new TagDtoBundle(expectedResult, expectedSize);

        //when
        TagDtoBundle result = tagService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedBundle);
        verify(tagDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(modelMapper, times(expectedEntitiesList.size())).map(testEntity, TagDto.class);
        verify(tagDao).count();
    }

    @Test
    public void testFindAllForQuery_BundleWithoutDto_ThereIsNoEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();

        List<Tag> expectedEntitiesList = Collections.emptyList();
        when(tagDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(expectedEntitiesList);
        List<TagDto> expectedResult = Collections.emptyList();
        long expectedSize = 0L;
        when(tagDao.count()).thenReturn(expectedSize);
        TagDtoBundle expectedBundle = new TagDtoBundle(expectedResult, expectedSize);

        //when
        TagDtoBundle result = tagService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedBundle);
        verify(tagDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(tagDao).count();
    }

    @Test
    public void testSave_SaveEntityAndReturnDto_GivenEntityIsNotPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDao.findByName(tagName)).thenReturn(Optional.empty());
        when(modelMapper.map(testDto, Tag.class)).thenReturn(testEntity);
        when(tagDao.save(testEntity)).thenReturn(testEntity);
        //when
        TagDto result = tagService.save(testDto);
        //then
        verify(tagDao).findByName(tagName);
        verify(modelMapper).map(testDto, Tag.class);
        verify(tagDao).save(testEntity);
        assertEquals(result, testDto);
    }

    @Test
    public void testSave_ThrowException_GivenEntityIsPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDao.findByName(tagName)).thenThrow(TagException.class);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.save(testDto));
        verify(tagDao).findByName(tagName);
    }

    @Test
    public void testDelete_DeleteEntity_EntityWithSuchIdIsPresentInDb() {
        //given
        long tagId = testDto.getId();
        doNothing().when(certificateTagsDao).deleteAllCertificateLinksForTagId(tagId);
        when(tagDao.deleteById(tagId)).thenReturn(true);
        //when
        tagService.delete(tagId);
        //then
        verify(tagDao).deleteById(tagId);
    }

    @Test
    public void testDelete_ThrowException_EntityWithSuchIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        doNothing().when(certificateTagsDao).deleteAllCertificateLinksForTagId(tagId);
        when(tagDao.deleteById(tagId)).thenReturn(false);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.delete(tagId));
        verify(tagDao).deleteById(tagId);
    }

    @Test
    public void testFindByName_ReturnDto_EntityWithSuchNameIsPresentInDb() {
        //given
        String name = testDto.getName();
        when(tagDao.findByName(name)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findByName(name);
        //then
        verify(tagDao).findByName(name);
        verify(modelMapper).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }

    @Test
    public void testFindByName_ThrowException_EntityWithSuchNameIsNotPresentInDb() {
        //given
        String name = testDto.getName();
        when(tagDao.findByName(name)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagException.class, () -> tagService.findByName(name));
        verify(tagDao).findByName(name);
    }

    @Test
    public void testFindMostWidelyUser_ReturnListOfDto_UserWithGivenIdExists() {
        //given
        long userId = 1;
        when(userDao.findById(userId)).thenReturn(Optional.of(new User()));
        when(tagDao.findMaxWidelyUsed(userId)).thenReturn(Arrays.asList(testEntity, testEntity));
        when(modelMapper.map(testEntity, TagDto.class)).thenReturn(testDto);
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto);
        //when
        List<TagDto> result = tagService.findMostWidelyUsed(userId);
        //then
        verify(userDao).findById(userId);
        verify(tagDao).findMaxWidelyUsed(userId);
        verify(modelMapper, times(2)).map(testEntity, TagDto.class);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindMostWidelyUser_ThrowException_UserWithGivenIdDoesNotExistInDb() {
        //given
        long userId = -1;
        when(userDao.findById(userId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(UserException.class, () -> tagService.findMostWidelyUsed(userId));
        verify(userDao).findById(userId);
    }


}
