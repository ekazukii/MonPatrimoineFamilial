package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserGetDTO {
    private Long id;

    private String lastname;

    private String firstname;

    private String email;

    private String username;

    private String socialSecurityNumber;

    private boolean isValidated;

    private boolean isAdmin;

    private String birthdate;
}
