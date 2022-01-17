package com.decimalcode.qmed.api._controllers;

import lombok.RequiredArgsConstructor;
import com.decimalcode.qmed.api.token.TokenServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {
    private final TokenServiceImpl tokenService;
}
