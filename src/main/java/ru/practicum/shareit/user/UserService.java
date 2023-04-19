package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.UserDto;

import java.util.Collection;

public interface UserService {
    /**
     * Create a new UserDto
     *
     * @param userDto
     * @return new UserDto
     */
    UserDto createUser(UserDto userDto);

    /**
     * Updates the userDto
     *
     * @param userDto
     * @return updated User
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Returns a UserDto by id
     *
     * @param id
     * @return UserDto or null if no object exists
     */
    UserDto getUser(Long id);

    /**
     * Returns a collection all userDro
     *
     * @return UserDto or null if no object exists
     */
    Collection<UserDto> getUsers();

    /**
     * Delete a UserDto by id
     *
     * @param userId
     */
    void deleteUser(Long userId);
}
