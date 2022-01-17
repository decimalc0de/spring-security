package com.decimalcode.qmed.exception;

import com.decimalcode.qmed.exception.custom.ApiException;
import com.decimalcode.qmed.response.ApiExceptionResponse;
import com.decimalcode.qmed.response.DTOExceptionResponse;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class _ExceptionInterceptor {

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpServletRequest servletRequest) {
        DTOExceptionResponse validationError = new DTOExceptionResponse();
        ex.getBindingResult().getFieldErrors().forEach((error)-> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            validationError.getMessages().put(fieldName, errorMessage);
        });
        validationError.setPath(servletRequest.getRequestURI());
        validationError.setErrorCount(validationError.getMessages().size());
        return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> handleApiException(Exception ex, HttpServletRequest servletRequest) {
        ApiExceptionResponse apiException = new ApiExceptionResponse();
        apiException.setMessages(ex.getMessage());
        apiException.setPath(servletRequest.getRequestURI());
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
