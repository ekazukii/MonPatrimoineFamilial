package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    /**
     * Find a tree by id with all its nodes
     * @param id the tree id
     * @return the tree
     */
    @Query("SELECT t FROM Tree t LEFT JOIN Node n ON n.tree.id = t.id WHERE t.id = :id")
    Optional<Tree> findTreeByIdWithNodes(Long id);

    Tree findTreeByOwnerId(Long id);
}
