package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateControllerTest {

    @Mock
    private GiftCertificateService serviceMock;
    @Mock
    private WebRequest webRequestMock;

    @InjectMocks
    private GiftCertificateController giftCertificateController;

    private static GiftCertificateDto defaultCertDto;
    private static TagDto defaultTagDto;
    private static GiftCertificateDto updatedDto;
    private static GiftCertificatePatchDto patchedDto;

    @BeforeAll
    static void init() {
        defaultTagDto = new TagDto(100L, "testTag");

        defaultCertDto = new GiftCertificateDto();
        defaultCertDto.setId(100L);
        defaultCertDto.setName("testCertificate");
        defaultCertDto.setDescription("description");
        defaultCertDto.setPrice(new BigDecimal("1.5"));
        defaultCertDto.setDuration(10);
        defaultCertDto.setCreateDate("Date");
        defaultCertDto.setLastUpdateDate("Date");
        defaultCertDto.setTags(Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

        updatedDto = new GiftCertificateDto();
        updatedDto.setId(null);
        updatedDto.setName("testCertificate");
        updatedDto.setDescription("description");
        updatedDto.setPrice(new BigDecimal("1.5"));
        updatedDto.setDuration(10);
        updatedDto.setCreateDate("Date");
        updatedDto.setLastUpdateDate("Date");
        updatedDto.setTags(Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

        patchedDto = new GiftCertificatePatchDto();
        patchedDto.setName("testCertificate");
        patchedDto.setDescription("description");
        patchedDto.setPrice(new BigDecimal("1.5"));
        patchedDto.setDuration(10);
        patchedDto.setTags(Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

    }


    @Test
    public void testGetAllForQueryShouldReturnHttpStatusOkWithDtoList() {
        //given

        Map<String, String[]> paramMap = new HashMap<>();
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        when(webRequestMock.getParameterMap()).thenReturn(paramMap);
        when(serviceMock.findAllForQuery(paramMap)).thenReturn(listDto);

        ResponseEntity<List<GiftCertificateDto>> expected = ResponseEntity.ok(listDto);
        //when
        ResponseEntity<List<GiftCertificateDto>> result = giftCertificateController.getAllForQuery(webRequestMock);
        //then
        assertEquals(result, expected);
        verify(webRequestMock).getParameterMap();
        verify(serviceMock).findAllForQuery(paramMap);
    }

    @Test
    public void testGetByIdShouldReturnHttpStatusOkWithDto() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.findById(certId)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.getById(certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findById(certId);
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
        when(serviceMock.save(updatedDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = new ResponseEntity<>(defaultCertDto, HttpStatus.CREATED);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.createCertificate(updatedDto);
        //then
        assertEquals(result, expected);
        verify(serviceMock).save(updatedDto);
    }

    @Test
    public void testPatchCertificateShouldReturnHttpStatusOkWithDtoWhenThereAreNotErrorsInBindingResult() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.patch(patchedDto, certId)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.patchCertificate(patchedDto, certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).patch(patchedDto, certId);
    }

    @Test
    public void testUpdateCertificateShouldReturnHttpStatusNoContentWhenThereAreNotErrorsInBindingResult() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.update(updatedDto, certId)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.updateCertificate(updatedDto, certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).update(updatedDto, certId);
    }

}
