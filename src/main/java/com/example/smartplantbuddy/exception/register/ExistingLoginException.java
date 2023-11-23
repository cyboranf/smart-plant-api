package com.example.smartplantbuddy.exception.register;

public class ExistingLoginException extends RuntimeException {
    public ExistingLoginException(String message) {
        super(message);
    }
}
