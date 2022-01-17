package com.decimalcode.qmed.response;

import java.util.LinkedHashMap;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class DTOExceptionResponse {

    /*
    *
    */
    private final boolean success = false;

    /*
    *
    */
    private final int statusCode = 400;

    /*
    *
    */
    private final String error = "Bad Request";

    /*
    *
    */
    private int errorCount;

    /*
    *
    */
    private final String timestamp = ZonedDateTime.now().format(
        DateTimeFormatter.ISO_INSTANT
    );

    /*
    *
    */
    private final Map<String, String> messages = new LinkedHashMap<>();

    /*
    *
    */
    private String path;

    /*
    * No Argument Constructor
    */
    public DTOExceptionResponse() { }

    public Map<String, String> getMessages() {
        return messages;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

}
