package com.decimalcode.qmed.api.mail.services.utils;

import com.decimalcode.qmed.api.mail.services.ConfirmEmailAccount;
import com.decimalcode.qmed.api.users.services.UserEntity;
import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService{

    private final ConfirmEmailAccount confirmEmailAccount;

    @Autowired
    public EmailServiceImpl(ConfirmEmailAccount confirmEmailAccount){
        this.confirmEmailAccount = confirmEmailAccount;
    }

    @Override
    public ApiResponse<UserEntity> confirmYourEmailAccount(UserEntity user) {
        boolean mailWasSent = confirmEmailAccount.sendEmail(user);
        String apiResponseMessage = "Internal server error while sending confirmation email to %s, please try again";
        if(mailWasSent) apiResponseMessage = "A confirmation email as been sent to the email account %s you provided";
        apiResponseMessage = String.format(apiResponseMessage, user.getEmail());
        return new ApiResponse<>(apiResponseMessage, user);
    }

}
