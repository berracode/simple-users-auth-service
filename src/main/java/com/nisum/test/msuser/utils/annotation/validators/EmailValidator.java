package com.nisum.test.msuser.utils.annotation.validators;

import com.nisum.test.msuser.utils.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Value("${user-props.re-email}")
    private String emailRegex;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && email.matches(emailRegex);
    }
}