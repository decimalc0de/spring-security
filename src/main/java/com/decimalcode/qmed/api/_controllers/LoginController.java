package com.decimalcode.qmed.api._controllers;

import com.decimalcode.qmed.api.users.service.UserCredential;
import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.api.users.service.UserPrincipal;
import com.decimalcode.qmed.exception.ApiException;
import com.decimalcode.qmed.response.ApiResponse;
import com.decimalcode.qmed.security.JwtToken;
import com.decimalcode.qmed.security.JwtTokenService;
import com.decimalcode.qmed.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.decimalcode.qmed.config.ApiGeneralSettings.AUTHORIZATION;
import static com.decimalcode.qmed.config.ApiGeneralSettings.TOKEN_BEARER;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign-in")
@Import(WebSecurityConfig.class)
public class LoginController {

    private final JwtTokenService tokenService;

    private final AuthenticationManager authenticationManager;

    @PostMapping
    @SuppressWarnings("unused")
    public ApiResponse<UserEntity> signIn(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @Valid @RequestBody UserCredential credentials){
        /*
         * Initialise UsernamePasswordAuthenticationToken: An Authentication implementation class
         */
        UsernamePasswordAuthenticationToken userCredentials = new UsernamePasswordAuthenticationToken(
            credentials.getUsername(), credentials.getPassword()
        );
        /*
         * Authenticate user credentials
         */
        Authentication authentication = authenticationManager.authenticate(userCredentials);
        /*
         * Store the details of the currently authenticated user
         */
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        /*
         * Check if user is authorized
         */
        if(sc.getAuthentication().isAuthenticated()) {
            /*
             * Get user-object
             */
            UserEntity user = ((UserPrincipal)authentication.getPrincipal()).getUser();
            /*
             * Generate access-token object
             */
            JwtToken accessToken = new JwtToken();
            /*
             * Include access-token
             */
            accessToken.setUser(user);
            /*
             * Persist Jwt Token
             */
            JwtToken accessToken0 = tokenService.persistJwtToken(accessToken);
            /*
             * Generate access-token
             */
            String plainToken = accessToken0.generateToken();
            /*
             * Add token to response header
             */
            response.addHeader(AUTHORIZATION, TOKEN_BEARER + plainToken);
            /*
             * Meta-Data
             */
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            /*
             * Return Api-Response message
             */
            String message = "sign in successful";
            return new ApiResponse<>(true, message, user, HttpStatus.OK.name(), 200);
        }
        throw new ApiException("Internal server error");
    }

}
