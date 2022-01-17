package com.decimalcode.qmed.api.mail.services.utils;

import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.response.ApiResponse;

public interface IEmailService {
    ApiResponse<UserEntity> confirmYourEmailAccount(UserEntity user);
}
