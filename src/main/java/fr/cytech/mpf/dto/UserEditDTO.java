package fr.cytech.mpf.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserEditDTO {
    private long Id;
    private String lastname;
    private String firstname;
    private String email;
    private String username;
    private Boolean isAdmin;
    private Boolean isMale;
    private Boolean isValidated;
    private String oldPassword;
    private String newPassword;
}
