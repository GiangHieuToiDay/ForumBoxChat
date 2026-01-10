package com.forum.boxchat.service;


import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String Html) throws AppException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(Html, true); // cái này là send dạng html

            helper.setFrom("g.trung.hieu.08@gmail.com");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new AppException(ErrorCode.CANOT_SEND_EMAIL);
        }
    }


}
