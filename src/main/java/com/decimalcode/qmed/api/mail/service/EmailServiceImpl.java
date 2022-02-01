package com.decimalcode.qmed.api.mail.service;

import com.decimalcode.qmed.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.decimalcode.qmed.config.ApiGeneralSettings.getFileNameWithoutExtension;

@Component
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class EmailServiceImpl implements IEmailService {

    private Boolean ignore = false;
    private Boolean deepIgnore = false;

    private MimeMessage mimeMessage;
    private MimeMessageHelper message;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;

    private final String SENDER_EMAIL = "dev.smtp.noreply@gmail.com";

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender,
                            TemplateEngine templateEngine,
                            ResourceLoader resourceLoader) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(SENDER_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }
        catch(Exception exception) { throw new ApiException(exception.getMessage()); }
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        final int multipart = MimeMessageHelper.MULTIPART_MODE_RELATED;
        final String encoding = "UTF-8";
        try{
            mimeMessage = emailSender.createMimeMessage();
            message = new MimeMessageHelper(mimeMessage, multipart, encoding);
            /*
            * Set sender mail address
             */
            message.setFrom(SENDER_EMAIL); // can be ignored
            /*
            * Set mail subject
            */
            message.setSubject(subject); // mail subject
            /*
            * Set Recipient Email address
            */
            message.setTo(to); // to mail address

            /*
            * Send mail
            */
            if(!ignore){
                /*
                 * Set mail content
                 */
                message.setText(htmlContent, true); // true = isHtml
                /*
                 * Send UserContact
                 */
                emailSender.send(mimeMessage);
            }
        }
        catch(Exception exception) { throw new ApiException(exception.getMessage()); }

    }

    @Override
    public void sendHtmlMessageWithAttachment(String to,
                                              String subject,
                                              String htmlContent ) {
        try{
            ignore = true;

            /*
             * Initializer variables
             */
            sendHtmlMessage(to, subject, htmlContent);

            /*
             * TODO add mail attachments here
             */

            /*
             * Set mail content
             */
            message.setText(htmlContent, true); // true = isHtml

            /*
             * Send mail
             */
            this.emailSender.send(mimeMessage);

            ignore = false;
        }
        catch (Exception exception){ throw new ApiException(exception.getMessage()); }
    }

    @Override
    public void sendHtmlMessageWithAttachment(String to,
                                              String subject,
                                              String htmlContent,
                                              Map<String, byte[]> inlineImageResources) {
        try{
            deepIgnore = true;

            /*
             * Initializer variables
             */
            sendHtmlMessageWithInlineImages(to, subject, htmlContent, inlineImageResources);

            /*
             * TODO add mail attachments here
             */

            /*
             * Send mail
             */
            this.emailSender.send(mimeMessage);

            deepIgnore = false;
        }
        catch (Exception exception){throw new ApiException(exception.getMessage());}
    }

    @Override
    public void sendHtmlMessageWithInlineImages(String to,
                                                String subject,
                                                String htmlContent,
                                                Map<String, byte[]> inlineImageResources) {
        try{
            ignore = true;
            /*
             * Initializer variables
             */
            sendHtmlMessage(to, subject, htmlContent);
            /*
             * Set mail content
             */
            message.setText(htmlContent, true); // true = isHtml
            /*
             * Add the inline images, referenced from the HTML code as "cid:${attachmentResourceName}"
             */
            for(Map.Entry<String, byte[]> inlineImageResource: inlineImageResources.entrySet()) {
                byte[] aImageBytes = inlineImageResource.getValue();
                String inlineImageResourceName = inlineImageResource.getKey();
                InputStreamSource imageSource = new ByteArrayResource(aImageBytes);
                message.addInline(
                    inlineImageResourceName,
                    imageSource,
                    URLConnection.guessContentTypeFromStream(imageSource.getInputStream())
                );
            }

            /*
             * Send mail
             */
            if(!deepIgnore) this.emailSender.send(mimeMessage);

            ignore = false;
        }
        catch (Exception exception){ throw new ApiException(exception.getMessage()); }
    }

    @Override
    /*
     * Recover all errors thrown while sending email
     * after 2-trails
     */
    public void recover(ApiException exception) {
        throw new ApiException(exception.getMessage());
    }

    public String readMailContent(String templateResourceName, Context context) {
        String mailContent = null;
        try {
            mailContent = this.templateEngine.process(templateResourceName, context);
        }catch(Exception ignore){}
        return mailContent != null ? mailContent : "";
    }

    public Map<String, byte[]> getTemplateInlineImages(String locationPattern, Context templateContext) {
        Map<String, byte[]> inlineImageResources = new LinkedHashMap<>();
        try {
            // Load resources
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(locationPattern);
            for(Resource resource: resources) {
                String resourceFilename = getFileNameWithoutExtension(resource.getFilename());
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getFile());
                templateContext.setVariable(resourceFilename, resourceFilename);
                inlineImageResources.put(resourceFilename, bytes);
            }
            return inlineImageResources;
        }
        catch(Exception exception) { throw new ApiException(exception.getMessage()); }
    }

}
