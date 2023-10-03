package fr.cytech.mpf.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    public String passwordToHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
        return hashedPassword;
    }

    public boolean isPasswordValid(String password, String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
        return hashedPassword.equals(password);
    }
}
