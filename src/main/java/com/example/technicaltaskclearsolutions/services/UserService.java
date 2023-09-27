package com.example.technicaltaskclearsolutions.services;

import com.example.technicaltaskclearsolutions.dto.UserRegisterDto;
import com.example.technicaltaskclearsolutions.models.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserService {
    User getUser(Long id);

    List<User> getUsers(Long minAge, Long maxAge);

    void createUser(User user);

    User updateUser(Long id, UserRegisterDto updatedUser);

    User partialUpdateUser(Long id, Map<String, Object> updates);

    void deleteUser(Long id);
}
