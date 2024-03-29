package fr.cytech.mpf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter @Getter @AllArgsConstructor
public class MergeTreeDTO implements Serializable {
    public Long requestingTreeId;
    public Long respondingTreeId;
    private long idRequester;
    private long idResponder;
}

