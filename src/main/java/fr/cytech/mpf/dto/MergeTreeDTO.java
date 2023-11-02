package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter @Getter
public class MergeTreeDTO implements Serializable {
    public Long requestingTreeId;
    public Long respondingTreeId;  
    public Long[] parentsNodesRequester;
    public Long[] childrenNodesRequester;
}

