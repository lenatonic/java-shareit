package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long id, UserPatchDto user);

    Long deleteUser(Long id);

    List<UserDto> findUsers();

    boolean isUserExistById(Long id);

    boolean isUserExistByEmail(String email);

    UserDto findUserById(Long id);
}
