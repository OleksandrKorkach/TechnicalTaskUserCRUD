package com.example.technicaltaskclearsolutions.exceptions.custom;

public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
    }
}
