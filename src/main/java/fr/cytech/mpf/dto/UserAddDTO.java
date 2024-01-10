package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class UserAddDTO implements Serializable {
    private String lastname;

    private String firstname;

    private String email;

    private String password;

    private String confirmPassword;

    private String username;

    private String socialSecurityNumber;

    private String birthdate;
}
