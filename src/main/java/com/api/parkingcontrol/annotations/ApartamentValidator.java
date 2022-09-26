package com.api.parkingcontrol.annotations;

import com.api.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class ApartamentValidator implements ConstraintValidator<ApartamentValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String apartament, ConstraintValidatorContext context) {
        return !parkingSpotService.existsApartament(apartament);
    }
}
