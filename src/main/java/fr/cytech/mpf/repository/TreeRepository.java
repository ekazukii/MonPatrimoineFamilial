package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    @Query("SELECT t FROM Tree t LEFT JOIN Node n WHERE t.id = :id AND n.tree.id = t.id")
    Optional<Tree> findTreeByIdWithNodes(Long id);
}
