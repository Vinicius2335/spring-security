package com.api.parkingcontrol.core.annotations;

import com.api.parkingcontrol.domain.service.implementation.ParkingSpotServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ParkingSpotNumberValidator implements ConstraintValidator<ParkingSpotNumberValidation, String> {
    private final ParkingSpotServiceImpl parkingSpotServiceImpl;

    @Override
    public boolean isValid(String parkingSpotNumber, ConstraintValidatorContext context) {
        return !parkingSpotServiceImpl.existsByParkingSpotNumber(parkingSpotNumber);
    }
}
