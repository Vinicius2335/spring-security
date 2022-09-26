package com.api.parkingcontrol.annotations;

import com.api.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ParkingSpotNumberValidator implements ConstraintValidator<ParkingSpotNumberValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String parkingSpotNumber, ConstraintValidatorContext context) {
        return !parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber);
    }
}
