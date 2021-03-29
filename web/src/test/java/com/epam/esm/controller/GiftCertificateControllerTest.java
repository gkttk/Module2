package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateControllerTest {


    @Mock
    private BindingResult bindingResult;

    @Mock
    private GiftCertificateService serviceMock;

    @InjectMocks
    private GiftCertificateController giftCertificateController;

    private static GiftCertificateDto defaultCertDto;
    private static TagDto defaultTagDto;
    private static GiftCertificateDto updatedDto;
    private static GiftCertificatePatchDto patchedDto;

    @BeforeAll
    static void init() {
        defaultCertDto = new GiftCertificateDto(100L, "testCertificate", "description",
                new BigDecimal("1.5"), 10, new Date(), new Date(),
                Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));
        defaultTagDto = new TagDto(100L, "testTag");

        updatedDto = new GiftCertificateDto(null, "testCertificate", "description",
                new BigDecimal("1.5"), 10, new Date(), new Date(),
                Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

        patchedDto = new GiftCertificatePatchDto("testCertificate", "description",
                new BigDecimal("1.5"), 10, Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));
    }


    @Test
    public void testGetAllByPartOfDescriptionShouldReturnHttpStatusOk() {
        //given
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        String partOfDescription = "desc";
        when(serviceMock.getAllByPartOfDescription(partOfDescription)).thenReturn(listDto);

        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<GiftCertificateDto>> result =
                giftCertificateController.getAllByPartOfDescription(partOfDescription);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getAllByPartOfDescription(partOfDescription);
    }

    @Test
    public void testGetAllByPartOfNameShouldReturnHttpStatusOkWithDtoList() {
        //given
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        String partOfName = "test";
        when(serviceMock.getAllByPartOfName(partOfName)).thenReturn(listDto);

        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<GiftCertificateDto>> result =
                giftCertificateController.getAllByPartOfName(partOfName);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getAllByPartOfName(partOfName);
    }

    @Test
    public void testGetAllSortedShouldReturnHttpStatusOkWithDtoList() {
        //given
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        List<String> sortFields = Arrays.asList("id", "name");
        String sortOrder = "DESC";
        when(serviceMock.getAllSorted(sortFields, sortOrder)).thenReturn(listDto);

        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<GiftCertificateDto>> result =
                giftCertificateController.getAllSorted(sortFields, sortOrder);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getAllSorted(sortFields, sortOrder);
    }

    @Test
    public void testGetAllShouldReturnHttpStatusOkWithDtoList() {
        //given
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        when(serviceMock.getAll()).thenReturn(listDto);

        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<GiftCertificateDto>> result =
                giftCertificateController.getAll();
        //then
        assertEquals(result, expected);
        verify(serviceMock).getAll();
    }

    @Test
    public void testGetByIdShouldReturnHttpStatusOkWithDto() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.getById(certId)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.getById(certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getById(certId);
    }

    @Test
    public void testGetAllByTagNameShouldReturnHttpStatusOkWithDtoList() {
        //given
        List<GiftCertificateDto> dtoList = Arrays.asList(defaultCertDto, defaultCertDto);
        String tagName = defaultTagDto.getName();
        when(serviceMock.getAllByTagName(tagName)).thenReturn(dtoList);
        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(dtoList);
        //when
        ResponseEntity<List<GiftCertificateDto>> result = giftCertificateController.getAllByTagName(tagName);
        //then
        assertEquals(result, expected);
        verify(serviceMock).getAllByTagName(tagName);
    }

    @Test
    public void testDeleteByIdShouldReturnHttpStatusNoContent() {
        //given
        ResponseEntity<Void> expected = ResponseEntity.noContent().build();
        Long certId = defaultCertDto.getId();
        //when
        ResponseEntity<Void> result = giftCertificateController.deleteById(certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).delete(certId);
    }

    @Test
    public void testCreateCertificateShouldReturnHttpStatusCreatedWhenThereAreNoErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(false);
        when(serviceMock.save(updatedDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = new ResponseEntity<>(defaultCertDto, HttpStatus.CREATED);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.createCertificate(updatedDto, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
        verify(serviceMock).save(updatedDto);
    }

    @Test
    public void testCreateCertificateShouldReturnHttpStatusBadRequestWhenThereAreErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.badRequest().build();
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.createCertificate(updatedDto, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
    }

    @Test
    public void testUpdateCertificateShouldReturnHttpStatusNoContentWhenThereAreNotErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(false);
        Long certId = defaultCertDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.noContent().build();
        //when
        ResponseEntity<Void> result = giftCertificateController.updateCertificate(updatedDto, certId, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
        verify(serviceMock).update(updatedDto, certId);
    }

    @Test
    public void testUpdateCertificateShouldReturnHttpStatusBadRequestWhenThereAreErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(true);
        Long certId = defaultCertDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.badRequest().build();
        //when
        ResponseEntity<Void> result = giftCertificateController.updateCertificate(updatedDto, certId, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
    }


    @Test
    public void testPatchCertificateShouldReturnHttpStatusNoContentWhenThereAreNotErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(false);
        Long certId = defaultCertDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.noContent().build();
        //when
        ResponseEntity<Void> result = giftCertificateController.patchCertificate(patchedDto, certId, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
        verify(serviceMock).patch(patchedDto, certId);
    }


    @Test
    public void testPatchCertificateShouldReturnHttpStatusBadRequestWhenThereAreErrorsInBindingResult() {
        //given
        when(bindingResult.hasErrors()).thenReturn(true);
        Long certId = defaultCertDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.badRequest().build();
        //when
        ResponseEntity<Void> result = giftCertificateController.patchCertificate(patchedDto, certId, bindingResult);
        //then
        assertEquals(result, expected);
        verify(bindingResult).hasErrors();
    }


}
