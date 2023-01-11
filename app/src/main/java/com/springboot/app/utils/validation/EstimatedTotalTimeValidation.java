package com.springboot.app.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EstimatedTotalTimeValidator.class)
public @interface EstimatedTotalTimeValidation {
    public String message() default "The field should be in the form: numberHours:numberMinutes (numberMinutes < 60). Example: 2:45";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
