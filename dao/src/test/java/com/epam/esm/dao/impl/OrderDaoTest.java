package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.OrderDao;
import com.epam.esm.entity.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class OrderDaoTest {


    private final OrderDao orderDao;
    private final CriteriaFindAllDao<Order> criteriaFindAllDao;

    @Autowired
    public OrderDaoTest(OrderDao orderDao,
                        @Qualifier("orderCriteriaFindAllDao") CriteriaFindAllDao<Order> criteriaFindAllDao) {
        this.orderDao = orderDao;
        this.criteriaFindAllDao = criteriaFindAllDao;
    }

    private static Order order1;

    @BeforeAll
    static void init() {
        order1 = new Order();
        order1.setId(1L);
        order1.setCost(new BigDecimal("300.00"));
    }


    @Test
    public void testCount_ShouldReturnNumberOfEntity_WhenThereAreEntitiesInDb() {
        //given
        long expectedResult = 4L;
        //when
        long result = orderDao.count();
        //then
        assertEquals(result, expectedResult);
    }

    @Test
    public void testFindById_EntityWithGivenId_WhenEntityWithGivenIdIsPresentInDb() {
        //given
        long testId = order1.getId();
        //when
        Optional<Order> resultOpt = orderDao.findById(testId);
        //then
        assertTrue(resultOpt.isPresent());
        resultOpt.ifPresent(result -> assertAll(() -> assertEquals(result.getId(), testId),
                () -> assertEquals(result.getCost(), order1.getCost())));
    }

    @Test
    public void testFindById_EmptyOptional_WhenEntityWithGivenIdIsNotPresentInDb() {
        //given
        long testId = -1L;

        //when
        Optional<Order> resultOpt = orderDao.findById(testId);
        //then
        assertFalse(resultOpt.isPresent());
    }

    @Test
    @Rollback
    public void testSave_Entity() {
        //given
        Order savingOrder = new Order();
        savingOrder.setCost(new BigDecimal("80.00"));
        //when
        Order resultOrder = orderDao.save(savingOrder);
        //then
        assertAll(() -> assertNotNull(resultOrder.getId()),
                () -> assertEquals(resultOrder.getCost(), savingOrder.getCost()));
    }

    @Test
    @Rollback
    public void testDeleteById_NotThrowExceptions_WhenEntityWasDeleted() {
        //given
        long testId = order1.getId();
        //when
        //then
        assertDoesNotThrow(() -> orderDao.deleteById(testId));
    }

    @Test
    @Rollback
    public void testDeleteById_ThrowExceptions_WhenEntityWithGivenIdWasNotFound() {
        //given
        long testId = -1;
        //when
        //then
        assertThrows(EmptyResultDataAccessException.class, () -> orderDao.deleteById(testId));
    }

    @Test
    public void testFindBy_ListOfAllEntities_WhenEntityAreExistInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        int expectedSize = 4;
        //when
        List<Order> results = criteriaFindAllDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }

    @Test
    public void testFindBy_ListOfAllEntitiesWithGivenUserId_WhenEntityWithGivenUserIdAreExistInDb() {
        //given
        Map<String, String[]> reqParams = Collections.singletonMap(ApplicationConstants.USER_ID_KEY, new String[]{"1"});

        int expectedSize = 1;
        //when
        List<Order> results = criteriaFindAllDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedSize);
    }


}
