package com.epam.esm.validator;

/**
 * This interface represents an api to check existence/non-existence of entity in db.
 * Implementations : {@link com.epam.esm.validator.GiftCertificateValidator},
 * {@link com.epam.esm.validator.TagValidator},
 * {@link com.epam.esm.validator.UserValidator} classes.
 *
 * @since 1.0
 */
public interface EntityValidator<T> {

    /**
     * This method attempts to get an entity from db by it's id.
     *
     * @param id id of the entity.
     * @return Entity.
     * @since 1.0
     */
    T validateAndFindByIdIfExist(long id);

    /**
     * This method checks if an entity with given name exists in db.
     *
     * @param name name of the entity.
     * @since 1.0
     */
    void validateIfEntityWithGivenNameExist(String name);

    /**
     * This method checks if an entity with given name and another id is present in db.
     *
     * @param name name of entity
     * @param id   id of entity
     */
    void validateIfAnotherEntityWithGivenNameExist(String name, long id);


}
