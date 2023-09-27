package com.example.technicaltaskclearsolutions.controllers;

import com.example.technicaltaskclearsolutions.dto.UserRegisterDto;
import com.example.technicaltaskclearsolutions.exceptions.custom.ValidationException;
import com.example.technicaltaskclearsolutions.models.User;
import com.example.technicaltaskclearsolutions.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(
            @RequestParam(name = "from", required = false, defaultValue = "18") Long minAge,
            @RequestParam(name = "to", required = false, defaultValue = "120") Long maxAge) {
        return userService.getUsers(minAge, maxAge);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid final UserRegisterDto newUser,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException("Validation not passed!");
        }
        return userService.createUser(newUser);
    }

    @PatchMapping("/{id}")
    public User partialUpdateUser(@PathVariable Long id,
                                  @RequestBody UserRegisterDto updates) {
        return userService.partialUpdateUser(id, updates);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody @Valid UserRegisterDto updatedUser,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException("Validation not passed!");
        }
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
