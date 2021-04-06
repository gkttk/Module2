package com.epam.esm.validator;

public interface EntityValidator<T> {

    T findByIdIfExist(long id);

    void throwExceptionIfExistWithGivenName(String name);

}
