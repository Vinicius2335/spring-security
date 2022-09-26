package com.api.parkingcontrol.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

// MethodArgumentNotValidException

@Documented
@Target({ FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LicensePlateCarValidator.class)
public @interface LicensePlateCarValidation {
    public String message() default "Invalid License Plate Car: is already in use!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
