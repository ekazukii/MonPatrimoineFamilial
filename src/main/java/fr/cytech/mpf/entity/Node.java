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
import java.util.UUID;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "node")
public class Node implements Serializable {
    @Id @Column(name = "id")
    private UUID id;

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
        this.id = UUID.randomUUID();
    }

    public Node(String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree, boolean isMale) {
        this(firstName, lastName, birthDate, visibility, tree);
        this.male = isMale;
    }

    public Node(String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree, boolean isMale, User userAccount) {
        this(firstName, lastName, birthDate, visibility, tree, isMale);
        this.userAccount = userAccount;
    }

    public Node(Node father, Node mother, String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree) {
        this(firstName, lastName, birthDate, visibility, tree);
        this.father = father;
        this.mother = mother;
    }

    public Node(Node father, Node mother, String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree, boolean isMale, User userAccount) {
        this(father, mother, firstName, lastName, birthDate, visibility, tree);
        this.male = isMale;
        this.userAccount = userAccount;
    }

    public Node() {

    }


    @Override
    public String toString() {
        return this.firstName + " : " + this.lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return this.firstName.equals(other.getFirstName()) && this.lastName.equals(other.getLastName()) 
            && this.birthDate.equals(other.getBirthDate()) && this.male == other.isMale();
    }

    
}
