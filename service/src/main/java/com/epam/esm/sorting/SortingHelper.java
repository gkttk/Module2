package com.epam.esm.sorting;

import java.util.List;


/**
 * This interface represents an api to sorting a list of object.
 * @param <T> type of object list
 *
 * @since 1.0
 */
public interface SortingHelper<T> {

    List<T> getSorted(String[] fields, String[] order, List<T> sortingObjects);
}
