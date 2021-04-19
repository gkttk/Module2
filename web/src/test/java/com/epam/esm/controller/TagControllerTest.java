package com.epam.esm.controller;

import com.epam.esm.config.ServiceConfig;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ServiceConfig.class)
public class TagControllerTest {

    @Mock
    private TagService serviceMock;

    @Mock
    private WebRequest webRequestMock;

    @InjectMocks
    private TagController tagController;
    private static TagDto defaultTagDto;
    private static TagDto createdTag;

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
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.getByName(tagName);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findByName(tagName);
    }

    @Test
    public void testGetAllName_ReturnHttpStatusOkWithDtoList() {
        //given
        List<TagDto> listDto = Arrays.asList(defaultTagDto, defaultTagDto);
        when(webRequestMock.getParameterMap()).thenReturn(new HashMap<>());
        when(serviceMock.findAllForQuery(anyMap(), any(), any())).thenReturn(listDto);
        ResponseEntity<List<TagDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<TagDto>> result = tagController.getAll(webRequestMock, 1, 2);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findAllForQuery(anyMap(), any(), any());
    }

    @Test
    public void testGetById_ReturnHttpStatusOkWithDto() {
        //given
        Long tagId = defaultTagDto.getId();
        when(serviceMock.findById(tagId)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.getById(tagId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findById(tagId);
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
    @Disabled
    public void testCreateTag_ThereAreNoValidationErrors_ReturnHttpStatusCreatedWithDto() {
        //given
        when(serviceMock.save(createdTag)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = new ResponseEntity<>(defaultTagDto, HttpStatus.CREATED);
        //when
        ResponseEntity<TagDto> result = tagController.createTag(createdTag);
        //then
        assertEquals(result, expected);
        verify(serviceMock).save(createdTag);
    }


}
