package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    private UserService service;
    private final Long userId = 1L;
    private final UserDto userOne = new UserDto(1L, "nameUser", "email@user.ru");

    @BeforeEach
    void BeforeEach() {
        service = new UserServiceImpl(repository);
    }

    @Test
    void createUser_UserFound_thenReturnedUser() {
        User user = UserMapper.toUser(userOne);

        when(repository.save(any())).thenReturn(user);

        UserDto expectedUser = service.createUser(userOne);

        assertEquals(userOne, expectedUser);
        verify(repository).save(any());
    }

    @Test
    void updateUser_UserFound_thenReturnedUser() {
        User user = UserMapper.toUser(userOne);
        user.setName("updateName");
        user.setEmail("update@email.ru");

        when(repository.save(any())).thenReturn(user);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto expectedUser = service.updateUser(userOne);

        assertEquals(userOne, expectedUser);
        verify(repository).save(any());
    }

    @Test
    void getUserById_UserFound_thenReturnedUser() {
        User user = UserMapper.toUser(userOne);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto expectedUser = service.getUser(userId);

        assertEquals(expectedUser, userOne);
    }

    @Test
    void getUserById_UserNotFound_thenReturnedNotFoundUserException() {
        when(repository.findById(anyLong())).thenThrow(new NotFoundUserException(userId));

        assertThrows(NotFoundUserException.class, () -> service.getUser(userId));
    }

    @Test
    void getAllUsers_ListUserDto() {
        List<UserDto> listDto = new ArrayList<>();

        when(service.getUsers()).thenReturn(listDto);

        List<UserDto> expectedUsers = service.getUsers();

        assertEquals(expectedUsers, listDto);
    }

    @Test
    void deleteUserTest() {
        service.deleteUser(userOne.getId());
        verify(repository).deleteById(anyLong());
    }
}
