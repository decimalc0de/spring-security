package com.decimalcode.qmed.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiLogoutResponse {
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

    private String message;

    public ApiLogoutResponse(String message) {
        this.message = message;
    }

}
