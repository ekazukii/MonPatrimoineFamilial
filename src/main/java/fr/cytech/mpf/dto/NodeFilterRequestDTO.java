package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterRequestDTO {
    private Long treeId;
    private String filterIs;
    private Map<String, String> filterInfo;
}
