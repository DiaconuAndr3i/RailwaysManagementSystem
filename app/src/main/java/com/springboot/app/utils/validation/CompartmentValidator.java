package com.springboot.app.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompartmentValidator implements ConstraintValidator<CompartmentValidation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        char[] array = s.toCharArray();
        boolean bool1 = Character.isLetter(array[0]), bool2 = true;
        for(int i = 1; i < array.length; i++){
            bool2 = bool2 && Character.isDigit(array[i]);
        }
        return bool1 && bool2;
    }
}
