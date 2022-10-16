package com.api.parkingcontrol.annotations;

import com.api.parkingcontrol.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class LicensePlateCarValidator implements ConstraintValidator<LicensePlateCarValidation, String> {
    private final ParkingSpotService parkingSpotService;

    @Override
    public boolean isValid(String licensePlateCar, ConstraintValidatorContext context) {
        return !parkingSpotService.existsByLicensePlateCar(licensePlateCar);
    }
}
