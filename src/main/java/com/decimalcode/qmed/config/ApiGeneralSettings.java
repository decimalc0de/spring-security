package com.decimalcode.qmed.config;

public class ApiGeneralSettings {
    /*
     * API URLs
     */
    public static final String API_ROOT_URL = "/api";
    public static final String SIGN_IN_URL = "/sign-in";
    public static final String SIGN_OUT_URL = "/sign-out";
    public static final String SIGN_UP_URL = "/registration";

    /* ################### TOKEN SETTINGS ############################# */
    public static final int TOKEN_EXPIRATION_TIME = 60 * 5; // Token Expiration time - in minutes
    public static final String TOKEN_BEARER = "Bearer "; // Token - Bearer
    public static final String AUTHORIZATION = "Authorization"; // response 'Authorization' header
    public static final String JWT_SECRET_KEY = "JWT_SECRET"; // JWT-SECRET

    public static String getFileNameWithoutExtension(String filename) {
        return filename == null ? "" : filename.substring(0, filename.lastIndexOf('.'));
    }

}
