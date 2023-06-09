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
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Добавлен user: {}", user);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User oldUser = repository.findById(userDto.getId()).orElseThrow(() -> new NotFoundUserException(userDto.getId()));

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUser.setEmail(userDto.getEmail());
        }
        log.info("Обновлен user: {}", oldUser);
        return UserMapper.toUserDto(repository.save(oldUser));
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
        List<UserDto> users = repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Users в списке: {}", users.size());
        return users;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удалён user: {}", repository.findById(userId));
        repository.deleteById(userId);
    }
}
