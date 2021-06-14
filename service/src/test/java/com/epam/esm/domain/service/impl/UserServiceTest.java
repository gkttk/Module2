package com.epam.esm.domain.service.impl;

import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.dto.bundles.UserDtoBundle;
import com.epam.esm.domain.exceptions.UserException;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CriteriaFindAllDao<User> criteriaFindAllDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;
    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;

    @BeforeEach
    void init() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setLogin("Login");
        userDto.setPassword("Pass");
        userDto.setRole("ADMIN");
        user = new User();
        user.setId(1L);
        user.setLogin("Login");
        user.setPassword("Pass");
        user.setRole("ADMIN");
    }

    @Test
    public void testFindById_ReturnDto_EntityWithGivenIdIsPresentInDb() {
        //given
        long id = user.getId();
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        //when
        UserDto result = userService.findById(id);
        //then
        verify(userDao).findById(id);
        verify(modelMapper).map(user, UserDto.class);
        assertEquals(result, userDto);
    }

    @Test
    public void testFindById_ThrowException_EntityWithGivenIdIsPresentInDb() {
        //given
        long id = user.getId();
        when(userDao.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(UserException.class, () -> userService.findById(id));
        verify(userDao).findById(id);
    }

    @Test
    public void testFindAllForQuery_BundleOfDto_ThereAreEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(criteriaFindAllDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Arrays.asList(user, user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> expectedResult = Arrays.asList(userDto, userDto);
        long expectedSize = 2;
        when(userDao.count()).thenReturn(expectedSize);
        UserDtoBundle expectedBundle = new UserDtoBundle(expectedResult, expectedSize);
        //when
        UserDtoBundle result = userService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedBundle);
        verify(criteriaFindAllDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(modelMapper, times(2)).map(user, UserDto.class);
        verify(userDao).count();
    }

    @Test
    public void testFindAllForQuery_BundleWithoutDto_ThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(criteriaFindAllDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Collections.emptyList());

        List<UserDto> expectedResult = Collections.emptyList();
        long expectedSize = 0L;
        when(userDao.count()).thenReturn(expectedSize);
        UserDtoBundle expectedBundle = new UserDtoBundle(expectedResult, expectedSize);
        //when
        UserDtoBundle result = userService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedBundle);
        verify(criteriaFindAllDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(userDao).count();
    }

    @Test
    public void testSave_ReturnDto_WhenUserWithGivenNameDoesNotExistInDb() {
        //given
        String login = userDto.getLogin();
        when(userDao.findByLogin(login)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("123");
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        //when
        UserDto result = userService.save(userDto);
        //then
        assertEquals(result, userDto);
        verify(userDao).findByLogin(login);
        verify(passwordEncoder).encode(anyString());
        verify(modelMapper).map(userDto, User.class);
        verify(userDao).save(user);
        verify(modelMapper).map(user, UserDto.class);
    }

    @Test
    public void testSave_ThrowException_WhenUserWithGivenNameAlreadyExistInDb() {
        //given
        String login = userDto.getLogin();
        when(userDao.findByLogin(login)).thenReturn(Optional.of(user));
        //when
        //then
        assertThrows(UserException.class, () -> userService.save(userDto));
        verify(userDao).findByLogin(login);
    }


}
