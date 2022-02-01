package com.decimalcode.qmed.api.mail.service;

import com.decimalcode.qmed.exception.ApiException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.util.Map;

@SuppressWarnings("unused")
public interface IEmailService {
    @Retryable(maxAttempts = 2, value = ApiException.class, backoff = @Backoff(delay = 2000))
    void sendHtmlMessageWithAttachment(String to,
                                       String subject,
                                       String htmlContent);

    @Retryable(maxAttempts = 2, value = ApiException.class, backoff = @Backoff(delay = 2000))
    void sendHtmlMessageWithAttachment(String to,
                                       String subject,
                                       String htmlContent,
                                       Map<String, byte[]> attachmentResources);

    @Retryable(maxAttempts = 2, value = ApiException.class, backoff = @Backoff(delay = 2000))
    void sendHtmlMessageWithInlineImages(String to,
                                         String subject,
                                         String htmlContent,
                                         Map<String, byte[]> inlineImageResources);

    @Retryable(maxAttempts = 2, value = ApiException.class, backoff = @Backoff(delay = 2000))
    void sendHtmlMessage(String to, String subject, String text);

    @Retryable(maxAttempts = 2, value = ApiException.class, backoff = @Backoff(delay = 2000))
    void sendSimpleMessage(String to, String subject, String text);

    @Recover
    void recover(ApiException exception);
}
