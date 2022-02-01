package com.decimalcode.qmed.api._controllers;

import com.decimalcode.qmed.api.registration.service.RegistrationRequest;
import com.decimalcode.qmed.api.registration.service.RegistrationService;
import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.decimalcode.qmed.config.ApiGeneralSettings.API_ROOT_URL;
import static com.decimalcode.qmed.config.ApiGeneralSettings.SIGN_UP_URL;


@RestController
@SuppressWarnings("unused")
@RequiredArgsConstructor
@RequestMapping(API_ROOT_URL + SIGN_UP_URL)
public class RegisterController {

    private final RegistrationService registrationService;

    @PostMapping
    public ApiResponse<UserEntity> register(@Valid @RequestBody RegistrationRequest registrationDTO) {
        return registrationService.acceptValidatedRequest(registrationDTO);
    }

    @GetMapping("/verify-email-account")
    public ApiResponse<Void> confirmUserEmail(@RequestParam String jwtToken) {
        return registrationService.verifyLink(jwtToken);
    }

    @GetMapping("/six-digit/verify-email-account")
    public ApiResponse<Void> confirmUserContact(HttpServletRequest  request,
                                                       HttpServletResponse response,
                                                       @RequestParam Integer sixDigitNumber) {
        return registrationService.verifySixDigitNumber(request, sixDigitNumber);
    }

}
