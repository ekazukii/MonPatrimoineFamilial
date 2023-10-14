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

    private String username;

    private boolean isMale;

    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.DELETE_ORPHAN)
    private Tree tree;
}
