package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateTagsDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class CertificateTagsDaoTest {

    @Autowired
    private CertificateTagsDao certificateTagsDao;

    @Autowired
    private GiftCertificateDao giftCertificateDao;

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
        certificateWithNewTagOpt.ifPresent(certificate-> {
            List<Tag> tags = certificate.getTags();
            assertTrue(tags.stream().anyMatch(tag-> tag.getId().equals(tagId)));
        });
    }

    @Test
    @Rollback
    public void testDeleteAllTagsForCertificate_ShouldDeleteAllRelations() {
        //given
        long certificateId = 1;
        //when
        certificateTagsDao.deleteAllTagLinksForCertificateId(certificateId);
        //then
        Optional<GiftCertificate> certificateWithNewTagOpt = giftCertificateDao.findById(certificateId);
        certificateWithNewTagOpt.ifPresent(certificate-> {
            List<Tag> tags = certificate.getTags();
            assertTrue(tags.isEmpty());
        });
    }

}
