package com.decimalcode.qmed.validators.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@SuppressWarnings("unused")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserPasswordValidator.class)
public @interface UserPassword {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message() default "Password field cannot be void";
}
