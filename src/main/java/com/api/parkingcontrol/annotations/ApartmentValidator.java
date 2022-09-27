package com.api.parkingcontrol.annotations;

import com.api.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ApartmentValidator implements ConstraintValidator<ApartmentValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String apartment, ConstraintValidatorContext context) {
        return !parkingSpotService.existsApartament(apartment);
    }
}
