package fr.cytech.mpf.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TreeView {
    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private User viewer;

    @JsonIgnore
    @ManyToOne
    private Tree tree;
}
