package com.decimalcode.qmed.validators.password;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {
    @Override
    public void initialize(PasswordStrength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password != null) {
            if (password.isBlank() || (password.length() < 6)) {
                String errorMessage = "Password strength is weak";
                /*
                 * Clear @UserNameValidation default message
                 */
                context.disableDefaultConstraintViolation();
                /*
                 * Add new message to @UserNameValidation
                 */
                context.buildConstraintViolationWithTemplate(errorMessage)
                        .addConstraintViolation();
                return false;
            }
            else return true;
        }

        return false;
    }
    // String passwordRegex = "^(?=.*[0-9])(?=.*[A-Za-z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{6,20}$";

}
