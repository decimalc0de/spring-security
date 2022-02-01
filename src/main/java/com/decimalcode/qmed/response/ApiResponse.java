package com.decimalcode.qmed.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;

@SuppressWarnings("unused")
@Getter
@Setter
@Component
@NoArgsConstructor
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /*
     *
     */
    private Boolean success;

    /*
     *
     */
    private int statusCode;

    /*
     *
     */
    private String status;

    /*
     *
     */
    private Map<String, String> errors;

    /*
     *
     */
    private T miscellaneousData;

    /*
     *
     */
    private OffsetDateTime dateTime = OffsetDateTime.now();

    /*
     *
     */
    private String message;

    /*
     *
     */
    private String path;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, String status, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, T miscData, String status, int statusCode) {
        this.miscellaneousData = miscData;
        this.status = status;
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;

    }

    public Boolean getSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public T getMiscellaneousData() {
        return miscellaneousData;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void setMiscellaneousData(T miscData) {
        this.miscellaneousData = miscData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
