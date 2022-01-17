package com.decimalcode.qmed.api.mail.services.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Configuration
@ComponentScan({"com.decimalcode.qmed.api.mailer"})
public class EmailConfig {

    @Bean
    @SuppressWarnings("SpellCheckingInspection")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        /*
         * NOTE: In the Gmail account "Security" setting
         * 1. Enable "Less secure app"
         * 2. Disable "Two steps verification"
         * OR
         * 1. Disable "Less secure app"
         * 2. Enable "Two steps verification"
         * 3. Then create app - password
         * 4. Copy and paste the app password here
         */
        mailSender.setUsername("dev.smtp.noreply@gmail.com");
        mailSender.setPassword("ydlxhxdwcrgvoyyl");

        mailSender.setHost("smtp.gmail.com");
        /*
         * NOTE: SSL, post 465 And Setting
         * spring.mail.properties.mail.smtp.socketFactory.port = 465
         * spring.mail.properties.mail.smtp.socketFactory.class = javax.net.ssl.SSLSocketFactory
         * But for this config, our default is TLS - port 587
         */
        mailSender.setPort(587);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");// TLS
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.debug", "true");

        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.smtp.timeout", "15000");

        return mailSender;
    }

    @Bean
    public ClassLoaderTemplateResolver thymeleafEmailTemplateConfig(){
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("/templates/mails/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);
        return emailTemplateResolver;
    }

}
