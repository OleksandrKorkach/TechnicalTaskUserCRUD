package com.example.technicaltaskclearsolutions.services.impl;

import com.example.technicaltaskclearsolutions.dto.UserRegisterDto;
import com.example.technicaltaskclearsolutions.exceptions.custom.ParametersNotValidException;
import com.example.technicaltaskclearsolutions.exceptions.custom.UserNotFoundException;
import com.example.technicaltaskclearsolutions.exceptions.custom.ValidationException;
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

@Service
public class UserServiceImpl implements UserService {

    @Value("${minUserAge}")
    private int minUserAge;

    List<User> usersDatabase = new ArrayList<>();

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
    public User createUser(UserRegisterDto userRegisterDto) {
        User user = UserRegisterDto.toUser(userRegisterDto);
        validateUser(user);
        usersDatabase.add(user);
        return user;
    }

    private void validateUser(User user){
        int age = getUserAge(user);
        if (!(age >= minUserAge)){
            throw new ValidationException("Age must be above 18!");
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
    public User partialUpdateUser(Long id, UserRegisterDto updates) {
        User user = getUser(id);
        usersDatabase.remove(user);
        setUpdatedFields(updates, user);
        usersDatabase.add(user);
        return user;
    }

    public void setUpdatedFields(UserRegisterDto updates, User user){
        if (updates.getEmail() != null){
            user.setEmail(updates.getEmail());
        }
        if (updates.getFirstName() != null){
            user.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null){
            user.setLastName(updates.getLastName());
        }
        if (updates.getBirthDate() != null){
            user.setBirthDate(updates.getBirthDate());
        }
        if (updates.getAddress() != null){
            user.setAddress(updates.getAddress());
        }
        if (updates.getPhoneNumber() != null){
            user.setPhoneNumber(updates.getPhoneNumber());
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

}
