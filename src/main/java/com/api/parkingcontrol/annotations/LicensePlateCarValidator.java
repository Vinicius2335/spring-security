package com.api.parkingcontrol.annotations;

import com.api.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class LicensePlateCarValidator implements ConstraintValidator<LicensePlateCarValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String licensePlateCar, ConstraintValidatorContext context) {
        return !parkingSpotService.existsByLicensePlateCar(licensePlateCar);
    }
}
