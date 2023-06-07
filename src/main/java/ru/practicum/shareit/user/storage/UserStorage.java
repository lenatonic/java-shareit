package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(Long id, UserPatchDto userPatchDto);

    Long deleteUser(Long id);

    List<User> findUsers();

    boolean isUserExistByEmail(String email);

    boolean isUserExistById(Long id);

    User findUser(Long id);
}