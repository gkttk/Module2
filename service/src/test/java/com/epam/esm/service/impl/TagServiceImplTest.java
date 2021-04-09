package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.validator.TagValidator;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDaoMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private TagValidator validator;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagDto testDto;
    private Tag testEntity;

    @BeforeEach
    void init() {
        testDto = new TagDto(100L, "test");
        testEntity = new Tag(100L, "test");
    }

    @Test
    public void testFindById_EntityWithGivenIdIsPresentInDb_ReturnDto() {
        //given
        long id = testEntity.getId();
        when(validator.validateAndFindByIdIfExist(id)).thenReturn(testEntity);
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findById(id);
        //then
        verify(validator).validateAndFindByIdIfExist(id);
        verify(modelMapperMock).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }

    @Test
    public void testFindById_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        //given
        long tagId = testDto.getId();
        when(validator.validateAndFindByIdIfExist(tagId)).thenThrow(TagException.class);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.findById(tagId));
        verify(validator).validateAndFindByIdIfExist(tagId);
    }

    @Test
    public void testFindAll_EntitiesArePresentInDb_ReturnListOfDto() {
        //given

        List<Tag> expectedEntitiesList = Arrays.asList(testEntity, testEntity, testEntity);
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto, testDto);

        when(tagDaoMock.findBy(any())).thenReturn(expectedEntitiesList);
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        List<TagDto> result = tagService.findAllForQuery(anyMap());
        //then
        verify(tagDaoMock).findBy(anyMap());
        verify(modelMapperMock, times(expectedEntitiesList.size())).map(any(), any());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindAll_ThereIsNoEntitiesInDb_ReturnEmptyList() {
        //given
        List<Tag> expectedEntitiesList = Collections.emptyList();
        when(tagDaoMock.findBy(any())).thenReturn(expectedEntitiesList);
        List<TagDto> expectedResult = Collections.emptyList();
        //when
        List<TagDto> result = tagService.findAllForQuery(anyMap());
        //then
        assertEquals(result, expectedResult);
        verify(tagDaoMock).findBy(any());
    }

    @Test
    public void testSave_GivenEntityIsNotPresentInDb_SaveEntityAndReturnDto() {
        //given
        String tagName = testDto.getName();
        doNothing().when(validator).validateIfEntityWithGivenNameExist(tagName);
        when(modelMapperMock.map(testDto, Tag.class)).thenReturn(testEntity);
        when(tagDaoMock.save(testEntity)).thenReturn(testEntity);
        //when
        TagDto result = tagService.save(testDto);
        //then
        verify(validator).validateIfEntityWithGivenNameExist(tagName);
        verify(modelMapperMock).map(testDto, Tag.class);
        verify(tagDaoMock).save(testEntity);
        assertEquals(result, testDto);
    }

    @Test
    public void testSave_GivenEntityIsPresentInDb_ThrowException() {
        //given
        String tagName = testDto.getName();
        doThrow(TagException.class).when(validator).validateIfEntityWithGivenNameExist(tagName);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.save(testDto));
        verify(validator).validateIfEntityWithGivenNameExist(tagName);
    }

    @Test
    public void testDelete_EntityWithSuchIdIsPresentInDb_DeleteEntity() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(true);
        //when
        tagService.delete(tagId);
        //then
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testDelete_EntityWithSuchIdIsNotPresentInDb_ThrowException() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(false);
        //when
        //then
        assertThrows(TagException.class, () -> tagService.delete(tagId));
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testFindByName_EntityWithSuchNameIsPresentInDb_ReturnDto() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.findByName(name)).thenReturn(Optional.of(testEntity));
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findByName(name);
        //then
        verify(tagDaoMock).findByName(name);
        verify(modelMapperMock).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }

    @Test
    public void testFindByName_EntityWithSuchNameIsNotPresentInDb_ThrowException() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.findByName(name)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagException.class, () -> tagService.findByName(name));
        verify(tagDaoMock).findByName(name);
    }


}
