package fr.cytech.mpf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Table that save all the messages of the memories feature
 */
@Entity
@Table(name = "ConvInfo")
@Getter
@Setter
public class MsgInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long conv;

    private long user_id;

    private String message;

    private Date date;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Timestamp timestamp;

    private long file_id;
}
