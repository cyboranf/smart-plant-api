package com.example.smartplantbuddy.exception.plant;

public class PlantNotFoundException extends RuntimeException {
    public PlantNotFoundException(String message) {
        super(message);
    }
}
