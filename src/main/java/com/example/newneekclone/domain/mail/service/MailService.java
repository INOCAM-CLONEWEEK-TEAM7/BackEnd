package com.example.newneekclone.domain.mail.service;

import com.example.newneekclone.domain.mail.entity.EmailMessage;
import com.example.newneekclone.global.enums.SuccessCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static com.example.newneekclone.global.enums.SuccessCode.EMAIL_SEND_SUCCESS;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public SuccessCode sendMail(EmailMessage email, String templateName) {
        Date date = new Date();
        long deadline = date.getTime() + 1000L * 60 * 5;
        String authNum = email.getTo() + "@newneekclone@" + deadline;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(setContext(authNum, templateName), true);
            mimeMessageHelper.setFrom(new InternetAddress("newneekclone@ino.cam", "이노캠7조"));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return EMAIL_SEND_SUCCESS;
    }

    private String setContext(String code, String templateName) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(templateName, context);
    }
}