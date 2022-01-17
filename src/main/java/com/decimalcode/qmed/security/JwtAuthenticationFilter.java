package com.decimalcode.qmed.security;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.exception.custom.ApiException;
import com.decimalcode.qmed.response.ApiResponse;
import com.decimalcode.qmed.response.ApiSecurityExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.decimalcode.qmed.misc.ApiGeneralSettings.*;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements ApiSecurityExceptionResponse {

    private final AntPathMatcher pathMatcher;
    private final JwtTokenService tokenService;
    @Autowired
    public JwtAuthenticationFilter(AntPathMatcher pathMatcher,
                                   JwtTokenService tokenService) {
        this.pathMatcher = pathMatcher;
        this.tokenService = tokenService;
    }

    @SneakyThrows
    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain ) {
        System.out.println("JwtAuthenticationFilter.doFilterInternal");
        String authHeader = request.getHeader(AUTHORIZATION);
        if(Strings.isNotEmpty(authHeader))
        {
            if(authHeader.startsWith(TOKEN_BEARER)) {
                authenticateToken(authHeader, request, response, filterChain);
            }
            else {
                sendErrorResponse(
                    request,
                    response,
                    new ApiException("Http Status 403 - Access Denied")
                );// pass on to next filter
            }
        }
        else {
            sendErrorResponse(
                request,
                response,
                new ApiException("Http Status 403 - Access Denied")
            );// pass on to next filter
        }
    }

    /**
     * Exclude paths from this filter
     */
    @Override
    @SuppressWarnings("NullableProblems")
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludeUrlPatterns = List.of(
            API_ROOT_URL + SIGN_IN_URL,
            API_ROOT_URL + SIGN_UP_URL
        );
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Transactional
    @SuppressWarnings("UnnecessaryReturnStatement")
    private void authenticateToken(String authHeader,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        String token = authHeader.replace(TOKEN_BEARER, Strings.EMPTY);
        /*
         * Extract the jwt token body
         */
        Claims jwsPayload = extractAllJwsClaims(token);
        /*
         * Token is broken or expired
         */
        if(jwsPayload == null) {
            sendErrorResponse(
                request,
                response,
                new ApiException("Token is either broken or expired")
            );
            return;
        }
        else {
            if(!request.getRequestURI().equals(API_ROOT_URL + SIGN_OUT_URL)) {
                /*
                 * Valid the payload in store
                 */
                UserEntity user = tokenService.authenticateAccessToken(jwsPayload, response);
                if(user != null) {
                    /*
                     * Grant assesses to user
                     */
                    Authentication authenticate = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities()
                    );
                    SecurityContext sc = SecurityContextHolder.getContext();
                    sc.setAuthentication(authenticate);
                    if(sc.getAuthentication().isAuthenticated()) {
                        HttpSession httpSession = request.getSession(true);
                        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                    }
                    filterChain.doFilter(request, response);
                }
                else
                {
                    sendErrorResponse(
                        request,
                        response,
                        new ApiException("Token is either broken or expired")
                    );
                    return;
                }

            }
            else
            {
                boolean isDropped = tokenService.dropToken(jwsPayload.getId());
                if(!isDropped) {
                    sendErrorResponse(
                        request,
                        response,
                        new ApiException("Token is either broken or does not exit")
                    );
                    return;
                }
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(
                    response.getWriter(),
                    new ApiResponse<>("Logged out successfully", null)
                );
            }

        }

    }

    private Claims extractAllJwsClaims(String token) {
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

}
