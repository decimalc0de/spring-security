package com.decimalcode.qmed.validators.member;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidator.class)
public @interface UserName {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message() default "User name most contain at least 3 characters";
}
