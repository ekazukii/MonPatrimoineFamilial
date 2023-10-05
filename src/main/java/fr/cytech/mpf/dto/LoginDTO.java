package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class LoginDTO implements Serializable {
    private String username;
    private String password;
}
