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

    /**
     * Find all the nodes of a tree with a specific visibility
     * @param tree the tree
     * @param visibility the visibility
     * @return a list of nodes
     */
    List<Node> findAllByTreeEqualsAndVisibilityIn(Tree tree, List<NodeVisibility> visibility);

    /**
     * Find all the nodes that has a specific father
     * @param nodeId the node id
     * @return a list of nodes
     */
    List<Node> findAllByFatherId(UUID nodeId);

    /**
     * Find all the nodes that has a specific mother
     * @param nodeId the node id
     * @return a list of nodes
     */
    List<Node> findAllByMotherId(UUID nodeId);

    /**
     * Find all the nodes of a specific user account and not in a specific tree
     * @param userAccountId the user account id
     * @param treeId the tree id
     * @return a list of nodes
     */
    public List<Node> findAllByTreeAndFirstNameAndLastName(Tree tree, String firstName, String lastName);

    List<Node> findByUserAccountIdAndTreeIdNot(Long userAccountId, Long treeId);

    List<Node> findByUserAccountIdAndTreeId(Long userAccountId, Long treeId);

    List<Node> findByUserAccountId(Long userAccountId);

    @Query("SELECT DISTINCT n.userAccount.Id FROM Node n WHERE n.tree.id = :treeId AND n.userAccount.Id IS NOT NULL")
    List<Long> findDistinctUserAccountIdsByTreeId(@Param("treeId") Long treeId);

    @Query("SELECT COUNT(n) FROM Node n WHERE n.tree.id = :treeId AND n.userAccount.Id = :userToSearchId")
    int countNodesByTreeIdAndUserAccountId(@Param("treeId") Long treeId, @Param("userToSearchId") Long userToSearchId);

}
