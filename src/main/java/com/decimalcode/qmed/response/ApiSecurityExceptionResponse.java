package com.decimalcode.qmed.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ApiSecurityExceptionResponse {

    String REALM = "Bearer Realm security channel";

    default void sendErrorResponse(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Exception exception ) {
        try {
            response.addHeader("WWW-Authentication", REALM);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            int status = response.getStatus();

            ApiExceptionResponse apiExceptionRes = new ApiExceptionResponse();
            apiExceptionRes.setError("HTTP Status " + status + " - " + REALM);
            String exceptionMessage = exception.getMessage();
            if(exception instanceof BadCredentialsException){
                exceptionMessage = "username or password is incorrect ";
            }
            apiExceptionRes.setMessages(exceptionMessage);
            apiExceptionRes.setPath(request.getRequestURI());
            apiExceptionRes.setStatusCode(response.getStatus());

            ObjectMapper gsonObjMapper = new ObjectMapper();

            String gsonString = gsonObjMapper.writeValueAsString(apiExceptionRes);
            response.getWriter().print(gsonString); // return json response as error
        }
        catch (Exception e) { throw new RuntimeException("Internal server error"); }
    }

}
