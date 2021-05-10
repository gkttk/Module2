package com.epam.esm.controller;

import com.epam.esm.assemblers.TagModelAssembler;
import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @Mock
    private TagModelAssembler assemblerMock;

    @Mock
    private TagService serviceMock;

    @Mock
    private WebRequest webRequestMock;

    @InjectMocks
    private TagController tagController;
    private static TagDto defaultTagDto;
    private static TagDto createdTag;

    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;

    @BeforeAll
    static void init() {
        defaultTagDto = new TagDto(100L, "testTag");
        createdTag = new TagDto(null, "testTag");
    }

    @Test
    public void testGetByName_ReturnHttpStatusOkWithDto() {
        //given
        String tagName = defaultTagDto.getName();
        when(serviceMock.findByName(tagName)).thenReturn(defaultTagDto);
        when(assemblerMock.toModel(defaultTagDto)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.getByName(tagName);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findByName(tagName);
        verify(assemblerMock).toModel(defaultTagDto);
    }

    @Test
    public void testGetAllForQuery_ReturnHttpStatusOkWithDtoList() {
        //given
        Map<String, String[]> paramMap = new HashMap<>();
        List<TagDto> listDto = Arrays.asList(defaultTagDto, defaultTagDto);
        when(webRequestMock.getParameterMap()).thenReturn(paramMap);
        when(serviceMock.findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET)).thenReturn(listDto);
        when(assemblerMock.toCollectionModel(listDto, TEST_OFFSET,paramMap)).thenReturn(CollectionModel.of(listDto));
        ResponseEntity<CollectionModel<TagDto>> expectedResult = ResponseEntity.ok(CollectionModel.of(listDto));

        //when
        ResponseEntity<CollectionModel<TagDto>> result = tagController.getAllForQuery(webRequestMock, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(webRequestMock).getParameterMap();
        verify(serviceMock).findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET);
        verify(assemblerMock).toCollectionModel(listDto, TEST_OFFSET,paramMap);
    }

    @Test
    public void testGetById_ReturnHttpStatusOkWithDto() {
        //given
        Long tagId = defaultTagDto.getId();
        when(serviceMock.findById(tagId)).thenReturn(defaultTagDto);
        when(assemblerMock.toModel(defaultTagDto)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.getById(tagId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findById(tagId);
        verify(assemblerMock).toModel(defaultTagDto);
    }

    @Test
    public void testDeleteById_ReturnHttpStatusNoContent() {
        //given
        Long tagId = defaultTagDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.noContent().build();
        //when
        ResponseEntity<Void> result = tagController.deleteById(tagId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).delete(tagId);
    }

    @Test
    public void testCreateTag_ThereAreNoValidationErrors_ReturnHttpStatusOkWithDto() {
        //given
        when(serviceMock.save(createdTag)).thenReturn(defaultTagDto);
        when(assemblerMock.toModel(defaultTagDto)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.createTag(createdTag);
        //then
        assertEquals(result, expected);
        verify(serviceMock).save(createdTag);
    }

    @Test
    public void testGetMostWidelyUsedTagsOfUser_ReturnHttpStatusOkWithDto() {
        //given
        long userId = 1;
        List<TagDto> listTags = Collections.singletonList(defaultTagDto);
        when(serviceMock.findMostWidelyUsed(userId)).thenReturn(listTags);
        when(assemblerMock.toModel(defaultTagDto)).thenReturn(defaultTagDto);
        ResponseEntity<List<TagDto>> expectedResult = ResponseEntity.ok(listTags);
        //when
        ResponseEntity<List<TagDto>> result = tagController.getMostWidelyUsedTagOfUser(userId);
        //then
        assertEquals(result, expectedResult);
        verify(serviceMock).findMostWidelyUsed(userId);
        verify(assemblerMock).toModel(defaultTagDto);
    }


}
