package com.epam.esm.service.converter;

public interface Converter<E,T> {

    T convertToDto(E entity);

    E convertToEntity(T dto);
}
