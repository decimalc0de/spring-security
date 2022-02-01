package com.decimalcode.qmed.api.registration.service;

import com.decimalcode.qmed.api._services.TokenServicesImpl;
import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.api.users.service.UserServiceImpl;
import com.decimalcode.qmed.exception.ApiException;
import com.decimalcode.qmed.response.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

    public ApiResponse<Void> verifyLink(String jwtToken) {
        Claims jwsClaims = TokenServicesImpl.extractAllJwsClaims(jwtToken);
        if(jwsClaims != null) {
            String message = "Verified successfully";
            return new ApiResponse<>(true, message, HttpStatus.OK.name(), 200);
        }
        throw new ApiException("Verification failed");
    }

    public ApiResponse<Void> verifySixDigitNumber(HttpServletRequest request, Integer sixDigitNumber) {
        String verificationToken = request.getHeader("Verification");
        boolean isVerified = TokenServicesImpl.decodeSixRandomNumberToken(sixDigitNumber, verificationToken);
        if(isVerified) {
            String message = "Verified successfully";
            return new ApiResponse<>(true, message, HttpStatus.OK.name(), 200);
        }
        throw new ApiException("Verification failed");
    }

}
