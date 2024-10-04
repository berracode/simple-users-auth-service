package com.nisum.test.msuser.utils.annotation.validators;

import com.nisum.test.msuser.utils.annotation.ValidPass;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class PassValidator implements ConstraintValidator<ValidPass, String> {

    @Value("${user-props.re-pass}")
    private String passwordRegex;

    @Override
    public void initialize(ValidPass constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches(passwordRegex);
    }
}