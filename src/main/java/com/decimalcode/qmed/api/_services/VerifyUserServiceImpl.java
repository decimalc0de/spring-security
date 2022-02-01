package com.decimalcode.qmed.api._services;

import com.decimalcode.qmed.api.mail.service.EmailServiceImpl;
import com.decimalcode.qmed.api.users.service.UserEntity;
import com.decimalcode.qmed.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class VerifyUserServiceImpl {

    private final EmailServiceImpl emailService;
    private final HttpServletResponse response;

    @Autowired
    public VerifyUserServiceImpl(EmailServiceImpl emailService, HttpServletResponse response){
        this.emailService = emailService;
        this.response = response;
    }

    public ApiResponse<UserEntity> emailAccount(UserEntity user) {
        boolean mailWasSent = sendEmail(user);
        String apiResponseMessage = "Internal server error while sending confirmation email to %s, please try again";
        if(mailWasSent) apiResponseMessage = "A confirmation email as been sent to the email account %s you provided";
        apiResponseMessage = String.format(apiResponseMessage, user.getEmail());
        return new ApiResponse<>(true, apiResponseMessage, user, HttpStatus.OK.name(), 200);
    }

    /*
     * Util Methods
     */

    private boolean sendEmail(UserEntity user) {
        try{
            TokenServicesImpl numberToken = TokenServicesImpl.sixRandomNumberToken();
            /*
             * UserContact subject
             */
            String subject = "Confirm Your Account";
            /*
             * UserContact template resource name
             */
            String templateResourceName = "confirm-your-account.html";
            /*
             * Thymeleaf Context Initializer
             */
            Context templateContext = new Context();
            /*
             * Set context variables
             */
            templateContext.setVariable("mailToken", numberToken.getSixRandomDigit());
            templateContext.setVariable("name", user.getUsername());
            /*
             * Match multiple extension i.e .gif or .png
             */
            String locationPattern = "classpath*:/templates/mails/img/*.{gif, png}";
            /*
             * Initialise template inline images
             */
            Map<String, byte[]> templateInlineImages = emailService.getTemplateInlineImages(locationPattern, templateContext);
            /*
             * UserContact content-type /html
             */
            String mailContent = emailService.readMailContent(templateResourceName, templateContext);

            emailService.sendHtmlMessageWithInlineImages(user.getEmail(), subject, mailContent, templateInlineImages);

            response.addHeader("Verification", numberToken.getVerificationToken());

            return true;

        } catch (Exception exception) {return false;}

    }

}
