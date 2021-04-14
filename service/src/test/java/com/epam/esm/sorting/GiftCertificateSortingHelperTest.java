package com.epam.esm.sorting;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateSortingHelperTest {

    @InjectMocks
    private GiftCertificateSortingHelper sortingHelper;

    private static GiftCertificate entity1;
    private static GiftCertificate entity2;
    private static GiftCertificate entity3;

    private static String[] sortingFields;

    @BeforeAll
    static void init() {
        entity1 = new GiftCertificate();
        entity1.setId(100L);
        entity1.setName("testCertificate");
        entity1.setDescription("description");
        entity1.setPrice(new BigDecimal("1.5"));
        entity1.setDuration(10);

        entity2 = new GiftCertificate();
        entity2.setId(200L);
        entity2.setName("testCertificate");
        entity2.setDescription("description");
        entity2.setPrice(new BigDecimal("1.5"));
        entity2.setDuration(10);

        entity3 = new GiftCertificate();
        entity3.setId(300L);
        entity3.setName("testCertificate");
        entity3.setDescription("description");
        entity3.setPrice(new BigDecimal("1.5"));
        entity3.setDuration(10);

        sortingFields = new String[]{"id"};
    }


    @ParameterizedTest
    @MethodSource("provideParams")
    public void testGetSorted_SortingDifferentOrders_ReturnListOfAscSortedEntities(List<GiftCertificate> entities, List<GiftCertificate> expected, String[] order) {
        //when
        List<GiftCertificate> result = sortingHelper.getSorted(sortingFields, order, entities);
        //then
        assertEquals(result, expected);
    }


    private static Stream<Arguments> provideParams() {
        return Stream.of(
                Arguments.of(Arrays.asList(entity3, entity2, entity1), Arrays.asList(entity1, entity2, entity3), new String[]{"ASC"}),
                Arguments.of(Arrays.asList(entity3, entity2, entity1), Arrays.asList(entity1, entity2, entity3), null),
                Arguments.of(Arrays.asList(entity3, entity2, entity1), Arrays.asList(entity1, entity2, entity3), new String[]{"IncorrectOrder"}),
                Arguments.of(Arrays.asList(entity1, entity2, entity3), Arrays.asList(entity3, entity2, entity1), new String[]{"DESC"})
        );
    }


}
