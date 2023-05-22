package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {
    private final User user = new User(1L, "name", "email@mail.ru");
    private final UserDto userDto = new UserDto(1L, "name", "email@mail.ru");

    @Test
    void toUserDtoFromUser_returnedUserDto() {
        UserDto expectedUserDto = UserMapper.toUserDto(user);

        assertEquals(userDto, expectedUserDto);
    }

    @Test
    void toUserFromUserDto_returnedUser() {
        User expectedUser = UserMapper.toUser(userDto);

        assertEquals(user.toString(), expectedUser.toString());
    }
}
