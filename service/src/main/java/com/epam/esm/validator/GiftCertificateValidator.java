package com.epam.esm.validator;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.GiftCertificateException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftCertificateValidator implements EntityValidator<GiftCertificate> {


    private final GiftCertificateDao giftCertificateDao;

    private final static int CERTIFICATE_NOT_FOUND_CODE = 40401;
    private final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;


    public GiftCertificateValidator(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    public GiftCertificate findByIdIfExist(long certificateId) {
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.getById(certificateId);
        return foundCertificateOpt.orElseThrow(() ->
                new GiftCertificateException(CERTIFICATE_NOT_FOUND_CODE, String.format("GiftCertificate with id: %d doesn't exist in DB", certificateId)));
    }


    public void throwExceptionIfExistWithGivenName(String certificateName) {
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.getByName(certificateName);
        if (foundCertificateOpt.isPresent()) {
            throw new GiftCertificateException(CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE, String.format("Gift certificate with name: %s already exits.",
                    certificateName));
        }


    }


}
