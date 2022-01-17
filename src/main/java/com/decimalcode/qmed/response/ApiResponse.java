package com.decimalcode.qmed.response;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    /*
    *
    */
    private final Boolean success = true;

    /*
    *
    */
    private final int statusCode = 200;

    /*
    *
    */
    private final String pointer = "Request Created";

    /*
    *
    */
    @Setter
    private T miscData;

    /*
    *
    */
    @Setter
    private String message;

    public ApiResponse(String message, T miscData) {
        this.message = message;
        this.miscData = miscData;
    }

}
