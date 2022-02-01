package com.decimalcode.qmed.exception;

import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


@RestControllerAdvice
@SuppressWarnings("unused")
public class _ExceptionInterceptor {

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public ApiResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new java.util.HashMap<>(Map.of());
        ex.getBindingResult().getFieldErrors().forEach((error)-> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // apiResponse.errors(errors)
        return new ApiResponse<>(
            false, "Data validation failure",
            HttpStatus.BAD_REQUEST.name(), 400
        );
    }

    @ExceptionHandler(value={Exception.class})
    public ApiResponse<Void> handleApiException(Exception ex) {
        Map<String, String> errors = new java.util.HashMap<>(Map.of());
        for(StackTraceElement el : ex.getStackTrace())
            errors.put(el.getClassName(), el.getMethodName() + ':' + el.getLineNumber());
        // apiResponse.errors(errors);
        return new ApiResponse<>(
            false, ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.name(), 500
        );
    }

}
