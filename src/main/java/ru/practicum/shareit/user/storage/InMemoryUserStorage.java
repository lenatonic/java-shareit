package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long id, UserPatchDto user) {
        User updateUser = findUser(id);
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        return updateUser;
    }

    @Override
    public Long deleteUser(Long id) {
        users.remove(id);
        return id;
    }

    @Override
    public List<User> findUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        boolean answer = false;
        if (!users.isEmpty()) {
            Optional<User> userSearch = users.values()
                    .stream()
                    .filter(a -> a.getEmail().equals(email))
                    .findFirst();
            if (userSearch.isPresent()) {
                answer = true;
            }
        }
        return answer;
    }

    @Override
    public boolean isUserExistById(Long id) {
        if (users.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User findUser(Long id) {
        User findedUser = users.get(id);
        return findedUser;
    }
}