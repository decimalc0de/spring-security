package com.decimalcode.qmed.response;

import com.decimalcode.qmed.config.ApplicationContextImpl;
import com.decimalcode.qmed.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ApiSecurityExceptionResponse implements AccessDeniedHandler, AuthenticationEntryPoint {

    private static final String REALM = "Bearer Realm security channel";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        sendErrorResponse(authException, response);
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        sendErrorResponse(accessDeniedException, response);
    }

    public static void sendErrorResponse(Exception exception,
                                         HttpServletResponse response) {
        try {
            response.addHeader("WWW-Authentication", REALM);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            int status = response.getStatus();
            /*
             * Get error message
             */
            String exceptionMessage = exception.getMessage();
            if(exception instanceof BadCredentialsException){
                exceptionMessage = "username or password is incorrect ";
            }

            Map<String, String> errors = Map.of("Realm", "HTTP Status " + status + " - " + REALM);
            ApiResponse<Void> apiResponse = new ApiResponse<>(
                false, exceptionMessage, HttpStatus.UNAUTHORIZED.name(), 401
            );
            apiResponse.setErrors(errors);

            ObjectMapper objectMapper = ApplicationContextImpl.getBean(ObjectMapper.class);
            String gsonString = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().print(gsonString); // return json response as error
        }
        catch (Exception e) { throw new ApiException("Internal server exception"); }
    }

}
