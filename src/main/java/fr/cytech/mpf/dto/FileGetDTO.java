package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class FileGetDTO {
    private String id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private Timestamp timestamp;
    private long conv;
}
