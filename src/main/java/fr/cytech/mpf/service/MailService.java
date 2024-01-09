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

    /**
     * Send an email with the given parameters
     * @param toMail the email address to send the email to
     * @param subject the subject of the email
     * @param message the message of the email
     */
    @Async
    public void sendEmail(String toMail, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("mpf.pro.sender@gmail.com");
        javaMailSender.send(mailMessage);
    }

    /**
     * Send a validation code to a user
     * @param user the user to send the validation code to
     */
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

    /**
     * Send an update your tree message to a user
     * @param user the user to send the message to
     * @param userThatChangedTree the user that changed the tree
     */
    @Async
    public void sendUpdateMessage(User user, User userThatChangedTree) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Modification de l'arbre de ");
        messageBuilder.append(userThatChangedTree.getUsername());
        messageBuilder.append("\n");
        messageBuilder.append("\n");
        messageBuilder.append("Pour voir les modifications cliquez sur ce lien http://localhost:5173/external?id=");
        messageBuilder.append(user.getTree().getId());
        messageBuilder.append("\n");
        messageBuilder.append("\n");
        messageBuilder.append("Pour re-synchroniser les changements cliquez sur ce lien http://localhost:8080/tree/mergeStrategy?requestingTreeId=");
        messageBuilder.append(user.getTree().getId());
        messageBuilder.append("&respondingTreeId=");
        messageBuilder.append(userThatChangedTree.getTree().getId());
        messageBuilder.append("&idRequester=");
        messageBuilder.append(user.getId());
        messageBuilder.append("&idResponder=");
        messageBuilder.append(userThatChangedTree.getId());
        this.sendEmail(user.getEmail(), "[MPF] Modification de l'arbre de " + userThatChangedTree.getUsername(), messageBuilder.toString());
    }
}