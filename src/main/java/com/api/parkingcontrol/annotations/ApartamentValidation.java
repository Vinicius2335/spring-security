package com.api.parkingcontrol.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = ApartamentValidator.class)
public @interface ApartamentValidation {
    String message() default "Invalid Apartament: Parking Spot already in registered for this apartament";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
