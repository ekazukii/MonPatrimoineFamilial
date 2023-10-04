package fr.cytech.mpf.controller;

import fr.cytech.mpf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import fr.cytech.mpf.service.MailService;

@Controller
public class MailController {
    @Autowired
    private MailService emailService;

    @PostMapping(value = "/sendmail")
    public void sendMail() {
            emailService.sendEmail("tom.baillet2@gmail.com", "monPapiFinito | Test mail back", "Bonjour Dark Baptiste, Vous voulez en finir avec votre Papi? Alors, vous êtes le bienvenue!");
    }
}
