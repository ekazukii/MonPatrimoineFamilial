package fr.cytech.mpf.service;

import fr.cytech.mpf.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fr.cytech.mpf.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
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

        File file = new File("H:/Desktop/" + fileName + "." + extension);
        OutputStream os = new FileOutputStream(file);
        os.write(image.getBytes());
    }

    public boolean isPasswordValid(String password, String hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8)).toString();
        return hashedPassword.equals(password);
    }

    public void generateUniqueUsername(RegisterDTO personalInfoData) {
        String baseUsername = personalInfoData.getFirstName().charAt(0) +
                personalInfoData.getLastName().replace(" ", "");

        String finalUsername = baseUsername;
        int suffix = 1;

        // Vérifiez si le username existe déjà
        while (userRepository.existsByUsername(finalUsername)) {
            finalUsername = baseUsername + suffix;
            suffix++;
        }

        personalInfoData.setUsername(finalUsername);
    }

    public boolean isBirthdateValid(String birthdate) {
        try {
            // Vérifier si la date peut être parsée correctement
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(birthdate, formatter);

            // Vérifier si la personne a entre 18 et 100 ans
            LocalDate currentDate = LocalDate.now();
            LocalDate minDate = currentDate.minusYears(100);
            LocalDate maxDate = currentDate.minusYears(18);

            return !parsedDate.isAfter(maxDate) && !parsedDate.isBefore(minDate);

        } catch (Exception e) {
            // En cas d'erreur de parsing ou autre, renvoyer false
            return false;
        }
    }
}
