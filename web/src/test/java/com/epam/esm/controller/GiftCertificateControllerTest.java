package com.epam.esm.controller;


import com.epam.esm.assemblers.GiftCertificateModelAssembler;
import com.epam.esm.domain.dto.GiftCertificateDto;
import com.epam.esm.domain.dto.TagDto;
import com.epam.esm.domain.dto.bundles.GiftCertificateDtoBundle;
import com.epam.esm.domain.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
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
    private GiftCertificateModelAssembler assemblerMock;
    @Mock
    private GiftCertificateService serviceMock;
    @Mock
    private WebRequest webRequestMock;


    @InjectMocks
    private GiftCertificateController giftCertificateController;

    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;
    private static final long TEST_COUNT = 1000;

    private static GiftCertificateDto defaultCertDto;
    private static GiftCertificateDto testDto;


    @BeforeAll
    static void init() {

        TagDto defaultTagDto = new TagDto();
        defaultTagDto.setId(100L);
        defaultTagDto.setName("testTag");

        defaultCertDto = new GiftCertificateDto();
        defaultCertDto.setId(100L);
        defaultCertDto.setName("testCertificate");
        defaultCertDto.setDescription("description");
        defaultCertDto.setPrice(new BigDecimal("1.5"));
        defaultCertDto.setDuration(10);
        defaultCertDto.setTags(Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

        testDto = new GiftCertificateDto();
        testDto.setId(null);
        testDto.setName("testCertificate");
        testDto.setDescription("description");
        testDto.setPrice(new BigDecimal("1.5"));
        testDto.setDuration(10);
        testDto.setTags(Arrays.asList(defaultTagDto, defaultTagDto, defaultTagDto));

    }

    @Test
    public void testGetAllForQuery_ReturnHttpStatusOkWithDtoList() {
        //given
        Map<String, String[]> paramMap = new HashMap<>();
        List<GiftCertificateDto> listDto = Arrays.asList(defaultCertDto, defaultCertDto);
        when(webRequestMock.getParameterMap()).thenReturn(paramMap);
        when(serviceMock.findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET)).thenReturn(new GiftCertificateDtoBundle(listDto, TEST_COUNT));
        when(assemblerMock.toCollectionModel(listDto, TEST_OFFSET, TEST_COUNT, paramMap)).thenReturn(CollectionModel.of(listDto));
        ResponseEntity<CollectionModel<GiftCertificateDto>> expectedResult = ResponseEntity.ok(CollectionModel.of(listDto));

        //when
        ResponseEntity<CollectionModel<GiftCertificateDto>> result = giftCertificateController.getAllForQuery(webRequestMock, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(webRequestMock).getParameterMap();
        verify(serviceMock).findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET);
        verify(assemblerMock).toCollectionModel(listDto, TEST_OFFSET, TEST_COUNT, paramMap);
    }


    @Test
    public void testGetById_ReturnHttpStatusOkWithDto() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.findById(certId)).thenReturn(defaultCertDto);
        when(assemblerMock.toModel(defaultCertDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.getById(certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).findById(certId);
        verify(assemblerMock).toModel(defaultCertDto);
    }

    @Test
    public void testDeleteById_ReturnHttpStatusNoContent() {
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
    public void testCreateCertificate_ThereAreNoValidationErrors_ReturnHttpStatusOk() {
        //given
        when(serviceMock.save(testDto)).thenReturn(defaultCertDto);
        when(assemblerMock.toModel(defaultCertDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.createCertificate(testDto);
        //then
        assertEquals(result, expected);
        verify(serviceMock).save(testDto);
        verify(assemblerMock).toModel(defaultCertDto);
    }

    @Test
    public void testPatchCertificate_ThereAreNoValidationErrors_ReturnHttpStatusOkWithDto() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.patch(testDto, certId)).thenReturn(defaultCertDto);
        when(assemblerMock.toModel(defaultCertDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.patchCertificate(testDto, certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).patch(testDto, certId);
        verify(assemblerMock).toModel(defaultCertDto);
    }


    @Test
    public void testUpdateCertificate_ThereAreNoValidationErrors_ReturnHttpStatusNoContent() {
        //given
        Long certId = defaultCertDto.getId();
        when(serviceMock.update(testDto, certId)).thenReturn(defaultCertDto);
        when(assemblerMock.toModel(defaultCertDto)).thenReturn(defaultCertDto);
        ResponseEntity<GiftCertificateDto> expected = ResponseEntity.ok(defaultCertDto);
        //when
        ResponseEntity<GiftCertificateDto> result = giftCertificateController.updateCertificate(testDto, certId);
        //then
        assertEquals(result, expected);
        verify(serviceMock).update(testDto, certId);
        verify(assemblerMock).toModel(defaultCertDto);
    }


}
