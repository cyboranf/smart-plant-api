package com.example.smartplantbuddy.exception.register;

public class PasswordNotMatchingException extends RuntimeException {
    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
