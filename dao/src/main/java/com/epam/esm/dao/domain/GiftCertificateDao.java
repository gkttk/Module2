package com.epam.esm.dao.domain;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface represents an api to interact with the GiftCertificate entity in database.
 *
 * @since 4.0
 */
public interface GiftCertificateDao extends JpaRepository<GiftCertificate, Long> {

    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 4.0
     */
    Optional<GiftCertificate> findByName(String name);
}
