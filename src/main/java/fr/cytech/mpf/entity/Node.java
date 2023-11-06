package fr.cytech.mpf.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.cytech.mpf.utils.NodeVisibility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "node")
public class Node implements Serializable {
    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tree_id", referencedColumnName = "id")
    private Tree tree;

    private NodeVisibility visibility;

    @ManyToOne
    private User userAccount;

    private String firstName;

    private String lastName;

    private String birthDate;

    private boolean male;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Node mother;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Node father;

    public Node(String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.visibility = visibility;
        this.tree = tree;
    }

    public Node(String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree, boolean isMale) {
        this(firstName, lastName, birthDate, visibility, tree);
        this.male = male;
    }

    public Node(Node father, Node mother, String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree) {
        this(firstName, lastName, birthDate, visibility, tree);
        this.father = father;
        this.mother = mother;
    }

    public Node() {

    }
}
