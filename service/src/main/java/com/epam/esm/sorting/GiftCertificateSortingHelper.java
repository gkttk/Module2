package com.epam.esm.sorting;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exceptions.GiftCertificateException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link com.epam.esm.sorting.SortingHelper} interface for {@link GiftCertificate} entity.
 *
 * @since 1.0
 */
@Component
public class GiftCertificateSortingHelper implements SortingHelper<GiftCertificate> {


    private final static String ID_FIELD = "id";
    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "description";
    private final static String PRICE_FIELD = "price";
    private final static String DURATION_FIELD = "duration";
    private final static String CREATE_DATE_FIELD = "createDate";
    private final static String LAST_UPDATE_DATE_FIELD = "lastUpdateDate";
    private final static String DESC_ORDER = "desc";
    private final static int INVALID_SORT_FIELD_ERROR_CODE = 44601;


    /**
     * This method returns a sorted list of GiftCertificate entities.
     *
     * @param fields            fields to sort by.
     * @param order             sorting order. If {@param order} == null or not equals {{@link #DESC_ORDER}} then sorts by ascending order.
     * @param foundCertificates list of GiftCertificate entities for sorting.
     * @return sorted list of GiftCertificate entities.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getSorted(String[] fields, String order, List<GiftCertificate> foundCertificates) {
        Comparator<GiftCertificate> comparator = buildComparator(fields);

        return DESC_ORDER.equalsIgnoreCase(order) ? sortDesc(comparator, foundCertificates) : sortAsc(comparator, foundCertificates);

    }

    /**
     * This method return a list of GiftCertificate entities sorted by ascending order.
     *
     * @param comparator        built comparator for sorting.
     * @param foundCertificates list of GiftCertificate entities for sorting.
     * @return sorted list of GiftCertificate entities by ascending order.
     * @since 1.0
     */
    private List<GiftCertificate> sortAsc(Comparator<GiftCertificate> comparator, List<GiftCertificate> foundCertificates) {
        return foundCertificates.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * This method return a list of GiftCertificate entities sorted by descending order.
     *
     * @param comparator        built comparator for sorting.
     * @param foundCertificates list of GiftCertificate entities for sorting.
     * @return sorted list of GiftCertificate entities by descending order.
     * @since 1.0
     */
    private List<GiftCertificate> sortDesc(Comparator<GiftCertificate> comparator, List<GiftCertificate> foundCertificates) {
        return foundCertificates.stream().sorted(comparator.reversed()).collect(Collectors.toList());

    }

    /**
     * This method builds comparator according to passed fields for sorting.
     *
     * @param fields fields for sorting.
     * @return built comparator for sorting.
     * @throws GiftCertificateException when the {@param fields} array contains incorrect value of field.
     */
    private Comparator<GiftCertificate> buildComparator(String[] fields) {

        List<Comparator<GiftCertificate>> comparators = new ArrayList<>();

        Arrays.stream(fields)
                .forEach(field -> {
                    switch (field) {
                        case ID_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getId));
                            break;
                        }
                        case NAME_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getName));
                            break;
                        }
                        case DESCRIPTION_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getDescription));
                            break;
                        }
                        case PRICE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getPrice));
                            break;
                        }
                        case DURATION_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getDuration));
                            break;
                        }
                        case CREATE_DATE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getCreateDate));
                            break;
                        }
                        case LAST_UPDATE_DATE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getLastUpdateDate));
                            break;
                        }
                        default:
                            throw new GiftCertificateException(INVALID_SORT_FIELD_ERROR_CODE, String.format("Can't sort GiftCertificates by the field: %s", field));
                    }
                });

        return comparators.stream().reduce(Comparator::thenComparing).get();
    }


}
