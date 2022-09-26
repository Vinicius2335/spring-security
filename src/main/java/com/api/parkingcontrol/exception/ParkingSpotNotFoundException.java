package com.api.parkingcontrol.exception;

public class ParkingSpotNotFoundException extends RuntimeException{
    public ParkingSpotNotFoundException(String message) {
        super(message);
    }
}
