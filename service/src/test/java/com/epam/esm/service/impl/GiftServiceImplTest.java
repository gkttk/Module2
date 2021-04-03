package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.exceptions.GiftCertificateWithSuchNameAlreadyExists;
import com.epam.esm.exceptions.IllegalRequestParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        testDto.setCreateDate("Date");
        testDto.setLastUpdateDate("Date");
        testDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));


        secondTestDto = new GiftCertificateDto();
        secondTestDto.setId(200L);
        secondTestDto.setName("testCertificate2");
        secondTestDto.setDescription("description2");
        secondTestDto.setPrice(new BigDecimal("2.5"));
        secondTestDto.setDuration(20);
        secondTestDto.setCreateDate("Date2");
        secondTestDto.setLastUpdateDate("Date2");
        secondTestDto.setTags(Arrays.asList(tagDto, tagDto, tagDto));


        testEntity = new GiftCertificate();
        testEntity.setId(100L);
        testEntity.setName("testCertificate");
        testEntity.setDescription("description");
        testEntity.setPrice(new BigDecimal("1.5"));
        testEntity.setDuration(10);
        testEntity.setCreateDate("Date");
        testEntity.setLastUpdateDate("Date");

        secondTestEntity = new GiftCertificate();
        secondTestEntity.setId(200L);
        secondTestEntity.setName("testCertificate2");
        secondTestEntity.setDescription("description2");
        secondTestEntity.setPrice(new BigDecimal("2.5"));
        secondTestEntity.setDuration(20);
        secondTestEntity.setCreateDate("Date2");
        secondTestEntity.setLastUpdateDate("Date2");

    }


    @Test
    public void testGetAllForQueryShouldReturnListOfDtoWhenThereAreNoRequestParamsAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);
        when(certDao.getAll()).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        Long tagId = testDto.getId();
        List<Tag> tagsForEachDto = Arrays.asList(tag, tag, tag);
        when(tagDao.getAllByCertificateId(tagId)).thenReturn(tagsForEachDto);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAll();
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
        verify(tagDao, times(3)).getAllByCertificateId(tagId);
        verify(modelMapper, times(9)).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreNoRequestParamsAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.getAll()).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findAllForQuery(params));
        verify(certDao).getAll();
    }


    @Test
    public void testGetAllForQueryShouldReturnDtoListWhenThereAreRequestParamsDescriptionsPartAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstPartOfDescription = "testPart1";
        String secondPartOfDescription = "testPart2";
        params.put("descriptionsPart", new String[]{firstPartOfDescription, secondPartOfDescription});
        List<GiftCertificate> firstEntityList = Collections.singletonList(testEntity);
        List<GiftCertificate> secondEntityList = Collections.singletonList(secondTestEntity);

        when(certDao.getAllByPartOfDescription(firstPartOfDescription)).thenReturn(firstEntityList);
        when(certDao.getAllByPartOfDescription(secondPartOfDescription)).thenReturn(secondEntityList);

        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(modelMapper.map(secondTestEntity, GiftCertificateDto.class)).thenReturn(secondTestDto);

        Long firstDtoId = testDto.getId();
        when(tagDao.getAllByCertificateId(firstDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        Long secondTestDtoId = secondTestDto.getId();
        when(tagDao.getAllByCertificateId(secondTestDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, secondTestDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllByPartOfDescription(firstPartOfDescription);
        verify(certDao).getAllByPartOfDescription(secondPartOfDescription);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(modelMapper).map(secondTestEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(firstDtoId);
        verify(tagDao).getAllByCertificateId(secondTestDtoId);
        verify(modelMapper, times(4)).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreRequestParamsDescriptionsPartAndThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstPartOfDescription = "testPart1";
        String secondPartOfDescription = "testPart2";
        params.put("descriptionsPart", new String[]{firstPartOfDescription, secondPartOfDescription});
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.getAllByPartOfDescription(firstPartOfDescription)).thenReturn(expectedEntityList);
        when(certDao.getAllByPartOfDescription(secondPartOfDescription)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findAllForQuery(params));
        verify(certDao).getAllByPartOfDescription(firstPartOfDescription);
        verify(certDao).getAllByPartOfDescription(secondPartOfDescription);
    }

    @Test
    public void testGetAllForQueryShouldReturnDtoListWhenThereAreRequestParamsNamesPartAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstNamePart = "testName1";
        String secondNamePart = "testName2";
        params.put("namesPart", new String[]{firstNamePart, secondNamePart});

        List<GiftCertificate> entityList = Collections.singletonList(testEntity);
        when(certDao.getAllByPartOfName(firstNamePart)).thenReturn(entityList);

        List<GiftCertificate> secondEntityList = Collections.singletonList(secondTestEntity);
        when(certDao.getAllByPartOfName(secondNamePart)).thenReturn(secondEntityList);

        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(modelMapper.map(secondTestEntity, GiftCertificateDto.class)).thenReturn(secondTestDto);

        Long firstDtoId = testDto.getId();
        when(tagDao.getAllByCertificateId(firstDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        Long secondTestDtoId = secondTestDto.getId();
        when(tagDao.getAllByCertificateId(secondTestDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, secondTestDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllByPartOfName(firstNamePart);
        verify(certDao).getAllByPartOfName(secondNamePart);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(modelMapper).map(secondTestEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(firstDtoId);
        verify(tagDao).getAllByCertificateId(secondTestDtoId);
        verify(modelMapper, times(4)).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreRequestParamsNamesPartAndThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstNamePart = "testName1";
        String secondNamePart = "testName2";
        params.put("namesPart", new String[]{firstNamePart, secondNamePart});
        List<GiftCertificate> expectedEntityList = Collections.emptyList();

        when(certDao.getAllByPartOfName(firstNamePart)).thenReturn(expectedEntityList);
        when(certDao.getAllByPartOfName(secondNamePart)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findAllForQuery(params));
        verify(certDao).getAllByPartOfName(firstNamePart);
        verify(certDao).getAllByPartOfName(secondNamePart);
    }

    @Test

    public void testGetAllForQueryShouldReturnDtoListWhenThereAreRequestParamsSortFieldsAndOrderAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String sortField = "testField";
        String[] sortFields = new String[]{sortField};
        String order = "desc";
        params.put("sortFields", sortFields);
        params.put("order", new String[]{order});

        List<GiftCertificate> entityList = Collections.singletonList(testEntity);
        when(certDao.getAllSorted(sortFields, order)).thenReturn(entityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        Long tagId = tagDto.getId();
        when(tagDao.getAllByCertificateId(tagId)).thenReturn(Collections.singletonList(tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Collections.singletonList(testDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllSorted(sortFields, order);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(tagId);
        verify(modelMapper).map(tag, TagDto.class);
    }


    @Test
    public void testFindAllForQueryShouldThrowExceptionWhenThereAreRequestParamsRequestParamsSortFieldsAndOrderAndThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String sortField = "testField";
        String[] sortFields = new String[]{sortField};
        String order = "desc";
        params.put("sortFields", sortFields);
        params.put("order", new String[]{order});

        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.getAllSorted(sortFields, order)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findAllForQuery(params));
        verify(certDao).getAllSorted(sortFields, order);
    }

    @Test
    public void testFindAllForQueryShouldThrowExceptionWhenThereAreRequestParamsRequestParamsSortFieldsAndNoOrderParam() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String sortField = "testField";
        String[] sortFields = new String[]{sortField};
        params.put("sortFields", sortFields);
        //when
        //then
        assertThrows(IllegalRequestParameterException.class, () -> service.findAllForQuery(params));
    }


    @Test
    public void testFindAllForQueryShouldReturnDtoListWhenThereAreRequestParamsTagNamesAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        params.put("tagNames", new String[]{firstTagName, secondTagName});

        List<GiftCertificate> entityList = Collections.singletonList(testEntity);
        when(certDao.getAllByTagName(firstTagName)).thenReturn(entityList);

        List<GiftCertificate> secondEntityList = Collections.singletonList(secondTestEntity);
        when(certDao.getAllByTagName(secondTagName)).thenReturn(secondEntityList);

        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(modelMapper.map(secondTestEntity, GiftCertificateDto.class)).thenReturn(secondTestDto);

        Long firstDtoId = testDto.getId();
        when(tagDao.getAllByCertificateId(firstDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        Long secondTestDtoId = secondTestDto.getId();
        when(tagDao.getAllByCertificateId(secondTestDtoId)).thenReturn(Arrays.asList(tag, tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, secondTestDto);
        //when
        List<GiftCertificateDto> result = service.findAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllByTagName(firstTagName);
        verify(certDao).getAllByTagName(secondTagName);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(modelMapper).map(secondTestEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(firstDtoId);
        verify(tagDao).getAllByCertificateId(secondTestDtoId);
        verify(modelMapper, times(4)).map(tag, TagDto.class);
    }


    @Test
    public void testFindAllForQueryShouldThrowExceptionWhenThereAreRequestParamsTagNamesAndThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        params.put("tagNames", new String[]{firstTagName, secondTagName});

        List<GiftCertificate> expectedEntityList = Collections.emptyList();

        when(certDao.getAllByTagName(firstTagName)).thenReturn(expectedEntityList);
        when(certDao.getAllByTagName(secondTagName)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findAllForQuery(params));
        verify(certDao).getAllByTagName(firstTagName);
        verify(certDao).getAllByTagName(secondTagName);
    }

    @Test
    public void testFindByIdShouldReturnDtoWithGivenIdWhenEntityIsPresentInDb() {
        ///given
        Long id = testDto.getId();
        when(certDao.getById(id)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.findById(id);
        //then
        assertEquals(result, testDto);
        verify(certDao).getById(id);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        ///given
        Long id = testDto.getId();
        when(certDao.getById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.findById(id));
        verify(certDao).getById(id);
    }


    @Test
    public void testSaveShouldSaveEntityWithoutGivenTagsIfTheyAreGivenAndPresentInDbAndReturnDtoWithId() {
        //given
        GiftCertificateDto certDtoArg = new GiftCertificateDto();
        certDtoArg.setId(null);
        certDtoArg.setName("testCertificate");
        certDtoArg.setDescription("description");
        certDtoArg.setPrice(new BigDecimal("1.5"));
        certDtoArg.setDuration(10);
        certDtoArg.setCreateDate("Date");
        certDtoArg.setLastUpdateDate("Date");
        certDtoArg.setTags(Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate();
        entityWithoutId.setId(null);
        entityWithoutId.setName("testCertificate");
        entityWithoutId.setDescription("description");
        entityWithoutId.setPrice(new BigDecimal("1.5"));
        entityWithoutId.setDuration(10);
        entityWithoutId.setCreateDate("Date");
        entityWithoutId.setLastUpdateDate("Date");

        when(certDao.getByName(certDtoArg.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity);
        Long tagId = tag.getId();
        String tagName = tag.getName();
        Long certificateId = testEntity.getId();
        when(tagDao.getByName(tagName)).thenReturn(Optional.of(tag));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(tagDao.getAllByCertificateId(certificateId)).thenReturn(Collections.singletonList(tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        //when
        GiftCertificateDto result = service.save(certDtoArg);
        //then
        assertEquals(result, testDto);
        verify(certDao).getByName(certDtoArg.getName());
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).getByName(tagName);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(modelMapper).map(tag, TagDto.class);
    }

    @Test
    public void testSaveShouldSaveEntityWithGivenTagsIfTheyAreGivenAndNotPresentInDbAndReturnDtoWithId() {
        //given
        GiftCertificateDto certDtoArg = new GiftCertificateDto();
        certDtoArg.setId(null);
        certDtoArg.setName("testCertificate");
        certDtoArg.setDescription("description");
        certDtoArg.setPrice(new BigDecimal("1.5"));
        certDtoArg.setDuration(10);
        certDtoArg.setCreateDate("Date");
        certDtoArg.setLastUpdateDate("Date");
        certDtoArg.setTags(Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate();
        entityWithoutId.setId(null);
        entityWithoutId.setName("testCertificate");
        entityWithoutId.setDescription("description");
        entityWithoutId.setPrice(new BigDecimal("1.5"));
        entityWithoutId.setDuration(10);
        entityWithoutId.setCreateDate("Date");
        entityWithoutId.setLastUpdateDate("Date");

        when(certDao.getByName(certDtoArg.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity);
        String tagName = tag.getName();
        when(tagDao.getByName(tagName)).thenReturn(Optional.empty());

        Tag tagWithoutId = new Tag(null, "testTag");
        when(modelMapper.map(tagDto, Tag.class)).thenReturn(tagWithoutId);
        when(tagDao.save(tagWithoutId)).thenReturn(tag);

        Long certificateId = testEntity.getId();
        Long tagId = tag.getId();

        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(tagDao.getAllByCertificateId(certificateId)).thenReturn(Collections.singletonList(tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        //when
        GiftCertificateDto result = service.save(certDtoArg);
        //then
        assertEquals(result, testDto);
        verify(certDao).getByName(certDtoArg.getName());
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).getByName(tagName);
        verify(modelMapper, times(3)).map(tagDto, Tag.class);
        verify(tagDao, times(3)).save(tagWithoutId);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(modelMapper).map(tag, TagDto.class);
    }


    @Test
    public void testSave_EntityWithGivenNameIsPresentInDb_ThrowGiftCertificateWithSuchNameAlreadyExistsException() {
        //given
        String certificateName = testDto.getName();
        when(certDao.getByName(certificateName)).thenReturn(Optional.of(testEntity));
        //when
        //then
        assertThrows(GiftCertificateWithSuchNameAlreadyExists.class, () -> service.save(testDto));
        verify(certDao).getByName(certificateName);
    }

    @Test
    public void testUpdateShouldUpdateGivenEntityFieldsWhenEntityWithGivenIdIsPresentInDb() {
        //given
        Long certId = testDto.getId();

        when(certDao.getById(certId)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testDto, GiftCertificate.class)).thenReturn(testEntity);

        String tagName = tag.getName();
        Long tagId = tag.getId();

        when(tagDao.getByName(tagName)).thenReturn(Optional.of(tag));
        //when
        service.update(testDto, certId);
        //then
        verify(certDao).getById(certId);
        verify(modelMapper).map(testDto, GiftCertificate.class);
        verify(certDao).update(testEntity, certId);
        verify(certificateTagsDao).deleteAllTagsForCertificate(certId);
        verify(tagDao, atLeast(1)).getByName(tagName);
        verify(certificateTagsDao, atLeast(1)).save(certId, tagId);
    }

    @Test
    public void testUpdateShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long incorrectId = 120L;

        when(certDao.getById(incorrectId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.update(testDto, incorrectId));
        verify(certDao).getById(incorrectId);
    }

    @Test
    public void testDeleteShouldDeleteEntityWhenEntityWithGivenIdIsPresentInDb() {
        //given
        Long tagId = tagDto.getId();
        when(certDao.delete(tagId)).thenReturn(true);
        //when
        service.delete(tagId);
        //then
        verify(certDao).delete(tagId);
    }

    @Test
    public void testDeleteShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long incorrectId = 120L;
        when(certDao.delete(incorrectId)).thenReturn(false);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.delete(incorrectId));
        verify(certDao).delete(incorrectId);
    }

    @Test

    public void testPatchShouldPatchEntityFieldsAndAddNewTagsWhenEntityWithGivenIdIsPresentInDb() {
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
        when(certDao.getById(certificateId)).thenReturn(Optional.of(testEntity));

        List<Tag> entityTagsFromDb = Arrays.asList(tag, tag, tag);
        when(tagDao.getAllByCertificateId(certificateId)).thenReturn(entityTagsFromDb);

        when(tagDao.getByName(tag.getName())).thenReturn(Optional.of(tag));
        Tag newTag = new Tag(newTagDto.getName());
        Tag newTagWithId = new Tag(130L, newTag.getName());
        when(tagDao.getByName(newTagDto.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(newTagDto, Tag.class)).thenReturn(newTag);
        when(tagDao.save(newTag)).thenReturn(newTagWithId);
        TagDto newTagDtoWithId = new TagDto(newTagWithId.getId(), newTagWithId.getName());
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(modelMapper.map(newTagWithId, TagDto.class)).thenReturn(newTagDtoWithId);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.patch(dtoForPatch, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).getById(certificateId);
        verify(certDao).update(testEntity, certificateId);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(tagDao).getByName(tag.getName());
        verify(tagDao).getByName(newTagDto.getName());
        verify(modelMapper).map(newTagDto, Tag.class);
        verify(tagDao).save(newTag);
        verify(certificateTagsDao).save(certificateId, newTagWithId.getId());
        verify(modelMapper, times(3)).map(tag, TagDto.class);
        verify(modelMapper).map(newTagWithId, TagDto.class);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testPatchShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        GiftCertificateDto dtoForPatch = new GiftCertificateDto();
        dtoForPatch.setName("newName");
        dtoForPatch.setDescription("newDesc");
        dtoForPatch.setPrice(new BigDecimal("2.5"));
        dtoForPatch.setDuration(20);
        dtoForPatch.setTags(null);

        long incorrectId = 150L;
        when(certDao.getById(incorrectId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.patch(dtoForPatch, incorrectId));
        verify(certDao).getById(incorrectId);
    }

}
