package com.example.technicaltaskclearsolutions.services.impl;

import com.example.technicaltaskclearsolutions.dto.UserRegisterDto;
import com.example.technicaltaskclearsolutions.exceptions.custom.ParametersNotValidException;
import com.example.technicaltaskclearsolutions.exceptions.custom.UserNotFoundException;
import com.example.technicaltaskclearsolutions.exceptions.custom.UserNotValidException;
import com.example.technicaltaskclearsolutions.models.User;
import com.example.technicaltaskclearsolutions.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Value("${minUserAge}")
    private int minUserAge;

    List<User> usersDatabase = new ArrayList<>();


    private Long startId = 1L;
    @Override
    public User getUser(Long id) {
        Optional<User> user = usersDatabase.stream().filter(u -> u.getId().equals(id)).findFirst();
        if(user.isPresent()){
            return user.get();
        } else {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
    }

    @Override
    public List<User> getUsers(Long minAge, Long maxAge) {
        if (!(minAge < maxAge)){
            throw new ParametersNotValidException("Parameters not valid");
        }
        return usersDatabase.stream()
                .filter(user -> getUserAge(user) >= minAge && getUserAge(user) <= maxAge)
                .toList();
    }

    @Override
    public void createUser(User user) {
        validateUser(user);
        user.setId(startId);
        usersDatabase.add(user);
        startId += 1;
    }

    private void validateUser(User user){
        int age = getUserAge(user);
        if (!(age >= minUserAge)){
            throw new UserNotValidException("Age must be above 18!");
        }
        if (!isValidEmail(user.getEmail())){
            throw new UserNotValidException("Wrong email format!");
        }
        if (!user.getBirthDate().isBefore(LocalDate.now())){
            throw new UserNotValidException("Wrong birth date!");
        }
    }

    @Override
    public User updateUser(Long id, UserRegisterDto updatedUser) {
        User userBefore = getUser(id);
        User userAfter = UserRegisterDto.toUser(updatedUser);
        userAfter.setId(id);
        usersDatabase.remove(userBefore);
        usersDatabase.add(userAfter);
        return userAfter;
    }

    @Override
    public User partialUpdateUser(Long id, Map<String, Object> updates) {
        User user = getUser(id);
        usersDatabase.remove(user);
        updateFields(updates, user);
        usersDatabase.add(user);
        return user;
    }

    private void updateFields(Map<String, Object> updates, User user){
        for (String field: updates.keySet()){
            updateUserEmail(user, updates.get(field));
            updateUserFirstName(user, updates.get(field));
            updateUserLastName(user, updates.get(field));
            updateUserBirthDate(user, updates.get(field));
            updateUserAddress(user, updates.get(field));
            updateUserPhoneNumber(user, updates.get(field));
        }
    }

    private void updateUserEmail(User user, Object updatedValue) {
        if (updatedValue instanceof String) {
            user.setEmail((String) updatedValue);
        }
    }

    private void updateUserFirstName(User user, Object updatedValue) {
        if (updatedValue instanceof String) {
            user.setFirstName((String) updatedValue);
        }
    }

    private void updateUserLastName(User user, Object updatedValue) {
        if (updatedValue instanceof String) {
            user.setLastName((String) updatedValue);
        }
    }

    private void updateUserBirthDate(User user, Object updatedValue) {
        if (updatedValue instanceof LocalDate) {
            user.setBirthDate((LocalDate) updatedValue);
        }
    }

    private void updateUserAddress(User user, Object updatedValue) {
        if (updatedValue instanceof String) {
            user.setAddress((String) updatedValue);
        }
    }

    private void updateUserPhoneNumber(User user, Object updatedValue) {
        if (updatedValue instanceof String) {
            user.setPhoneNumber((String) updatedValue);
        }
    }

    @Override
    public void deleteUser(Long id) {
        usersDatabase.removeIf(user -> user.getId().equals(id));
    }

    public int getUserAge(User user){
        Period age = Period.between(user.getBirthDate(), LocalDate.now());
        return age.getYears();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
