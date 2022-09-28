package com.api.parkingcontrol.annotations;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.api.parkingcontrol.services.ParkingSpotService;

@RequiredArgsConstructor
public class LicensePlateCarValidator implements ConstraintValidator<LicensePlateCarValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String licensePlateCar, ConstraintValidatorContext context) {
        return !parkingSpotService.existsByLicensePlateCar(licensePlateCar);
    }
}
