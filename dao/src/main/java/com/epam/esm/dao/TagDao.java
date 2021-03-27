package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Optional<Tag> getById(long id);


    List<Tag> getAllByCertificateId(long certificateId);

    List<Tag> findAll();

    Tag save(Tag tag);

    boolean delete(long id);

    Optional<Tag> findByName(String tagName);
}
