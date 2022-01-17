package com.decimalcode.qmed.exception.custom;

public class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }
}
