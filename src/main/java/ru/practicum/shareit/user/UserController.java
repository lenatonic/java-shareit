package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable(name = "id") Long id,
                           @RequestBody UserPatchDto userPatchDto) {

        return userService.updateUser(id, userPatchDto);
    }

    @DeleteMapping("/{id}")
    public Long deleteUser(@PathVariable(name = "id") Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable(name = "id") Long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserDto> findUsers() {
        return userService.findUsers();
    }
}