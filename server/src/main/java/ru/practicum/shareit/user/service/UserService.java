package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto user);

    Long deleteUser(Long id);

    List<UserDto> findUsers();

    UserDto findUserById(Long id);
}
