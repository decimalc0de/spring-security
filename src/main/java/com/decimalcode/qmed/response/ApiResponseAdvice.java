package com.decimalcode.qmed.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<ApiResponse<?>> {
    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ApiResponse<?> beforeBodyWrite(ApiResponse<?> body,
                                          MethodParameter returnType,
                                          MediaType selectedContentType,
                                          Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                          ServerHttpRequest request,
                                          ServerHttpResponse response) {
        if(body != null) {
            HttpStatus statusCode = HttpStatus.valueOf(
                body.getStatusCode()
            );
            response.setStatusCode(statusCode);
            body.setPath(request.getURI().getPath());
        }

        return body != null? body : new ApiResponse<>();
    }
}
