package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapper.toUser(userDto);
        log.info("Получен запрос POST /");
        log.info("Добавлен user: {}", user);
        return mapper.toUserDto(storage.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (storage.getUser(userDto.getId()) == null) {
            throw new NotFoundUserException(userDto.getId());
        }
        User user = mapper.toUser(userDto);
        log.info("Получен запрос PUT /users");
        log.info("Обновлен user: {}", user);
        return mapper.toUserDto(storage.updateUser(user));
    }

    @Override
    public UserDto getUser(Long id) {
        if (storage.getUser(id) == null) {
            throw new NotFoundUserException(id);
        }
        User user = storage.getUser(id);
        log.info("User c ID: {}", user);
        return mapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getUsers() {
        log.info("Получен запрос GET /users");
        Collection<UserDto> users = storage.getUsers().stream().map(mapper::toUserDto).collect(Collectors.toList());
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
