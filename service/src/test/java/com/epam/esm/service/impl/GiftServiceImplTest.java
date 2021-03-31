package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
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
        testDto = new GiftCertificateDto(100L, "testCertificate", "description", new BigDecimal("1.5"),
                10, "Date", "Date", Arrays.asList(tagDto, tagDto, tagDto));
        secondTestDto = new GiftCertificateDto(200L, "testCertificate2", "description2", new BigDecimal("2.5"),
                20, "Date2", "Date2", Arrays.asList(tagDto, tagDto, tagDto));
        testEntity = new GiftCertificate(100L, "testCertificate", "description", new BigDecimal("1.5"),
                10, "Date", "Date");
        secondTestEntity = new GiftCertificate(200L, "testCertificate2", "description2", new BigDecimal("2.5"),
                20, "Date2", "Date2");

    }


    @Test
    public void testGetAllForQueryShouldReturnListOfDtoWhenThereAreNoRequestParamsAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);
        when(certDao.findAll()).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        Long tagId = testDto.getId();
        List<Tag> tagsForEachDto = Arrays.asList(tag, tag, tag);
        when(tagDao.getAllByCertificateId(tagId)).thenReturn(tagsForEachDto);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findAll();
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
        verify(tagDao, times(3)).getAllByCertificateId(tagId);
        verify(modelMapper, times(9)).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreNoRequestParamsAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findAll()).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getAllForQuery(params));
        verify(certDao).findAll();
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
        List<GiftCertificateDto> result = service.getAllForQuery(params);
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
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getAllForQuery(params));
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
        List<GiftCertificateDto> result = service.getAllForQuery(params);
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
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getAllForQuery(params));
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
        List<GiftCertificateDto> result = service.getAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllSorted(sortFields, order);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(tagId);
        verify(modelMapper).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreRequestParamsRequestParamsSortFieldsAndOrderAndThereAreNoEntitiesInDb() {
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
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getAllForQuery(params));
        verify(certDao).getAllSorted(sortFields, order);
    }

    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreRequestParamsRequestParamsSortFieldsAndNoOrderParam() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String sortField = "testField";
        String[] sortFields = new String[]{sortField};
        params.put("sortFields", sortFields);
        //when
        //then
        assertThrows(IllegalRequestParameterException.class, () -> service.getAllForQuery(params));
    }


    @Test
    public void testGetAllForQueryShouldReturnDtoListWhenThereAreRequestParamsTagNamesAndThereAreEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        params.put("tagNames", new String[]{firstTagName, secondTagName});

        List<GiftCertificate> entityList = Collections.singletonList(testEntity);
        when(certDao.findAllByTagName(firstTagName)).thenReturn(entityList);

        List<GiftCertificate> secondEntityList = Collections.singletonList(secondTestEntity);
        when(certDao.findAllByTagName(secondTagName)).thenReturn(secondEntityList);

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
        List<GiftCertificateDto> result = service.getAllForQuery(params);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findAllByTagName(firstTagName);
        verify(certDao).findAllByTagName(secondTagName);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(modelMapper).map(secondTestEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(firstDtoId);
        verify(tagDao).getAllByCertificateId(secondTestDtoId);
        verify(modelMapper, times(4)).map(tag, TagDto.class);
    }


    @Test
    public void testGetAllForQueryShouldThrowExceptionWhenThereAreRequestParamsTagNamesAndThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> params = new HashMap<>();
        String firstTagName = "tagName1";
        String secondTagName = "tagName2";
        params.put("tagNames", new String[]{firstTagName, secondTagName});

        List<GiftCertificate> expectedEntityList = Collections.emptyList();

        when(certDao.findAllByTagName(firstTagName)).thenReturn(expectedEntityList);
        when(certDao.findAllByTagName(secondTagName)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getAllForQuery(params));
        verify(certDao).findAllByTagName(firstTagName);
        verify(certDao).findAllByTagName(secondTagName);
    }

    @Test
    public void testGetByIdShouldReturnDtoWithGivenIdWhenEntityIsPresentInDb() {
        ///given
        Long id = testDto.getId();
        when(certDao.getById(id)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        //when
        GiftCertificateDto result = service.getById(id);
        //then
        assertEquals(result, testDto);
        verify(certDao).getById(id);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testGetByIdShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        ///given
        Long id = testDto.getId();
        when(certDao.getById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.getById(id));
        verify(certDao).getById(id);
    }


    @Test
    public void testSaveShouldSaveEntityWithoutGivenTagsIfTheyAreGivenAndPresentInDbAndReturnDtoWithId() {
        //given

        GiftCertificateDto certDtoArg = new GiftCertificateDto(null, "testCertificate", "description",
                new BigDecimal("1.5"), 10, "Date", "Date", Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate(null, "testCertificate", "description",
                new BigDecimal("1.5"), 10, "Date", "Date");
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity);
        Long tagId = tag.getId();
        String tagName = tag.getName();
        Long certificateId = testEntity.getId();
        when(tagDao.findByName(tagName)).thenReturn(Optional.of(tag));
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);
        when(tagDao.getAllByCertificateId(certificateId)).thenReturn(Collections.singletonList(tag));
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);
        //when
        GiftCertificateDto result = service.save(certDtoArg);
        //then
        assertEquals(result, testDto);
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).findByName(tagName);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(modelMapper).map(tag, TagDto.class);
    }

    @Test
    public void testSaveShouldSaveEntityWithGivenTagsIfTheyAreGivenAndNotPresentInDbAndReturnDtoWithId() {
        //given
        GiftCertificateDto certDtoArg = new GiftCertificateDto(null, "testCertificate", "description",
                new BigDecimal("1.5"), 10, "Date", "Date", Arrays.asList(tagDto, tagDto, tagDto));

        GiftCertificate entityWithoutId = new GiftCertificate(null, "testCertificate", "description",
                new BigDecimal("1.5"), 10, "Date", "Date");
        when(modelMapper.map(certDtoArg, GiftCertificate.class)).thenReturn(entityWithoutId);
        when(certDao.save(entityWithoutId)).thenReturn(testEntity);
        String tagName = tag.getName();
        when(tagDao.findByName(tagName)).thenReturn(Optional.empty());

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
        verify(modelMapper).map(certDtoArg, GiftCertificate.class);
        verify(certDao).save(entityWithoutId);
        verify(tagDao, times(3)).findByName(tagName);
        verify(modelMapper, times(3)).map(tagDto, Tag.class);
        verify(tagDao, times(3)).save(tagWithoutId);
        verify(certificateTagsDao, times(3)).save(certificateId, tagId);
        verify(modelMapper).map(testEntity, GiftCertificateDto.class);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(modelMapper).map(tag, TagDto.class);
    }


    @Test
    public void testUpdateShouldUpdateGivenEntityFieldsWhenEntityWithGivenIdIsPresentInDb() {
        //given
        Long certId = testDto.getId();

        when(certDao.getById(certId)).thenReturn(Optional.of(testEntity));
        when(modelMapper.map(testDto, GiftCertificate.class)).thenReturn(testEntity);

        String tagName = tag.getName();
        Long tagId = tag.getId();

        when(tagDao.findByName(tagName)).thenReturn(Optional.of(tag));
        //when
        service.update(testDto, certId);
        //then
        verify(certDao).getById(certId);
        verify(modelMapper).map(testDto, GiftCertificate.class);
        verify(certDao).update(testEntity, certId);
        verify(certificateTagsDao).deleteAllTagsForCertificate(certId);
        verify(tagDao, atLeast(1)).findByName(tagName);
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
        GiftCertificatePatchDto dtoForPatch = new GiftCertificatePatchDto("newName", "newDesc",
                new BigDecimal("2.5"), 20, tagsForPatch);
        Long certificateId = testEntity.getId();
        when(certDao.getById(certificateId)).thenReturn(Optional.of(testEntity));

        List<Tag> entityTagsFromDb = Arrays.asList(tag, tag, tag);
        when(tagDao.getAllByCertificateId(certificateId)).thenReturn(entityTagsFromDb);

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
        //when
        GiftCertificateDto result = service.patch(dtoForPatch, certificateId);
        //then
        assertEquals(result, testDto);
        verify(certDao).getById(certificateId);
        verify(certDao).update(testEntity, certificateId);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(tagDao).findByName(tag.getName());
        verify(tagDao).findByName(newTagDto.getName());
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
        GiftCertificatePatchDto dtoForPatch = new GiftCertificatePatchDto("newName", "newDesc",
                new BigDecimal("2.5"), 20, null);
        long incorrectId = 150L;
        when(certDao.getById(incorrectId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateNotFoundException.class, () -> service.patch(dtoForPatch, incorrectId));
        verify(certDao).getById(incorrectId);
    }

}
