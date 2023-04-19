package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.RegisterException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long userId = 0L;

    @Override
    public User createUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new RegisterException("User с таким email уже есть.");
        }
        user.setId(++userId);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        User user = users.get(newUser.getId());

        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }

        String newEmail = newUser.getEmail();
        if (newEmail != null) {
            if (emails.contains(newEmail) && !Objects.equals(user.getEmail(), newUser.getEmail())) {
                throw new RegisterException("User с таким email уже есть.");
            }
            emails.remove(user.getEmail());
            emails.add(newEmail);
            user.setEmail(newEmail);
        }

        users.put(newUser.getId(), user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void deleteUser(Long id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }
}
