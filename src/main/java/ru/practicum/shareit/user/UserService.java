package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    /**
     * Create a new User
     *
     * @param user
     * @return new User
     */
    User createUser(User user);

    /**
     * Updates the user
     *
     * @param user
     * @return updated User
     */
    User updateUser(User user);

    /**
     * Returns a User by id
     *
     * @param id
     * @return User or null if no object exists
     */
    User getUser(Long id);

    /**
     * Returns a collection all users
     *
     * @return User or null if no object exists
     */
    Collection<User> getUsers();

    /**
     * Delete a User by id
     *
     * @param userId
     */
    void deleteUser(Long userId);
}
