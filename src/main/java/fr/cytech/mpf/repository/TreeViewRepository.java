package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.TreeView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeViewRepository extends JpaRepository<TreeView, Long> {
    List<TreeView> getAllByTree(Tree tree);

    /**
     * Get all the views of a tree
     * @param id the tree id
     * @return a list of views
     */
    List<TreeView> getAllByTreeId(Long id);
}
