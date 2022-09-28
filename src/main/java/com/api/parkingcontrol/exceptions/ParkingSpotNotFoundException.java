package com.api.parkingcontrol.exceptions;

public class ParkingSpotNotFoundException extends RuntimeException{
    public ParkingSpotNotFoundException(String message) {
        super(message);
    }
}
