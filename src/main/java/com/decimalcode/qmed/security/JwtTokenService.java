package com.decimalcode.qmed.security;

import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.exception.ApiException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

import static com.decimalcode.qmed.config.ApiGeneralSettings.AUTHORIZATION;
import static com.decimalcode.qmed.config.ApiGeneralSettings.TOKEN_BEARER;

@Service
public class JwtTokenService {

    private final JwtTokenRepository tokenRepository;
    @Autowired
    public JwtTokenService(JwtTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public UserEntity authenticateAccessToken(Claims payload,
                                              HttpServletResponse response) {
        UUID uuid = UUID.fromString(payload.getId());
        Optional<JwtToken> optionalToken = tokenRepository.findById(uuid);
        if (optionalToken.isPresent()) {
            JwtToken accessToken = optionalToken.get();

            int reqId = payload.get("req-id", Integer.class);

            if(reqId != accessToken.getUniqueRequestId()) return null;

            accessToken.autoUniqueRequestId();
            JwtToken accessToken0 = tokenRepository.saveAndFlush(accessToken);

            response.addHeader(
                AUTHORIZATION,
                TOKEN_BEARER + accessToken0.generateToken()
            );
            return accessToken.getUser();
        }
        return null;
    }

    @Transactional
    public boolean dropToken(String uuid) {
        try{
            UUID id = UUID.fromString(uuid);
            tokenRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    @Transactional
    public JwtToken persistJwtToken(JwtToken token) {
        try{
            return tokenRepository.saveAndFlush(token);
        }
        catch(Exception e) {
            throw new ApiException("Internal Server error");
        }
    }

}
