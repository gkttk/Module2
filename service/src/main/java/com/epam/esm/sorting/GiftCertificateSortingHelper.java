package com.epam.esm.sorting;

import com.epam.esm.constants.ApplicationConstants;
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

    /**
     * This method returns a sorted list of GiftCertificate entities.
     *
     * @param fields            fields to sort by.
     * @param order             sorting order. If {@param order} == null or not equals {{@link ApplicationConstants} DESC_ORDER then sorts by ascending order.
     * @param foundCertificates list of GiftCertificate entities for sorting.
     * @return sorted list of GiftCertificate entities.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getSorted(String[] fields, String order, List<GiftCertificate> foundCertificates) {
        Comparator<GiftCertificate> comparator = buildComparator(fields);

        return ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order) ? sortDesc(comparator, foundCertificates) : sortAsc(comparator, foundCertificates);

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
                        case ApplicationConstants.ID_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getId));
                            break;
                        }
                        case ApplicationConstants.NAME_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getName));
                            break;
                        }
                        case ApplicationConstants.DESCRIPTION_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getDescription));
                            break;
                        }
                        case ApplicationConstants.PRICE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getPrice));
                            break;
                        }
                        case ApplicationConstants.DURATION_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getDuration));
                            break;
                        }
                        case ApplicationConstants.CREATE_DATE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getCreateDate));
                            break;
                        }
                        case ApplicationConstants.LAST_UPDATE_DATE_FIELD: {
                            comparators.add(Comparator.comparing(GiftCertificate::getLastUpdateDate));
                            break;
                        }
                        default:
                            throw new GiftCertificateException(ApplicationConstants.INVALID_SORT_FIELD_ERROR_CODE, String.format("Can't sort GiftCertificates by the field: %s", field));
                    }
                });

        return comparators.stream().reduce(Comparator::thenComparing).get();
    }


}
