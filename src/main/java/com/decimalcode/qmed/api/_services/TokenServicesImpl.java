package com.decimalcode.qmed.api._services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Random;

import static com.decimalcode.qmed.config.ApiGeneralSettings.JWT_SECRET_KEY;
import static com.decimalcode.qmed.config.ApiGeneralSettings.TOKEN_EXPIRATION_TIME;

/**
 * Generate Token
 */
@Component
@SuppressWarnings({"unused", "SameParameterValue"})
public class TokenServicesImpl {

    private String clientKey;
    private String verificationToken;

    public static TokenServicesImpl rawToken(String id, String... metaData) {
        /*
         * Initialise secret key
         */
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
        /*
         * IssueAt time
         */
        long issueAtMilliSec = OffsetDateTime.now().toInstant().toEpochMilli();
        /*
         * ExpireAt time
         */
        long ExpireAtMilliSec = OffsetDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME).toInstant().toEpochMilli();
        /*
         * JWT Token
         */
        String verificationToken = Jwts.builder()
                                        .setId(id)
                                        .setSubject("verification")
                                        .claim("verify", metaData)
                                        .setIssuedAt(new Date(issueAtMilliSec))
                                        .setExpiration(new Date(ExpireAtMilliSec))
                                        .signWith(jwtSecretKey)
                                        .compact();
        TokenServicesImpl tokenServicesImpl = new TokenServicesImpl();
        tokenServicesImpl.setVerificationToken(verificationToken);
        return tokenServicesImpl;
    }

    /**
     * Generate a six digit random number
     * Send the random digit to the client
     * Decode the rand digit with some algorithm
     * ann add it as token id created
     */
    public static TokenServicesImpl sixRandomNumberToken(String... metaData) {
        int sixRandomDigit = sixRandomDigit();
        String verificationKey = convertToBaseX(sixRandomDigit >> 2, 10, 6);
        /*
         * Initialise secret key
         */
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
        /*
         * IssueAt time
         */
        long issueAtMilliSec = OffsetDateTime.now().toInstant().toEpochMilli();
        /*
         * ExpireAt time
         */
        long ExpireAtMilliSec = OffsetDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME).toInstant().toEpochMilli();
        /*
         * JWT Token
         */
        /*
         * Sign & Return Token
         */
        String verificationToken = Jwts.builder()
                                        .setId(verificationKey)
                                        .setSubject("verification")
                                        .claim("verify", metaData)
                                        .setIssuedAt(new Date(issueAtMilliSec))
                                        .setExpiration(new Date(ExpireAtMilliSec))
                                        .signWith(jwtSecretKey)
                                        .compact();
        TokenServicesImpl tokenServicesImpl = new TokenServicesImpl();
        tokenServicesImpl.setVerificationToken(verificationToken);
        tokenServicesImpl.setSixRandomDigit(sixRandomDigit);
        return tokenServicesImpl;
    }

    public static boolean decodeSixRandomNumberToken(int sixRandomDigit, String token) {
        Claims payload = extractAllJwsClaims(token);
        if(payload != null) {
            String payloadId = payload.getId();
            String verificationKey = convertToBaseX(sixRandomDigit >> 2, 10, 6);
            return payloadId.equals(verificationKey);
        }
        return false;
    }

    public String getSixRandomDigit() {
        return clientKey;
    }

    public void setSixRandomDigit(int clientKey) {
        String key = String.valueOf(clientKey);
        this.clientKey = key.substring(0, 3) + '-' + key.substring(3);
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
    /*
     *
     */
    public static Claims extractAllJwsClaims(String token) {
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
        try{
            return  Jwts.parserBuilder()
                        .setSigningKey(jwtSecretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        }
        catch(Exception ex) {
            return null;
        }
    }

    /*
     * Generate a six-digit number token
     */
    private static int sixRandomDigit() {
        int bound = 900000;
        return (100000 + new Random().nextInt(bound));
    }

    /*
     *
     */
    private static String convertToBaseX(int value, int fromBase, int toBase) {
        String valueToString = String.valueOf(value);
        return Integer.toString(Integer.parseInt(valueToString, fromBase), toBase);
    }

}
