package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class MsgAddDTO implements Serializable {
    private long conv;
    private long user_id;
    private String message;
    private Date date;
    private long file_id;
}
