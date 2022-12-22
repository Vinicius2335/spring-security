package com.api.parkingcontrol.core.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ParkingSpotNumberValidator.class)
public @interface ParkingSpotNumberValidation {
    String message() default "Invalid Parking Spot Number: is already in use!";
    Class<?>[]  groups() default {};
    Class<? extends Payload>[] payload() default {};
}
