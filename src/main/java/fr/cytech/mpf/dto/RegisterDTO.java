package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter @Setter
public class RegisterDTO implements Serializable {
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String password2;
    private String username;
    private String birthDate;
    public boolean isMale;
    public long socialSecurityNumber;
    public String nationality;
}
