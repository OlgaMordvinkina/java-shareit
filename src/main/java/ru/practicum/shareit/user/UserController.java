package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return service.createUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@Valid @PathVariable Long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        return service.updateUser(userDto);
    }

    @GetMapping(value = "/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return service.getUser(userId);
    }

    @GetMapping()
    public Collection<UserDto> getUsers() {
        return service.getUsers();
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        service.deleteUser(userId);
    }
}
