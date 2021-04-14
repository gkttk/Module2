
package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.sorting.GiftCertificateSortingHelper;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.atLeast;
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

    @Mock
    private GiftCertificateSortingHelper sortingHelper;

    @Mock
    private GiftCertificateValidator validator;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    private static GiftCertificateDto testDto;
    private static GiftCertificateDto secondTestDto;

    private GiftCertificate testEntity;
    private GiftCertificate secondTestEntity;
    private Tag tag;
    private TagDto tagDto;

    @BeforeEach
    void init() {

        tag = new Tag(100L, "testTag");
        tagDto = new TagDto(100L, "testTag");

        testDto = new GiftCertificateDto();
        testDto.setId(100L);
        testDto.setName("testCertificate");
        testDto.setDescription("description");
        testDto.setPrice(new BigDecimal("1.5"));
        testDto.setDuration(10);
        testDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));


        secondTestDto = new GiftCertificateDto();
        secondTestDto.setId(200L);
        secondTestDto.setName("testCertificate2");
        secondTestDto.setDescription("description2");
        secondTestDto.setPrice(new BigDecimal("2.5"));
        secondTestDto.setDuration(20);
        secondTestDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));


        testEntity = new GiftCertificate();
        testEntity.setId(100L);
        testEntity.setName("testCertificate");
        testEntity.setDescription("description");
        testEntity.setPrice(new BigDecimal("1.5"));
        testEntity.setDuration(10);

        secondTestEntity = new GiftCertificate();
        secondTestEntity.setId(200L);
        secondTestEntity.setName("testCertificate2");
        secondTestEntity.setDescription("description2");
        secondTestEntity.setPrice(new BigDecimal("2.5"));
        secondTestEntity.setDuration(20);

    }


    @Test
    public void testGetAllForQuery_ThereAreNoRequestParamsAndThereAreEntitiesInDb_ReturnListOfDto() {
        //given

        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity);
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(anyMap());
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    public void testGetAllForQuery_ThereAreNoRequestParamsAndThereAreNoEntitiesInDb_ReturnEmptyList() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        List<GiftCertificateDto> expectedResult = Collections.emptyList();
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(anyMap());
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
    }


    @Test
    public void testGetAllForQuery_ThereAreRequestParamsDescriptionsPartAndThereAreEntitiesInDb_ReturnDtoList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstPartOfDescription = "testPart1";
        String secondPartOfDescription = "testPart2";
        String[] params = {firstPartOfDescription, secondPartOfDescription};
        reqParams.put("descriptionsPart", params);

        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity);
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    public void testGetAllForQuery_ThereAreRequestParamsDescriptionsPartAndThereAreNoEntitiesInDb_ReturnEmptyList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstPartOfDescription = "testPart1";
        String secondPartOfDescription = "testPart2";
        String[] params = {firstPartOfDescription, secondPartOfDescription};
        reqParams.put("descriptionsPart", params);

        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        List<GiftCertificateDto> expectedResult = Collections.emptyList();
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
    }


    @Test
    public void testGetAllForQuery_ThereAreRequestParamsNamesPartAndThereAreEntitiesInDb_ReturnDtoList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstNamePart = "testName1";
        String secondNamePart = "testName2";
        String[] params = {firstNamePart, secondNamePart};
        reqParams.put("namesPart", params);

        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity);
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    public void testGetAllForQuery_ThereAreRequestParamsNamesPartAndThereAreNoEntitiesInDb_ReturnEmptyList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstNamePart = "testName1";
        String secondNamePart = "testName2";
        String[] params = {firstNamePart, secondNamePart};
        reqParams.put("namesPart", new String[]{firstNamePart, secondNamePart});
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        List<GiftCertificateDto> expectedResult = Collections.emptyList();
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
    }


    @Test
    public void testGetAllForQuery_ThereAreRequestParamsSortFieldsAndOrderAndThereAreEntitiesInDb_ReturnDtoList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String sortField = "id";
        String[] sortFields = new String[]{sortField};
        String[] order = new String[]{"desc"};
        reqParams.put("sortFields", sortFields);

        reqParams.put("order", order);

        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, secondTestEntity);
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(modelMapper.map(secondTestEntity, GiftCertificateDto.class)).thenReturn(secondTestDto);
        when(sortingHelper.getSorted(sortFields, order, expectedEntityList)).thenReturn(Arrays.asList(secondTestEntity, testEntity));

        List<GiftCertificateDto> expectedResult = Arrays.asList(secondTestDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
        verify(sortingHelper).getSorted(sortFields, order, expectedEntityList);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(modelMapper).map(secondTestEntity, GiftCertificateDto.class);
    }


    @Test
    public void testFindAllForQuery_ThereAreRequestParamsRequestParamsSortFieldsAndOrderAndThereAreNoEntitiesInDb_ReturnEmptyList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String sortField = "id";
        String[] sortFields = new String[]{sortField};
        String order = "desc";
        reqParams.put("sortFields", sortFields);
        reqParams.put("order", new String[]{order});

        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        List<GiftCertificateDto> expectedResult = Collections.emptyList();
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
    }


    @Test
    public void testFindAllForQuery_ThereAreRequestParamsTagNamesAndThereAreEntitiesInDb_ReturnDtoList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        String[] params = {firstTagName, secondTagName};
        reqParams.put("tagNames", params);

        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity);
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
        verify(modelMapper, times(2)).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    public void testFindAllForQuery_ThereAreRequestParamsTagNamesAndThereAreNoEntitiesInDb_ReturnEmptyList() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        String[] params = {firstTagName, secondTagName};
        reqParams.put("tagNames", params);

        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findBy(anyMap())).thenReturn(expectedEntityList);
        List<GiftCertificateDto> expectedResult = Collections.emptyList();
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(reqParams);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findBy(anyMap());
    }


    @Test
    public void testFindById_EntityIsPresentInDb_ReturnDtoWithGivenId() {
        ///given
        Long id = testDto.getId();
        when(certDao.findById(id)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.findById(id);
        //then
        assertEquals(result, testDto);
        verify(certDao).findById(id);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    public void testFindById_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        ///given
        Long id = testDto.getId();
        when(certDao.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.findById(id));
        verify(certDao).findById(id);
    }


    @Test
    @Disabled
    public void testSave_TagsAreGivenAndPresentInDbAndReturnDtoWithId_SaveEntityWithoutGivenTags() {
        //given
        GiftCertificateDto certDtoArg = new GiftCertificateDto();
        certDtoArg.setId(null);
        certDtoArg.setName("testCertificate");
        certDtoArg.setDescription("description");
        certDtoArg.setPrice(new BigDecimal("1.5"));
        certDtoArg.setDuration(10);
        certDtoArg.setTags(Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate();
        entityWithoutId.setId(null);
        entityWithoutId.setName("testCertificate");
        entityWithoutId.setDescription("description");
        entityWithoutId.setPrice(new BigDecimal("1.5"));
        entityWithoutId.setDuration(10);

        when(certDao.findByName(certDtoArg.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity.getId());
        Long tagId = tag.getId();
        String tagName = tag.getName();
        Long certificateId = testEntity.getId();
        when(tagDao.findByName(tagName)).thenReturn(Optional.of(tag));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.save(certDtoArg);
        //then
        assertEquals(result, testDto);
        verify(certDao).findByName(certDtoArg.getName());
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).findByName(tagName);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    @Disabled
    public void testSave_TagsAreGivenAndNotPresentInDbAndReturnDtoWithId_SaveEntityWithGivenTags() {
        //given
        GiftCertificateDto certDtoArg = new GiftCertificateDto();
        certDtoArg.setId(null);
        certDtoArg.setName("testCertificate");
        certDtoArg.setDescription("description");
        certDtoArg.setPrice(new BigDecimal("1.5"));
        certDtoArg.setDuration(10);
        certDtoArg.setTags(Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate();
        entityWithoutId.setId(null);
        entityWithoutId.setName("testCertificate");
        entityWithoutId.setDescription("description");
        entityWithoutId.setPrice(new BigDecimal("1.5"));
        entityWithoutId.setDuration(10);

        when(certDao.findByName(certDtoArg.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity.getId());
        String tagName = tag.getName();
        when(tagDao.findByName(tagName)).thenReturn(Optional.empty());

        Tag tagWithoutId = new Tag(null, "testTag");
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tagWithoutId);
        when(tagDao.save(tagWithoutId)).thenReturn(tag);

        Long certificateId = testEntity.getId();
        Long tagId = tag.getId();

        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.save(certDtoArg);
        //then
        assertEquals(result, testDto);
        verify(certDao).findByName(certDtoArg.getName());
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).findByName(tagName);
        verify(modelMapper, times(3)).map(tagDto, Tag.class);
        verify(tagDao, times(3)).save(tagWithoutId);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }


    @Test
    @Disabled
    public void testSave_EntityWithGivenNameIsPresentInDb_ThrowGiftCertificateWithSuchNameAlreadyExistsException() {
        //given
        String certificateName = testDto.getName();
        when(certDao.findByName(certificateName)).thenReturn(Optional.of(testEntity));
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.save(testDto));
        verify(certDao).findByName(certificateName);
    }

    @Test
    @Disabled
    public void testUpdate_EntityWithGivenIdIsPresentInDb_UpdateGivenEntityFields() {
        //given
        Long certId = testDto.getId();

        when(certDao.findById(certId)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testDto, GiftCertificate.class)).thenReturn(testEntity);

        String tagName = tag.getName();
        Long tagId = tag.getId();

        when(tagDao.findByName(tagName)).thenReturn(Optional.of(tag));
        //when
        service.update(testDto, certId);
        //then
        verify(certDao).findById(certId);
        verify(modelMapper).map(testDto, GiftCertificate.class);
        verify(certDao).update(testEntity, certId);
        verify(certificateTagsDao).deleteAllTagsForCertificate(certId);
        verify(tagDao, atLeast(1)).findByName(tagName);
        verify(certificateTagsDao, atLeast(1)).save(certId, tagId);
    }

    @Test
    public void testUpdate_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        //given
        long incorrectId = 120L;
        when(validator.validateAndFindByIdIfExist(incorrectId)).thenThrow(GiftCertificateException.class);
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.update(testDto, incorrectId));
        verify(validator).validateAndFindByIdIfExist(incorrectId);
    }

    @Test
    public void testDelete_GivenIdIsPresentInDb_DeleteEntityWhenEntity() {
        //given
        Long tagId = tagDto.getId();
        when(certDao.delete(tagId)).thenReturn(true);
        //when
        service.delete(tagId);
        //then
        verify(certDao).delete(tagId);
    }

    @Test
    public void testDelete_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        //given
        long incorrectId = 120L;
        when(certDao.delete(incorrectId)).thenReturn(false);
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.delete(incorrectId));
        verify(certDao).delete(incorrectId);
    }

    @Test

    public void testPatch_EntityWithGivenIdIsPresentInDb_PatchEntityFieldsAndAddNewTags() {
        //given
        TagDto newTagDto = new TagDto(null, "newTag");
        List<TagDto> tagsForPatch = Arrays.asList(tagDto, newTagDto);
        GiftCertificateDto dtoForPatch = new GiftCertificateDto();
        dtoForPatch.setName("newName");
        dtoForPatch.setDescription("newDesc");
        dtoForPatch.setPrice(new BigDecimal("2.5"));
        dtoForPatch.setDuration(20);
        dtoForPatch.setTags(tagsForPatch);

        Long certificateId = testEntity.getId();
        when(validator.validateAndFindByIdIfExist(certificateId)).thenReturn(testEntity);
        doNothing().when(validator).validateIfAnotherEntityWithGivenNameExist(dtoForPatch.getName(), certificateId);

        when(tagDao.findByName(tag.getName())).thenReturn(Optional.of(tag));
        Tag newTag = new Tag(newTagDto.getName());
        Tag newTagWithId = new Tag(130L, newTag.getName());
        when(tagDao.findByName(newTagDto.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(newTagDto, Tag.class)).thenReturn(newTag);
        when(tagDao.save(newTag)).thenReturn(newTagWithId);
        TagDto newTagDtoWithId = new TagDto(newTagWithId.getId(), newTagWithId.getName());
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(modelMapper.map(newTagWithId, TagDto.class)).thenReturn(newTagDtoWithId);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(certDao.findById(certificateId)).thenReturn(Optional.of(testEntity));
        //when
        GiftCertificateDto result = service.patch(dtoForPatch, certificateId);
        //then
        assertEquals(result, testDto);
        verify(validator).validateAndFindByIdIfExist(certificateId);
        verify(validator).validateIfAnotherEntityWithGivenNameExist(dtoForPatch.getName(), certificateId);
        verify(certDao).findById(certificateId);
        verify(certDao).update(testEntity, certificateId);
        verify(tagDao).findByName(tag.getName());
        verify(tagDao).findByName(newTagDto.getName());
        verify(modelMapper).map(newTagDto, Tag.class);
        verify(tagDao).save(newTag);
        verify(certificateTagsDao).save(certificateId, newTagWithId.getId());
        verify(modelMapper).map(tag, TagDto.class);
        verify(modelMapper).map(newTagWithId, TagDto.class);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testPatch_EntityWithGivenIdIsNotPresentInDb_ThrowException() {
        //given
        GiftCertificateDto dtoForPatch = new GiftCertificateDto();
        dtoForPatch.setName("newName");
        dtoForPatch.setDescription("newDesc");
        dtoForPatch.setPrice(new BigDecimal("2.5"));
        dtoForPatch.setDuration(20);
        dtoForPatch.setTags(null);

        long incorrectId = 150L;
        when(validator.validateAndFindByIdIfExist(incorrectId)).thenThrow(GiftCertificateException.class);
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> service.patch(dtoForPatch, incorrectId));
        verify(validator).validateAndFindByIdIfExist(incorrectId);
    }

}

