package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Tag getById(long id);

    List<Tag> findAll();

    Tag save(Tag tag);

    void delete(long id);

    Optional<Tag> findByName(String tagName);
}
