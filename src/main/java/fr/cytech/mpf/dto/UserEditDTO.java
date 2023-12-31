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
    private boolean isAdmin;
    private boolean isValidated;
}
