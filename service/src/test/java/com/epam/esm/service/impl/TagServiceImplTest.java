package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.EntityNotFoundException;
import com.epam.esm.exceptions.EntityWithSuchNameAlreadyExists;
import org.junit.jupiter.api.BeforeAll;
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

    @InjectMocks
    private TagServiceImpl tagService;

    private static TagDto testDto;
    private static Tag testEntity;

    @BeforeAll
    static void init() {
        testDto = new TagDto(100L, "test");
        testEntity = new Tag(100L, "test");
    }

    @Test
    public void testGetByIdShouldReturnDtoWhenEntityWithGivenIdIsPresentInDb() {
        //given
        when(tagDaoMock.getById(anyLong())).thenReturn(Optional.of(new Tag()));
        when(modelMapperMock.map(any(), any())).thenReturn(testDto);
        //when
        TagDto result = tagService.getById(100);
        //then
        verify(tagDaoMock).getById(anyLong());
        verify(modelMapperMock).map(any(), any());
        assertEquals(result, testDto);
    }

    @Test
    public void testGetByIdShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.getById(tagId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> tagService.getById(tagId));
        verify(tagDaoMock).getById(tagId);
    }

    @Test
    public void testFindAllShouldReturnListOfDtoWhenEntitiesArePresentInDb() {
        //given
        List<Tag> expectedEntitiesList = Stream.generate(Tag::new).limit(3).collect(Collectors.toList());
        List<TagDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        when(tagDaoMock.findAll()).thenReturn(expectedEntitiesList);
        when(modelMapperMock.map(any(), any())).thenReturn(testDto);
        //when
        List<TagDto> result = tagService.findAll();
        //then
        verify(tagDaoMock).findAll();
        verify(modelMapperMock, times(expectedEntitiesList.size())).map(any(), any());
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindAllShouldThrowExceptionWhenThereIsNoEntitiesInDb() {
        //given
        List<Tag> expectedEntitiesList = Collections.emptyList();
        when(tagDaoMock.findAll()).thenReturn(expectedEntitiesList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> tagService.findAll());
        verify(tagDaoMock).findAll();
    }

    @Test
    public void testSaveShouldSaveEntityAndReturnDtoWhenGivenEntityIsNotPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.findByName(tagName)).thenReturn(Optional.empty());
        when(modelMapperMock.map(testDto, Tag.class)).thenReturn(testEntity);
        when(tagDaoMock.save(testEntity)).thenReturn(testEntity);
        //when
        TagDto result = tagService.save(testDto);
        //then
        verify(tagDaoMock).findByName(tagName);
        verify(modelMapperMock).map(testDto, Tag.class);
        verify(tagDaoMock).save(testEntity);
        assertEquals(result, testDto);
    }

    @Test
    public void testSaveShouldThrowExceptionWhenGivenEntityIsPresentInDb() {
        //given
        String tagName = testDto.getName();
        when(tagDaoMock.findByName(tagName)).thenReturn(Optional.of(testEntity));
        //when
        //then
        assertThrows(EntityWithSuchNameAlreadyExists.class, () -> tagService.save(testDto));
        verify(tagDaoMock).findByName(tagName);
    }

    @Test
    public void testDeleteShouldDeleteEntityIfEntityWithSuchIdIsPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(true);
        //when
        tagService.delete(tagId);
        //then
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testDeleteShouldThrowExceptionIfEntityWithSuchIdIsNotPresentInDb() {
        //given
        long tagId = testDto.getId();
        when(tagDaoMock.delete(tagId)).thenReturn(false);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> tagService.delete(tagId));
        verify(tagDaoMock).delete(tagId);
    }

    @Test
    public void testFindByNameShouldReturnDtoWhenEntityWithSuchNameIsPresentInDb() {
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
    public void testFindByNameShouldThrowExceptionWhenEntityWithSuchNameIsNotPresentInDb() {
        //given
        String name = testDto.getName();
        when(tagDaoMock.findByName(name)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> tagService.findByName(name));
        verify(tagDaoMock).findByName(name);
    }


}
