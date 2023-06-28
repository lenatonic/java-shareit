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
                || !emailPattern.matcher(userDto.getEmail()).matches()) {
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
}