package com.api.parkingcontrol.core.annotations;

import com.api.parkingcontrol.domain.service.implementation.ParkingSpotServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ApartmentValidator implements ConstraintValidator<ApartmentValidation, String> {
    private final ParkingSpotServiceImpl parkingSpotServiceImpl;

    @Override
    public boolean isValid(String apartment, ConstraintValidatorContext context) {
        return !parkingSpotServiceImpl.existsApartament(apartment);
    }
}
