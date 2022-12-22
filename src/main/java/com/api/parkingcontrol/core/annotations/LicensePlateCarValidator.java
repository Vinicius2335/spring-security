package com.api.parkingcontrol.core.annotations;

import com.api.parkingcontrol.domain.service.implementation.ParkingSpotServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class LicensePlateCarValidator implements ConstraintValidator<LicensePlateCarValidation, String> {
    private final ParkingSpotServiceImpl parkingSpotServiceImpl;

    @Override
    public boolean isValid(String licensePlateCar, ConstraintValidatorContext context) {
        return !parkingSpotServiceImpl.existsByLicensePlateCar(licensePlateCar);
    }
}
