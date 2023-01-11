package com.springboot.app.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EstimatedTotalTimeValidator implements ConstraintValidator<EstimatedTotalTimeValidation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(!s.matches("\\d+:\\d\\d")){
            return false;
        }
        List<String> list = List.of(s.split(":"));
        return Integer.parseInt(list.get(1)) < 60;
    }
}
