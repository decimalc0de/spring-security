package com.decimalcode.qmed.validators.member;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.decimalcode.qmed.api.users.service.UserServiceImpl;
import com.decimalcode.qmed.config.ApplicationContextImpl;

public class UserNameValidator implements ConstraintValidator<UserName, String> {

    private UserServiceImpl userService;

    @Override
    public void initialize(UserName constraintAnnotation) {
        userService = ApplicationContextImpl.getBean(UserServiceImpl.class);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        boolean userNameIsValid = false;
        if(username != null)
            if(!username.isBlank())
                if(username.length()>2)
                {
                    userNameIsValid = !userService.usernameAlreadyExist(username);
                    if(!userNameIsValid)
                    {
                        String errorMessage = "User name is already taken";
                        /*
                        * Clear @UserNameValidation default message
                        */
                        context.disableDefaultConstraintViolation();
                        /*
                        * Add new message to @UserNameValidation
                        */
                        context.buildConstraintViolationWithTemplate(errorMessage)
                                .addConstraintViolation();
                    }
                }
        return userNameIsValid;

    }

}
