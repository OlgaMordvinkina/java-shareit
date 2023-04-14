package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.exceptions.RegisterException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 0L;

    @Override
    public User createUser(User user) {
        emailAvailabilityCheck(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundUserException(user.getId());
        }
        emailAvailabilityCheck(user);
        User newUser = users.get(user.getId());
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        users.put(user.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundUserException(id);
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private void emailAvailabilityCheck(User user) {
        for (User value : users.values()) {
            if (!Objects.equals(value.getId(), user.getId()) && Objects.equals(value.getEmail(), user.getEmail())) {
                throw new RegisterException("User с таким email уже есть.");
            }
        }
    }
}
