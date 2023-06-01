package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users");
        return service.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@Valid @PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users");
        userDto.setId(userId);
        return service.updateUser(userDto);
    }

    @GetMapping(value = "/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{userId}");
        return service.getUser(userId);
    }

    @GetMapping()
    public Collection<UserDto> getUsers() {
        log.info("Получен запрос GET /users");
        return service.getUsers();
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /users/{userId}");
        service.deleteUser(userId);
    }
}
