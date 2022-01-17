package com.decimalcode.qmed.api.registration.services;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.api.users.services.UserServiceImpl;
import com.decimalcode.qmed.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    public ApiResponse<UserEntity> acceptValidatedRequest(RegistrationRequest registrationDTO) {
        /*
         * Map registration dto to user entity class using ModelMapper api
         */
        UserEntity newUser = modelMapper.map(registrationDTO, UserEntity.class);
        return userService.signUp(newUser);
    }

}
