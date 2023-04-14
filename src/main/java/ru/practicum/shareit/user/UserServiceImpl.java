package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public User createUser(User user) {
        log.info("Получен запрос POST /");
        log.info("Добавлен user: {}", user);
        return storage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Получен запрос PUT /users");
        log.info("Обновлен user: {}", user);
        return storage.updateUser(user);
    }

    @Override
    public User getUser(Long id) {
        User user = storage.getUser(id);
        log.info("User c ID: {}", user);
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Получен запрос GET /users");
        Collection<User> users = storage.getUsers();
        log.info("Users в списке: {}", users.size());
        return users;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Получен запрос DELETE /users");
        log.info("Удалён user: {}", storage.getUser(userId));
        storage.deleteUser(userId);
    }
}
