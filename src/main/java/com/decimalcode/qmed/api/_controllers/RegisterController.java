package com.decimalcode.qmed.api._controllers;

import com.decimalcode.qmed.api.registration.services.RegistrationRequest;
import com.decimalcode.qmed.api.registration.services.RegistrationService;
import com.decimalcode.qmed.api.token.TokenDto;
import com.decimalcode.qmed.api.token.TokenEntity;
import com.decimalcode.qmed.api.token.TokenServiceImpl;
import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.api.users.services.UserServiceImpl;
import com.decimalcode.qmed.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.decimalcode.qmed.misc.ApiGeneralSettings.*;


@RestController
@SuppressWarnings("unused")
@RequiredArgsConstructor
@RequestMapping(API_ROOT_URL + SIGN_UP_URL)
public class RegisterController {

    private final UserServiceImpl userService;
    private final TokenServiceImpl tokenService;
    private final RegistrationService registrationService;

    @PostMapping
    public ApiResponse<UserEntity> register(@Valid @RequestBody RegistrationRequest registrationDTO) {
        return registrationService.acceptValidatedRequest(registrationDTO);
    }

    @GetMapping("/verify-email-account")
    public ApiResponse<TokenEntity> confirmRegistrationEmail(TokenDto token) {
        return tokenService.confirmRegistrationEmailToken(token.getUser(), token.get());
    }

}
