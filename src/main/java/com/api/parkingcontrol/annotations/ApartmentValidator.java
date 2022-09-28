package com.api.parkingcontrol.annotations;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.api.parkingcontrol.services.ParkingSpotService;

@RequiredArgsConstructor
public class ApartmentValidator implements ConstraintValidator<ApartmentValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String apartment, ConstraintValidatorContext context) {
        return !parkingSpotService.existsApartament(apartment);
    }
}
