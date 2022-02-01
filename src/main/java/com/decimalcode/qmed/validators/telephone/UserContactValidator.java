package com.decimalcode.qmed.validators.telephone;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserContactValidator implements ConstraintValidator<UserContact, String> {

    @Override
    public void initialize(UserContact constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return false;
        String telephoneRegex = "\\d{8,13}";
        return !Strings.isBlank(value) && value.matches(telephoneRegex);
    }

}
