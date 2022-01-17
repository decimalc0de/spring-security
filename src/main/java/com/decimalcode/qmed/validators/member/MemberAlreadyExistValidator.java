package com.decimalcode.qmed.validators.member;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.decimalcode.qmed.api.users.services.UserServiceImpl;
import com.decimalcode.qmed.validators.ContextProvider;

public class MemberAlreadyExistValidator implements ConstraintValidator<MemberAlreadyExist, String> {

    private UserServiceImpl userService;

    @Override
    public void initialize(MemberAlreadyExist constraintAnnotation) {
        userService = (UserServiceImpl) ContextProvider.getBean(UserServiceImpl.class);
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
