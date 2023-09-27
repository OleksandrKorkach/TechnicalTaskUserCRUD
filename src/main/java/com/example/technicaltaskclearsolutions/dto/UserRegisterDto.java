package com.example.technicaltaskclearsolutions.dto;

import com.example.technicaltaskclearsolutions.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class UserRegisterDto {

    @Email
    @NotNull
    @NotBlank
    private final String email;

    @NotNull
    private final String firstName;

    @NotNull
    private final String lastName;

    @Past
    @NotNull
    private final LocalDate birthDate;

    @NotNull
    private final String address;

    @NotNull
    private final String phoneNumber;

    public UserRegisterDto(@Email @NotNull String email,
                           @NotNull String firstName,
                           @NotNull String lastName,
                           @Past LocalDate birthDate,
                           @NotNull String address,
                           @NotNull String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public static User toUser(UserRegisterDto dto) {
        return new User(
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate(),
                dto.getAddress(),
                dto.getPhoneNumber()
        );
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
