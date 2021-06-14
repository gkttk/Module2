package com.epam.esm.dao.impl;


import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.config.DaoTestConfig;
import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class UserDaoTest {


    private final UserDao userDao;
    private final CriteriaFindAllDao<User> criteriaFindAllDao;

    @Autowired
    public UserDaoTest(UserDao userDao,
                       @Qualifier("userCriteriaFindAllDao") CriteriaFindAllDao<User> criteriaFindAllDao) {
        this.userDao = userDao;
        this.criteriaFindAllDao = criteriaFindAllDao;
    }

    private static User user3;
    private static User savingUser;


    @BeforeAll
    static void init() {
        user3 = new User();
        user3.setId(3L);
        user3.setLogin("u2Login");
        user3.setPassword("u2Pass");
        user3.setRole("USER");

        savingUser = new User();
        savingUser.setId(null);
        savingUser.setLogin("savingUserLogin");
        savingUser.setPassword("savingUserPass");
        savingUser.setRole("USER");
    }

    @Test
    public void testFindByLogin_ShouldReturnOptionalWithUser_WhenUserExistsInDb() {
        //given
        String login = "u2Login";
        //when
        Optional<User> result = userDao.findByLogin(login);
        //then
        assertTrue(result.isPresent());
        result.ifPresent(userResult -> assertEquals(userResult, user3));
    }

    @Test
    public void testFindByLogin_ShouldReturnEmptyOptional_WhenUserDoesNotExistInDb() {
        //given
        String login = "randomLogin";
        //when
        Optional<User> result = userDao.findByLogin(login);
        //then
        assertFalse(result.isPresent());
    }

    @Test
    public void testCount_ShouldReturnNumberOfEntity_WhenThereAreEntitiesInDb() {
        //given
        long expectedResult = 3L;
        //when
        long result = userDao.count();
        //then
        assertEquals(result, expectedResult);
    }


    @Test
    public void testFindBy_ListAllEntities_AtLeastOneEntityWithIsPresentInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        int expectedListSize = 3;
        //when
        List<User> results = criteriaFindAllDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedListSize);
    }

    @Test
    public void testFindBy_EmptyList_ThereAreNoEntitiesWithGivenRoleInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        reqParams.put(ApplicationConstants.ROLE_KEY, new String[]{"NewRole"});

        //when
        List<User> results = criteriaFindAllDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindBy_ListEntitiesByRoles_AtLeastOneEntityWithGivenRoleIsPresentInDb() {
        //given
        Map<String, String[]> reqParams = new HashMap<>();
        reqParams.put(ApplicationConstants.ROLE_KEY, new String[]{"USER"});
        int expectedListSize = 2;
        //when
        List<User> results = criteriaFindAllDao.findBy(reqParams, ApplicationConstants.MAX_LIMIT, ApplicationConstants.DEFAULT_OFFSET);
        //then
        assertFalse(results.isEmpty());
        assertEquals(results.size(), expectedListSize);
    }


    @Test
    @Rollback
    public void testSave_Entity() {
        //given
        //when
        User result = userDao.save(savingUser);
        //then
        assertAll(() -> assertNotNull(result.getId()),
                () -> assertEquals(result.getLogin(), savingUser.getLogin()),
                () -> assertEquals(result.getPassword(), savingUser.getPassword()),
                () -> assertEquals(result.getRole(), savingUser.getRole()));
    }


    @Test
    public void testFindByLogin_OptionalWithEntity_EntityWithGivenLoginIsPresentInDb() {
        //given
        String testLogin = user3.getLogin();
        //when
        Optional<User> result = userDao.findByLogin(testLogin);
        //then
        assertTrue(result.isPresent());
        result.ifPresent(user -> assertAll(
                () -> assertEquals(user.getId(), user3.getId()),
                () -> assertEquals(user.getLogin(), user3.getLogin()),
                () -> assertEquals(user.getPassword(), user3.getPassword()),
                () -> assertEquals(user.getRole(), user3.getRole())));
    }

    @Test
    public void testFindByLogin_EmptyOptional_EntityWithGivenLoginIsNotPresentInDb() {
        //given
        String testLogin = "notPresentLogin";
        //when
        Optional<User> result = userDao.findByLogin(testLogin);
        //then
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindById_OptionalWithEntity_EntityWithGivenIdIsPresentInDb() {
        //given
        long testId = user3.getId();
        //when
        Optional<User> result = userDao.findById(testId);
        //then
        assertTrue(result.isPresent());
        result.ifPresent(user -> assertAll(
                () -> assertEquals(user.getId(), user3.getId()),
                () -> assertEquals(user.getLogin(), user3.getLogin()),
                () -> assertEquals(user.getPassword(), user3.getPassword()),
                () -> assertEquals(user.getRole(), user3.getRole())));
    }

    @Test
    public void testFindById_EmptyOptional_EntityWithGivenIdIsNotPresentInDb() {
        //given
        long testId = -1;
        //when
        Optional<User> result = userDao.findById(testId);
        //then
        assertFalse(result.isPresent());
    }


}
