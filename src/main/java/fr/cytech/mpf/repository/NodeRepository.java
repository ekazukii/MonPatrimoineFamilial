package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.utils.NodeVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByTreeEquals(Tree tree);

    List<Node> findAllByTreeEqualsAndVisibilityIn(Tree tree, List<NodeVisibility> visibility);

    List<Node> findAllByFatherId(Long nodeId);

    List<Node> findAllByMotherId(Long nodeId);
}
