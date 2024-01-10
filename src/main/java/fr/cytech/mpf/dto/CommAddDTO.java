package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class CommAddDTO implements Serializable {
    private long conv;
    private long souvenir;
    private String message;
}
