package com.decimalcode.qmed.api.token;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ITokenService {
    @Transactional
    void persistToken(UserEntity appUser, String token);

    @Transactional
    ApiResponse<TokenEntity> confirmRegistrationEmailToken(UserEntity appUser, String token);
}
