package com.example.technicaltaskclearsolutions.exceptions.custom;

public class UserNotValidException extends RuntimeException{

    public UserNotValidException(String message){
        super(message);
    }
}
