package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        return saveUser(userDto, "Получен запрос POST /", "Добавлен user: {}");
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto) {
        User oldUser = repository.findById(userDto.getId()).orElseThrow(() -> new NotFoundUserException(userDto.getId()));

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUser.setEmail(userDto.getEmail());
        }
        return saveUser(UserMapper.toUserDto(oldUser), "Получен запрос PUT /users", "Обновлен user: {}");
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundUserException(id));
        log.info("User c ID: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers() {
        log.info("Получен запрос GET /users");
        List<UserDto> users = repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Users в списке: {}", users.size());
        return users;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Получен запрос DELETE /users");
        log.info("Удалён user: {}", repository.findById(userId));
        repository.deleteById(userId);
    }

    private UserDto saveUser(UserDto userDto, String logMethod, String logOperation) {
        User user = UserMapper.toUser(userDto);
        log.info(logMethod);
        log.info(logOperation, user);
        return UserMapper.toUserDto(repository.save(user));
    }
}
