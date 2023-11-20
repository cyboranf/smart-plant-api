package com.example.smartplantbuddy.exception.plant;

import java.io.IOException;

public class ImageEmptyException extends IOException {
    public ImageEmptyException(String message) {
        super(message);
    }
}
