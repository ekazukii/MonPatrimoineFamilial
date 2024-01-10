package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MsgGetDTO implements Serializable{
    private long id;
    private long user_id;
    private String message;
    private Date date;
    private Timestamp timestamp;

    private long file_id;
}
