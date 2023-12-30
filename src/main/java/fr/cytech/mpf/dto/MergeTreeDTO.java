package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter @Getter
public class MergeTreeDTO implements Serializable {
    public Long requestingTreeId;
    public Long respondingTreeId;
    public UUID[] parentsNodesRequester;
    public UUID[] childrenNodesRequester;
    public UUID userNodeRequesterId;
    public UUID userNodeResponderId;
}

