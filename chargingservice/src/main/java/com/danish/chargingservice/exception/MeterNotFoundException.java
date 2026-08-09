package com.danish.chargingservice.exception;

public class MeterNotFoundException extends RuntimeException {
    public MeterNotFoundException(String message) {
        super(message);
    }
}
