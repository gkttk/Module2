package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.EntityNotFoundException;
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
import java.util.Optional;

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
    private static GiftCertificate testEntity;
    private static Tag tag;
    private static TagDto tagDto;

    @BeforeAll
    static void init() {
        tag = new Tag(100L, "testTag");
        tagDto = new TagDto(100L, "testTag");
        testDto = new GiftCertificateDto(100L, "testCertificate", "description", new BigDecimal("1.5"),
                10, "Date", "Date", Arrays.asList(tagDto, tagDto, tagDto));
        testEntity = new GiftCertificate(100L, "testCertificate", "description", new BigDecimal("1.5"),
                10, "Date", "Date");

    }

    @Test
    public void testGetAllShouldReturnListOfDtoWhenThereAreEntitiesInDb() {
        //given
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);
        when(certDao.findAll()).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        Long tagId = testDto.getId();
        List<Tag> tagsForEachDto = Arrays.asList(tag, tag, tag);
        when(tagDao.getAllByCertificateId(tagId)).thenReturn(tagsForEachDto);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAll();
        //then
        verify(certDao).findAll();
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
        verify(tagDao, times(3)).getAllByCertificateId(tagId);
        verify(modelMapper, times(9)).map(tag, TagDto.class);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testGetAllShouldThrowExceptionWhenThereAreNoEntitiesInDb() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        when(certDao.findAll()).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.getAll());
        verify(certDao).findAll();
    }

    @Test
    public void testGetAllByPartOfDescriptionShouldReturnListOfDtoWhenThereAreEntitiesInDbWithGivenDescriptionPart() {
        //given
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);
        String partOfDescription = "desc";
        when(certDao.getAllByPartOfDescription(partOfDescription)).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAllByPartOfDescription(partOfDescription);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllByPartOfDescription(partOfDescription);
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testGetAllByPartOfDescriptionShouldThrowExceptionWhenThereAreNoEntitiesInDbWithGivenDescriptionPart() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        String partOfDescription = "desc";
        when(certDao.getAllByPartOfDescription(partOfDescription)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.getAllByPartOfDescription(partOfDescription));
        verify(certDao).getAllByPartOfDescription(partOfDescription);
    }

    @Test
    public void testGetAllByPartOfNameShouldReturnListOfDtoWhenThereAreEntitiesInDbWithGivenNamePart() {
        //given
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);
        String partOfName = "test";
        when(certDao.getAllByPartOfName(partOfName)).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAllByPartOfName(partOfName);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllByPartOfName(partOfName);
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testGetAllByPartOfNameShouldThrowExceptionWhenThereAreNoEntitiesInDbWithGivenNamePart() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();
        String partOfName = "test";
        when(certDao.getAllByPartOfName(partOfName)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.getAllByPartOfName(partOfName));
        verify(certDao).getAllByPartOfName(partOfName);
    }

    @Test
    public void testGetAllSortedShouldReturnListOfDtoWhenThereAreEntitiesInDb() {
        //given
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);

        List<String> sortingFieldsName = Arrays.asList("name", "id");
        String sortingOrder = "DESC";

        when(certDao.getAllSorted(sortingFieldsName, sortingOrder)).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAllSorted(sortingFieldsName, sortingOrder);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).getAllSorted(sortingFieldsName, sortingOrder);
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
    }

    @Test
    public void testGetAllSortedShouldThrowExceptionWhenThereAreNoEntitiesInDb() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();

        List<String> sortingFieldsName = Arrays.asList("name", "id");
        String sortingOrder = "DESC";

        when(certDao.getAllSorted(sortingFieldsName, sortingOrder)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.getAllSorted(sortingFieldsName, sortingOrder));
        verify(certDao).getAllSorted(sortingFieldsName, sortingOrder);
    }

    @Test
    public void testGetAllByTagNameShouldReturnListOfDtoWhenThereAreEntitiesInDbWithGivenTagName() {
        //given
        List<GiftCertificate> expectedEntityList = Arrays.asList(testEntity, testEntity, testEntity);

        String tagName = "testTag";

        when(certDao.findAllByTagName(tagName)).thenReturn(expectedEntityList);
        when(modelMapper.map(testEntity, GiftCertificateDto.class)).thenReturn(testDto);

        List<Tag> tagsForEachDto = Arrays.asList(tag, tag, tag);

        Long tagId = tagDto.getId();

        when(tagDao.getAllByCertificateId(tagId)).thenReturn(tagsForEachDto);
        when(modelMapper.map(tag, TagDto.class)).thenReturn(tagDto);

        List<GiftCertificateDto> expectedResult = Arrays.asList(testDto, testDto, testDto);
        //when
        List<GiftCertificateDto> result = service.getAllByTagName(tagName);
        //then
        assertEquals(result, expectedResult);
        verify(certDao).findAllByTagName(tagName);
        verify(modelMapper, times(3)).map(testEntity, GiftCertificateDto.class);
        verify(tagDao, times(3)).getAllByCertificateId(tagId);
        verify(modelMapper, times(9)).map(tag, TagDto.class);
    }

    @Test
    public void testGetAllByTagNameShouldThrowExceptionWhenThereAreNoEntitiesInDbe() {
        //given
        List<GiftCertificate> expectedEntityList = Collections.emptyList();

        String tagName = "testTag";

        when(certDao.findAllByTagName(tagName)).thenReturn(expectedEntityList);
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.getAllByTagName(tagName));
        verify(certDao).findAllByTagName(tagName);
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
        assertThrows(EntityNotFoundException.class, () -> service.getById(id));
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
        verify(tagDao, times(3)).findByName(tagName);
        verify(certificateTagsDao, times(3)).save(certId, tagId);
    }

    @Test
    public void testUpdateShouldThrowExceptionWhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long incorrectId = 120L;

        when(certDao.getById(incorrectId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(EntityNotFoundException.class, () -> service.update(testDto, incorrectId));
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
        assertThrows(EntityNotFoundException.class, () -> service.delete(incorrectId));
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
        //when
        service.patch(dtoForPatch, certificateId);
        //then
        verify(certDao,times(2)).getById(certificateId);
        verify(certDao).update(testEntity, certificateId);
        verify(tagDao).getAllByCertificateId(certificateId);
        verify(tagDao).findByName(tag.getName());
        verify(tagDao).findByName(newTagDto.getName());
        verify(modelMapper).map(newTagDto, Tag.class);
        verify(tagDao).save(newTag);
        verify(certificateTagsDao).save(certificateId, newTagWithId.getId());
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
        assertThrows(EntityNotFoundException.class, () -> service.patch(dtoForPatch, incorrectId));
        verify(certDao).getById(incorrectId);
    }

}
