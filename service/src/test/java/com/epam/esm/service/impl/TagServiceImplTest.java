package com.epam.esm.service.impl;

import com.epam.esm.criteria.factory.TagCriteriaFactory;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.criteria.tags.AllTagCriteria;
import com.epam.esm.criteria.tags.CertificateIdTagCriteria;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.TagWithSuchNameAlreadyExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDaoMock;
    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private TagCriteriaFactory tagCriteriaFactory;

    @Mock
    private AllTagCriteria allTagCriteria;

    @Mock
    private CertificateIdTagCriteria certificateIdTagCriteria;

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
        when(tagDaoMock.getById(anyLong())).thenReturn(Optional.of(new Tag()));
        when(modelMapperMock.map(any(), any())).thenReturn(testDto);
        //when
        TagDto result = tagService.findById(100);
        //then
        verify(tagDaoMock).getById(anyLong());
        verify(modelMapperMock).map(any(), any());
        assertEquals(result, testDto);
    }

    @Test
    public void testFindById_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.getById(tagId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tagId));
        verify(tagDaoMock).getById(tagId);
    }

    @Test
    public void testFindAll_EntitiesArePresentInDb_ReturnListOfDto() {
        //given

        CriteriaFactoryResult<Tag> criteriaFactoryResult = new CriteriaFactoryResult<>(allTagCriteria,null);

        List<Tag> expectedEntitiesList = Arrays.asList(testEntity, testEntity, testEntity);
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto, testDto);

        when(tagCriteriaFactory.getCriteriaWithParams(anyMap())).thenReturn(criteriaFactoryResult);
        when(tagDaoMock.getBy(any())).thenReturn(expectedEntitiesList);
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        List<TagDto> result = tagService.findAllForQuery(anyMap());
        //then
        verify(tagDaoMock).getBy(criteriaFactoryResult);
        verify(modelMapperMock, times(expectedEntitiesList.size())).map(any(), any());
        assertEquals(result, expectedResult);
    }

   @Test
    public void testFindAll_ThereIsNoEntitiesInDb_ThrowException() {
        //given
        List<Tag> expectedEntitiesList = Collections.emptyList();
        when(tagDaoMock.getBy(any())).thenReturn(expectedEntitiesList);
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findAllForQuery(anyMap()));
        verify(tagDaoMock).getBy(any());
    }

    @Test
    public void testSave_GivenEntityIsNotPresentInDb_SaveEntityAndReturnDto() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.getByName(tagName)).thenReturn(Optional.empty());
        when(modelMapperMock.map(testDto, Tag.class)).thenReturn(testEntity);
        when(tagDaoMock.save(testEntity)).thenReturn(testEntity);
        //when
        TagDto result = tagService.save(testDto);
        //then
        verify(tagDaoMock).getByName(tagName);
        verify(modelMapperMock).map(testDto, Tag.class);
        verify(tagDaoMock).save(testEntity);
        assertEquals(result, testDto);
    }

    @Test
    public void testSave_GivenEntityIsPresentInDb_ThrowException() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.getByName(tagName)).thenReturn(Optional.of(testEntity));
        //when
        //then
        assertThrows(TagWithSuchNameAlreadyExists.class, () -> tagService.save(testDto));
        verify(tagDaoMock).getByName(tagName);
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
        assertThrows(TagNotFoundException.class, () -> tagService.delete(tagId));
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testFindByName_EntityWithSuchNameIsPresentInDb_ReturnDto() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.getByName(name)).thenReturn(Optional.of(testEntity));
        when(modelMapperMock.map(testEntity, TagDto.class)).thenReturn(testDto);
        //when
        TagDto result = tagService.findByName(name);
        //then
        verify(tagDaoMock).getByName(name);
        verify(modelMapperMock).map(testEntity, TagDto.class);
        assertEquals(result, testDto);
    }

    @Test
    public void testFindByName_EntityWithSuchNameIsNotPresentInDb_ThrowException() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.getByName(name)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(TagNotFoundException.class, () -> tagService.findByName(name));
        verify(tagDaoMock).getByName(name);
    }


}
