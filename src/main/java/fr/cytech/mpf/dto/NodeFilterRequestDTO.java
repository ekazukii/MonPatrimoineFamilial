package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class NodeFilterRequestDTO implements Serializable {
    private Long treeId;
    private String filterIs;
    private Map<String, String> filterInfo;
}
