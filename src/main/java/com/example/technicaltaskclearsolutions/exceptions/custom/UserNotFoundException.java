package com.example.technicaltaskclearsolutions.exceptions.custom;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
