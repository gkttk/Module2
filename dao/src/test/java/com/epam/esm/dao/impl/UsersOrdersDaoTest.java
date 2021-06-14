package com.epam.esm.dao.impl;

import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.dao.relation.UsersOrdersDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class UsersOrdersDaoTest {

    private final UsersOrdersDao usersOrdersDao;
    private final UserDao userDao;

    @Autowired
    public UsersOrdersDaoTest(UsersOrdersDao usersOrdersDao, UserDao userDao) {
        this.usersOrdersDao = usersOrdersDao;
        this.userDao = userDao;
    }

    @Test
    @Rollback
    public void testSave_ShouldSaveRelation() {
        //given
        long userId = 1;
        long orderId = 2;
        //when
        usersOrdersDao.save(userId, orderId);
        //then
        Optional<User> userWithNewOrderOpt = userDao.findById(userId);
        userWithNewOrderOpt.ifPresent(user -> {
            List<Order> orders = user.getOrders();
            assertTrue(orders.stream().anyMatch(order -> order.getId().equals(orderId)));
        });
    }
}
