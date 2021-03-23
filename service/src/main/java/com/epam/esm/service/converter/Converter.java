package com.epam.esm.service.converter;

public interface Converter<E,D> {

    D convertToDto(E entity);

    E convertToEntity(D dto);
}
