package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping()
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users");
        return client.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users");
        return client.updateUser(userId, userDto);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{userId}");
        return client.getUser(userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос GET /users");
        return client.getUsers();
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /users/{userId}");
        return client.deleteUser(userId);
    }
}
