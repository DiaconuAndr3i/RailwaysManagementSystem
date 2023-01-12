package com.springboot.app.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ListStationDtoValidator.class)
public @interface ListStationDtoValidation {
    String message() default "Name or city from the list of station cannot be null or empty";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
