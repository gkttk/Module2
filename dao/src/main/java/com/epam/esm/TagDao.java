package com.epam.esm;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Tag getById(long id);

    List<Tag> findAll();

    void save(Tag tag);

    void update(Tag tag);

    void delete(long id);

}
