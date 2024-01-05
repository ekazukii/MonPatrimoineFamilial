package fr.cytech.mpf.repository;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.utils.NodeVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NodeRepository extends JpaRepository<Node, UUID> {
    List<Node> findAllByTreeEquals(Tree tree);

    List<Node> findAllByTreeEqualsAndVisibilityIn(Tree tree, List<NodeVisibility> visibility);

    List<Node> findAllByFatherId(UUID nodeId);

    List<Node> findAllByMotherId(UUID nodeId);

    List<Node> findByUserAccountIdAndTreeIdNot(Long userAccountId, Long treeId);

    @Query("SELECT DISTINCT n.userAccount.Id FROM Node n WHERE n.tree.id = :treeId AND n.userAccount.Id IS NOT NULL")
    List<Long> findDistinctUserAccountIdsByTreeId(@Param("treeId") Long treeId);

    @Query("SELECT COUNT(n) FROM Node n WHERE n.tree.id = :treeId AND n.userAccount.Id = :userToSearchId")
    int countNodesByTreeIdAndUserAccountId(@Param("treeId") Long treeId, @Param("userToSearchId") Long userToSearchId);
}
