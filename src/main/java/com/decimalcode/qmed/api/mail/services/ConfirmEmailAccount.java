package com.decimalcode.qmed.api.mail.services;

import com.decimalcode.qmed.api.mail.services.utils.EmailUtilsImpl;
import com.decimalcode.qmed.api.token.TokenServiceImpl;
import com.decimalcode.qmed.api.users.services.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import static com.decimalcode.qmed.misc.ApiGeneralSettings.*;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class ConfirmEmailAccount {

    private final TokenServiceImpl tokenService;
    private final EmailUtilsImpl emailUtils;

    @Autowired
    public ConfirmEmailAccount(EmailUtilsImpl emailUtils,
                               TokenServiceImpl tokenService){
        this.emailUtils = emailUtils;
        this.tokenService = tokenService;
    }

    public boolean sendEmail(UserEntity user) {
        try{
            String mailToken = generateToken();
            /*
             * Persist mail verification token
             */
            tokenService.persistToken(user, mailToken);
            /*
             * Mail subject
             */
            String subject = "Confirm Your Account";
            /*
             * Mail template resource name
             */
            String templateResourceName = "confirm-your-account.html";
            /*
             * Thymeleaf Context Initializer
             */
            Context templateContext = new Context();
            /*
             * Set context variables
             */
            templateContext.setVariable("mailToken", mailToken);
            templateContext.setVariable("name", user.getUsername());
            /*
             * Match multiple extension i.e .gif or .png
             */
            String locationPattern = "classpath*:/templates/mails/img/*.{gif, png}";
            /*
             * Initialise template inline images
             */
            Map<String, byte[]> templateInlineImages = emailUtils.getTemplateInlineImages(locationPattern, templateContext);
            /*
             * Mail content-type /html
             */
            String mailContent = emailUtils.readMailContent(templateResourceName, templateContext);

            emailUtils.sendHtmlMessageWithInlineImages(user.getEmail(), subject, mailContent, templateInlineImages);

            return true;

        } catch (Exception exception) {return false;}

    }

}
