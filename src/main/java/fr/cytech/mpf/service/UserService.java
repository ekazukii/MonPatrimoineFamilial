package fr.cytech.mpf.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    public String passwordToHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update("tempsalt".getBytes(StandardCharsets.UTF_8));
        byte[] hashedPasswordBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< hashedPasswordBytes.length ;i++){
            sb.append(Integer.toString((hashedPasswordBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void saveFileImage(MultipartFile image, String fileName, String subFolder) throws IOException {
        String[] parts = image.getOriginalFilename().split("[.]");
        String extension = parts[parts.length - 1];

        File file = new File("/Users/ekazuki/Desktop/" + fileName + "." + extension);
        OutputStream os = new FileOutputStream(file);
        os.write(image.getBytes());
    }

    public boolean isPasswordValid(String password, String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
        return hashedPassword.equals(password);
    }
}
