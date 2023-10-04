package fr.cytech.mpf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String toMail, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("mpf.pro.sender@gmail.com");
        javaMailSender.send(mailMessage);
    }
}
