package fr.cytech.mpf.dto;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.utils.NodeVisibility;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
public class NodeAddDTO implements Serializable {
    private Long treeId;
    private String firstName;
    private String lastName;
    private String birthDate;
    private boolean male;
    private Long fatherId;
    private Long motherId;
    private NodeVisibility nodeVisibility;
}
