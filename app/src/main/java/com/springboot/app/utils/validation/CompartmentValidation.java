package com.springboot.app.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CompartmentValidator.class)
public @interface CompartmentValidation {
    String message() default "Compartment type is invalid, you can choose a compartment which starts with a letter and then follows digits";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
