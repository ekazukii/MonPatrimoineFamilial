package fr.cytech.mpf.dto;

import lombok.Setter;
import lombok.Getter;
import fr.cytech.mpf.entity.User;
import java.io.Serializable;


@Getter
@Setter
public class TreeViewGetDTO {
    private User viewer;
    private int total;

    public TreeViewGetDTO(User viewer, int total) {
        this.viewer = viewer;
        this.total = total;
    }

    public void incrementTotal() {
        total++;
    }
}
