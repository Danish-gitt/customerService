package com.danish.chargingservice.exception;

public class ChargingAlreadyDoneException extends RuntimeException {
    public ChargingAlreadyDoneException(String message) {
        super(message);
    }
}
