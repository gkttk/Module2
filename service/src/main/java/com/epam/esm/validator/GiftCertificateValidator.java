package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.GiftCertificateException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link EntityValidator} for GiftCertificateEntity.
 *
 * @since 1.0
 */
@Component
public class GiftCertificateValidator implements EntityValidator<GiftCertificate> {

    private final GiftCertificateDao giftCertificateDao;

    public GiftCertificateValidator(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    /**
     * This method attempts to get an GiftCertificate entity from db by it's id.
     *
     * @param certificateId id of the GiftCertificate entity.
     * @return GiftCertificate entity.
     * @throws GiftCertificateException when there is no entity with given id in db.
     * @since 1.0
     */
    public GiftCertificate validateAndFindByIdIfExist(long certificateId) {
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.findById(certificateId);
        return foundCertificateOpt.orElseThrow(() ->
                new GiftCertificateException(ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, String.format("GiftCertificate with id: %d doesn't exist in DB", certificateId)));
    }

    /**
     * This method checks if a GiftCertificate entity with given id exists in db by it's name.
     *
     * @param certificateName name of the GiftCertificate entity.
     * @throws GiftCertificateException if there is GiftCertificate entity with given id in db.
     * @since 1.0
     */
    public void validateIfEntityWithGivenNameExist(String certificateName) {
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.findByName(certificateName);
        if (foundCertificateOpt.isPresent()) {
            throw new GiftCertificateException(ApplicationConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE, String.format("Gift certificate with name: %s already exits.",
                    certificateName));
        }
    }

    /**
     * This method throws an exception when GiftCertificate entity with given name and another id is present in db.
     *
     * @param certificateName name of GiftCertificate entity
     * @param certificateId   id of GiftCertificate entity
     * @throws GiftCertificateException when there is another GiftCertificate entity in db with given name.
     * @since 1.0
     */
    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String certificateName, long certificateId) {
        Optional<GiftCertificate> foundCertOpt = giftCertificateDao.findByName(certificateName);
        foundCertOpt.ifPresent(certificate -> {
            if (!certificate.getId().equals(certificateId)) {
                throw new GiftCertificateException(ApplicationConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE, String.format("Gift certificate with name: %s already exits.",
                        certificate.getName()));
            }
        });
    }


}
