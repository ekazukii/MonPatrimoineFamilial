package fr.cytech.mpf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

import java.io.Serializable;

@Entity
@Table(name = "ConvInfo")
@Getter
@Setter
public class ConvInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long conv;

    private long user_id;

    private String message;
}
