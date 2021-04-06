package com.epam.esm.validator;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.exceptions.GiftCertificateWithSuchNameAlreadyExists;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftCertificateValidator implements EntityValidator<GiftCertificate>{


    private final GiftCertificateDao giftCertificateDao;


    public GiftCertificateValidator(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    public GiftCertificate findByIdIfExist(long certificateId){
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.getById(certificateId);
        return foundCertificateOpt.orElseThrow(() ->
                new GiftCertificateNotFoundException(String.format("GiftCertificate with id: %d doesn't exist in DB", certificateId)));
    }


    public void throwExceptionIfExistWithGivenName(String certificateName){
        Optional<GiftCertificate> foundCertificateOpt = giftCertificateDao.getByName(certificateName);
           if (foundCertificateOpt.isPresent()){
              throw new GiftCertificateWithSuchNameAlreadyExists(String.format("Gift certificate with name: %s already exits.",
                       certificateName));
           }



    }





}
