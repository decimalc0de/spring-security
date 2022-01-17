package com.decimalcode.qmed.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ApiExceptionResponse {

    /*
    *
    */
    private final boolean success = false;

    /*
    *
    */
    private int statusCode = 500;

    /*
    *
    */
    private String error = "Internal Api Exception";
    /*
    *
    */
    private String timestamp = ZonedDateTime.now().format(
        DateTimeFormatter.ISO_INSTANT
    );

    /*
    *
    */
    private String messages;

    /*
    *
    */
    private String path;

    /*
    * No Argument Constructor
    */
    public ApiExceptionResponse() { }

}
