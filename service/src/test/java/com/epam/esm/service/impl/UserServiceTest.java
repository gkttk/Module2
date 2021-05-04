package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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
    public void testFindAllForQuery_ListOfDto_ThereAreEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(userDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Arrays.asList(user, user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> expectedResult = Arrays.asList(userDto, userDto);
        //when
        List<UserDto> result = userService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(userDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(modelMapper, times(2)).map(user, UserDto.class);
    }

    @Test
    public void testFindAllForQuery_EmptyList_ThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(userDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Collections.emptyList());

        List<UserDto> expectedResult = Collections.emptyList();
        //when
        List<UserDto> result = userService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(userDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
    }

    @Test
    public void testSave_ReturnDto_WhenUserWithGivenNameDoesNotExistInDb() {
        //given
        String login = userDto.getLogin();
        when(userDao.findByLogin(login)).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        //when
        UserDto result = userService.save(userDto);
        //then
        assertEquals(result, userDto);
        verify(userDao).findByLogin(login);
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
        assertThrows(UserException.class, ()-> userService.save(userDto));
        verify(userDao).findByLogin(login);
    }


}
