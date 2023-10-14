package fr.cytech.mpf.dto;

import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;

@Getter @Setter
public class TreeViewAddDTO implements Serializable {
    public Long viewerId;
    public Long treeId;
}
