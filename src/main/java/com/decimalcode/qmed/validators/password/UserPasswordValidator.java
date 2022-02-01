package com.decimalcode.qmed.validators.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {
    @Override
    public void initialize(UserPassword constraintAnnotation) {
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
