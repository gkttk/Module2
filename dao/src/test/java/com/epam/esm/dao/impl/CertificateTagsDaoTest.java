package com.epam.esm.dao.impl;

import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dao.domain.GiftCertificateDao;
import com.epam.esm.dao.relation.CertificateTagsDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class CertificateTagsDaoTest {

    private final CertificateTagsDao certificateTagsDao;
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public CertificateTagsDaoTest(CertificateTagsDao certificateTagsDao, GiftCertificateDao giftCertificateDao) {
        this.certificateTagsDao = certificateTagsDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Test
    @Rollback
    public void testSave_ShouldSaveRelation() {
        //given
        long certificateId = 1;
        long tagId = 3;
        //when
        certificateTagsDao.save(certificateId, tagId);
        //then
        Optional<GiftCertificate> certificateWithNewTagOpt = giftCertificateDao.findById(certificateId);
        certificateWithNewTagOpt.ifPresent(certificate -> {
            List<Tag> tags = certificate.getTags();
            assertTrue(tags.stream().anyMatch(tag -> tag.getId().equals(tagId)));
        });
    }

    @Test
    @Rollback
    public void testDeleteAllTagLinksForCertificateId_ShouldDeleteAllRelationsBetweenTagsAndCertificateWithGivenCertificateId() {
        //given
        long certificateId = 1;
        //when
        certificateTagsDao.deleteAllTagLinksForCertificateId(certificateId);
        //then
        Optional<GiftCertificate> certificateWithNewTagOpt = giftCertificateDao.findById(certificateId);
        certificateWithNewTagOpt.ifPresent(certificate -> {
            List<Tag> tags = certificate.getTags();
            assertTrue(tags.isEmpty());
        });
    }

    @Test
    @Rollback
    public void testDeleteAllCertificateLinksForTagId_ShouldDeleteAllRelationsBetweenTagsAndCertificateWithGivenTagId() {
        //given
        long tagId = 1;
        long certificateWithTestTagId = 1;
        //when
        certificateTagsDao.deleteAllCertificateLinksForTagId(tagId);
        //then
        Optional<GiftCertificate> testCertificateOpt = giftCertificateDao.findById(certificateWithTestTagId);
        testCertificateOpt.ifPresent(certificate -> {
                    List<Tag> tags = certificate.getTags();
                    boolean isContainTagWithGivenId = tags.stream().anyMatch(tag -> tag.getId().equals(tagId));
                    Assertions.assertFalse(isContainTagWithGivenId);
                }
        );
    }


}
