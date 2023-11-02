package fr.cytech.mpf.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tree implements Serializable {
    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "tree")
    private List<Node> nodes;

    private String name;
}
