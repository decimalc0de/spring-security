package com.decimalcode.qmed.validators;

import org.apache.logging.log4j.util.Strings;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@SuppressWarnings("unused")
@Constraint(validatedBy= EmailValidation.EmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface EmailValidation {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Invalid email address";

    class EmailValidator implements ConstraintValidator<EmailValidation, String> {

        @Override
        public void initialize(EmailValidation constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if(value == null) return false;
            String emailRegex = "(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
            return !Strings.isBlank(value) && value.matches(emailRegex);
        }

    }

}
