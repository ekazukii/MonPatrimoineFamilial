package fr.cytech.mpf.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommGetDTO implements Serializable {
    private long id;
    private String message;
    private Timestamp timestamp;
}
