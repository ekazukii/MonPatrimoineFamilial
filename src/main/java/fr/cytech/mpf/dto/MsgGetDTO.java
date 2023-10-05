package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import java.io.Serializable;

@Getter
@Setter
public class MsgGetDTO implements Serializable{
    private long id;
    private long user_id;
    private String message;
    private Timestamp timestamp;
}
