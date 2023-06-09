package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.еrror.exception.EmailAlreadyExistError;
import ru.practicum.shareit.еrror.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        if (!isUserExistByEmail(user.getEmail())) {
            log.debug("Создаём нового пользователя: name {}, e-mail {}.", user.getName(), user.getEmail());
            return userStorage.createUser(user);
        } else {
            throw new EmailAlreadyExistError("Пользователь с e-mail: " + user.getEmail() + " уже существует.");
        }
    }

    @Override
    public User updateUser(Long id, UserPatchDto user) {
        if (!isUserExistById(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует.");
        }
        if (isUserExistByEmail(user.getEmail()) && !findUserById(id).getEmail().equals(user.getEmail())) {
            throw new EmailAlreadyExistError(
                    "В системе уже существует другой пользователь с e-mail = " + user.getEmail());
        }
        log.debug("Редактируем данные пользователя id = {}.", id);
        return userStorage.updateUser(id, user);
    }

    @Override
    public Long deleteUser(Long id) {
        if (isUserExistById(id)) {
            log.debug("Удаляем пользователя id = {}.", id);
            return userStorage.deleteUser(id);
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }

    public UserDto findUserById(Long id) {
        if (isUserExistById(id)) {
            log.debug("Поиск данных по пользователю id = {}.", id);
            return userMapper.toUserDto(userStorage.findUser(id));
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
    }

    @Override
    public List<UserDto> findUsers() {
        List<UserDto> allUserDto = new ArrayList<>();
        log.debug("Вывод данных по всем существующим пользователям.");
        userStorage.findUsers()
                .stream()
                .forEach(a -> allUserDto.add(userMapper.toUserDto(a)));
        return allUserDto;
    }

    @Override
    public boolean isUserExistById(Long id) {
        log.debug("Проверка существования пользователя с id = {}.", id);
        return userStorage.isUserExistById(id);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        log.debug("Проверка существования пользователя с e-mail = {}.", email);
        return userStorage.isUserExistByEmail(email);
    }
}