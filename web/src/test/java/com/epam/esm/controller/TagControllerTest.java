package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private TagService serviceMock;

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
    public void testGetByNameShouldReturnHttpStatusOkWithDto() {
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
    public void testGetAllNameShouldReturnHttpStatusOkWithDtoList() {
        //given
        List<TagDto> listDto = Arrays.asList(defaultTagDto, defaultTagDto);
        when(serviceMock.findAll()).thenReturn(listDto);
        ResponseEntity<List<TagDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<TagDto>> result = tagController.getAll();
        //then
        assertEquals(result, expected);
        verify(serviceMock).findAll();
    }

    @Test
    public void testGetByIdShouldReturnHttpStatusOkWithDto() {
        //given
        Long tagId = defaultTagDto.getId();
        when(serviceMock.getById(tagId)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = ResponseEntity.ok(defaultTagDto);
        //when
        ResponseEntity<TagDto> result = tagController.getById(tagId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getById(tagId);
    }

    @Test
    public void testDeleteByIdShouldReturnHttpStatusNoContent() {
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
    public void testCreateTagShouldReturnHttpStatusCreatedWithDtoWhenThereAreNoErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(serviceMock.save(createdTag)).thenReturn(defaultTagDto);
        ResponseEntity<TagDto> expected = new ResponseEntity<>(defaultTagDto, HttpStatus.CREATED);
        //when
        ResponseEntity<TagDto> result = tagController.createTag(createdTag, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
        verify(serviceMock).save(createdTag);
    }

    @Test
    public void testCreateTagShouldReturnHttpStatusBadRequestWhenThereAreErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<TagDto> expected = ResponseEntity.badRequest().build();
        //when
        ResponseEntity<TagDto> result = tagController.createTag(createdTag, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
    }


}
