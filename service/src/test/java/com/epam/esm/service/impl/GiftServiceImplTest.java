
package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftServiceImplTest {

    @Mock
    private GiftCertificateDao certDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private CertificateTagsDao certificateTagsDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    private static GiftCertificateDto testDto;
    private static GiftCertificateDto testDtoWithoutId;
    private static GiftCertificate testEntity;
    private static GiftCertificate testEntityWithoutId;
    private static Tag tag;
    private static TagDto tagDto;

    @BeforeAll
    static void init() {
        tag = new Tag();
        tag.setId(100L);
        tag.setName("testTag");
        tagDto = new TagDto();
        tagDto.setId(100L);
        tagDto.setName("testTag");

        testEntity = new GiftCertificate();
        testEntity.setId(100L);
        testEntity.setName("testCertificate");
        testEntity.setDescription("description");
        testEntity.setPrice(new BigDecimal("1.5"));
        testEntity.setDuration(10);
        testEntity.setTags(Collections.singletonList(tag));

        testEntityWithoutId = new GiftCertificate();
        testEntityWithoutId.setName("testName");
        testEntityWithoutId.setDescription("testDescription");
        testEntityWithoutId.setPrice(new BigDecimal("10"));
        testEntityWithoutId.setDuration(25);
        testEntityWithoutId.setTags(Collections.singletonList(tag));

        testDto = new GiftCertificateDto();
        testDto.setId(100L);
        testDto.setName("testCertificate");
        testDto.setDescription("description");
        testDto.setPrice(new BigDecimal("1.5"));
        testDto.setDuration(10);
        testDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));
        testDto.setTags(Collections.singletonList(tagDto));

        testDtoWithoutId = new GiftCertificateDto();
        testDtoWithoutId.setName("testName");
        testDtoWithoutId.setDescription("testDescription");
        testDtoWithoutId.setPrice(new BigDecimal("10"));
        testDtoWithoutId.setDuration(25);
        testDtoWithoutId.setTags(Collections.singletonList(tagDto));
    }

    @Test
    public void testFindById_DtoWithGivenId_EntityWithGivenIdExistsInDb() {
        //given
        long testId = testEntity.getId();
        when(certDao.findById(testId)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.findById(testId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(testId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testFindById_ThrowException_EntityWithGivenIdDoesNotExistInDb() {
        //given
        long testId = -1;
        when(certDao.findById(testId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.findById(testId));
        verify(certDao).findById(testId);
    }

    @Test
    public void testSave_DtoWithId_EntityWithGivenNameDoesNotExistInDb() {
        //given
        when(certDao.findByName(testDtoWithoutId.getName())).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testDtoWithoutId, GiftCertificate.class)).thenReturn(testEntityWithoutId);
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());
        when(certDao.save(testEntityWithoutId)).thenReturn(testEntity);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.save(testDtoWithoutId);
        //then
        assertNotNull(result.getId());
        verify(certDao).findByName(testDtoWithoutId.getName());
        verify(modelMapper).map(testDtoWithoutId, GiftCertificate.class);
        verify(modelMapper).map(tagDto, Tag.class);
        verify(certDao).save(testEntityWithoutId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testUpdate_UpdatedDto_EntityWithGivenNameDoesNotExistInDb() {
        //given
        long certificateId = testDto.getId();
        doNothing().when(certDao.findById(certificateId));
        doNothing().when(certDao.findByName(testDto.getName()));
        doNothing().when(certificateTagsDao).deleteAllTagsForCertificate(certificateId);

        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(tagDao.findByName(tag.getName())).thenReturn(Optional.empty());

        when(modelMapper.map(testDto, GiftCertificate.class)).thenReturn(testEntity);

        when(certDao.update(testEntity)).thenReturn(testEntity);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.update(testDto, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(certificateId);
        verify(certDao).findByName(testDto.getName());
        verify(certificateTagsDao).deleteAllTagsForCertificate(certificateId);
        verify(modelMapper).map(tagDto, Tag.class);
        verify(tagDao).findByName(tag.getName());
        verify(modelMapper).map(testDto, GiftCertificate.class);
        verify(certDao).update(testEntity);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testPatch_PatchedDto_EntityWithGivenNameDoesNotExistInDb() {
        //given
        long certificateId = testDto.getId();
        long tagId = testDto.getTags().get(0).getId();
        when(certDao.findById(certificateId)).thenReturn(Optional.of(testEntity));
        when(certDao.findByName(testDto.getName())).thenReturn(Optional.empty());
        when(certDao.update(testEntity)).thenReturn(testEntity);
        doNothing().when(certificateTagsDao).save(certificateId, tagId);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.patch(testDto, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(certificateId);
        verify(certDao).findByName(testDto.getName());
        verify(certDao).update(testEntity);
        verify(certificateTagsDao).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testDelete_ShouldDeleteEntity_EntityWithGivenIdDoesExistInDb() {
        //given
        long certificateId = testDto.getId();
        when(certDao.delete(certificateId)).thenReturn(true);
        //when
        service.delete(certificateId);
        //then
        verify(certDao).delete(certificateId);
    }

    @Test
    public void testDelete_ShouldThrowException_EntityWithGivenIdDoesNotExistInDb() {
        //given
        long certificateId = -1;
        when(certDao.delete(certificateId)).thenReturn(false);
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.delete(certificateId));
        verify(certDao).delete(certificateId);
    }

    @Test
    public void testFindAllForQuery_ListOfEntities_ThereAreEntitiesInDb() {
        //given
        int limit = 5;
        int offset = 0;
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(certDao.findBy(reqParams, limit, offset)).thenReturn(Arrays.asList(testEntity, testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        int expectedSize = 2;
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams, limit, offset);
        //then
        assertFalse(result.isEmpty());
        assertEquals(result.size(), expectedSize);
        verify(certDao).findBy(reqParams, limit, offset);
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testFindAllForQuery_EmptyList_ThereAreNoEntitiesInDb() {
        //given
        int limit = 5;
        int offset = 0;
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(certDao.findBy(reqParams, limit, offset)).thenReturn(Collections.emptyList());
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams, limit, offset);
        //then
        assertTrue(result.isEmpty());
        verify(certDao).findBy(reqParams, limit, offset);
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }
}

