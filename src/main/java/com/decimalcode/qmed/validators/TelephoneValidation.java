package com.decimalcode.qmed.validators;

import org.apache.logging.log4j.util.Strings;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@SuppressWarnings("unused")
@Constraint(validatedBy= TelephoneValidation.TelephoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TelephoneValidation {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "User name most contain at least 3 characters";

    class TelephoneValidator implements ConstraintValidator<TelephoneValidation, String> {

        @Override
        public void initialize(TelephoneValidation constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if(value == null) return false;
            String telephoneRegex = "\\d{8,13}";
            return !Strings.isBlank(value) && value.matches(telephoneRegex);
        }

    }

}
