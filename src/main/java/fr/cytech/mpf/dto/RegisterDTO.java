package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter @Setter
public class RegisterDTO implements Serializable {
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private String confirmPassword;
    private String username;
    private String birthDate;
    public boolean isMale;
}
