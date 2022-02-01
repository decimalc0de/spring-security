package com.decimalcode.qmed.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }
}
