package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.IncorrectDateError;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()
                ||!emailPattern.matcher(userDto.getEmail()).matches()) {
            throw new IncorrectDateError("Поле email заполнено не верно.");
        }
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            throw new IncorrectDateError("Поле email заполнено не верно.");
        }
        User userCreated = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(userCreated);
    }

    @Override
    public Long deleteUser(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    @Override
    public List<UserDto> findUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.toUserDto(users);
    }

    @Override
    public boolean isUserExistById(Long id) {
        return false;
    }

//    @Override
//    public boolean isUserExistByEmail(String email) {
//        return false;
//    }

    @Override
    public UserDto findUserById(Long id) {
        User userFind = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователя с id = " + id + " не существует."));
        return UserMapper.toUserDto(userFind);
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        user.setId(id);
        User interimUser = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователя с id = " + id + " не существует."));
        UserDto updateUserDto = UserMapper.toUserDto(interimUser);
        User updateUser = UserMapper.toUpdateUser(updateUserDto, user);
        userRepository.up(updateUser.getName(), updateUser.getEmail(),
                id);
        return UserMapper.toUserDto(updateUser);
    }

//    private final UserStorage userStorage;
//    private final UserMapper userMapper;
//
//    @Override
//    public UserDto createUser(User user) {
//        if (!isUserExistByEmail(user.getEmail())) {
//            log.debug("Создаём нового пользователя: name {}, e-mail {}.", user.getName(), user.getEmail());
//            return userMapper.toUserDto(userStorage.createUser(user));
//        } else {
//            throw new EmailAlreadyExistError("Пользователь с e-mail: " + user.getEmail() + " уже существует.");
//        }
//    }
//
//    @Override
//    public UserDto updateUser(Long id, UserPatchDto user) {
//        if (!isUserExistById(id)) {
//            throw new NotFoundException("Пользователя с id = " + id + " не существует.");
//        }
//        if (isUserExistByEmail(user.getEmail()) && !findUserById(id).getEmail().equals(user.getEmail())) {
//            throw new EmailAlreadyExistError(
//                    "В системе уже существует другой пользователь с e-mail = " + user.getEmail());
//        }
//        log.debug("Редактируем данные пользователя id = {}.", id);
//        return userMapper.toUserDto(userStorage.updateUser(id, user));
//    }
//
//    @Override
//    public Long deleteUser(Long id) {
//        if (isUserExistById(id)) {
//            log.debug("Удаляем пользователя id = {}.", id);
//            return userStorage.deleteUser(id);
//        } else {
//            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
//        }
//    }
//
//    public UserDto findUserById(Long id) {
//        if (isUserExistById(id)) {
//            log.debug("Поиск данных по пользователю id = {}.", id);
//            return userMapper.toUserDto(userStorage.findUser(id));
//        } else {
//            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
//        }
//    }
//
//    @Override
//    public List<UserDto> findUsers() {
//        List<UserDto> allUserDto = new ArrayList<>();
//        log.debug("Вывод данных по всем существующим пользователям.");
//        userStorage.findUsers()
//                .stream()
//                .forEach(a -> allUserDto.add(userMapper.toUserDto(a)));
//        return allUserDto;
//    }
//
//    @Override
//    public boolean isUserExistById(Long id) {
//        log.debug("Проверка существования пользователя с id = {}.", id);
//        return userStorage.isUserExistById(id);
//    }
//
//    @Override
//    public boolean isUserExistByEmail(String email) {
//        log.debug("Проверка существования пользователя с e-mail = {}.", email);
//        return userStorage.isUserExistByEmail(email);
//    }
}