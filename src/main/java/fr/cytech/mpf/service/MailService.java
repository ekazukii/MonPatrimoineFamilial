package fr.cytech.mpf.service;

import fr.cytech.mpf.entity.User;
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

    @Async
    public void sendValidationCode(User user) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Validation de l'email pour le compte ");
        messageBuilder.append(user.getUsername());
        messageBuilder.append("\n");
        messageBuilder.append("Pour valider la création de votre compte cliquez sur ce lien http://localhost:8080/user/validate?code=");
        messageBuilder.append(user.getValidationCode());
        this.sendEmail(user.getEmail(), "[MPF] Validation de votre email", messageBuilder.toString());
    }
}