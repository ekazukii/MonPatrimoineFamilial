package fr.cytech.mpf.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "UserTable")
public class User implements Serializable {
    @Id @GeneratedValue()
    private long Id;
    private String lastname;
    private String firstname;
    private String email;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String username;
    private boolean isMale;
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.DELETE_ORPHAN)
    private Tree tree;
    private UUID validationCode;
    private boolean isAdmin;
    private boolean isValidated;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp lastModifyDate;
    @Column(unique = true, nullable = false, length = 15)
    private String socialSecurityNumber;
    @Column(nullable = false)
    private String birthdate;
}
