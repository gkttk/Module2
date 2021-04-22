package com.epam.esm.assemblers;

import org.springframework.hateoas.CollectionModel;

public interface ModelAssembler<T> {

    T toModel(T dto);

    CollectionModel<T> toCollectionModel(Iterable<? extends T> entities, Integer offset);

}
