package fr.cytech.mpf.service.mergeTree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cytech.mpf.dto.MergeTreeDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.service.CustomDTOMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MergeTreeService {

    @Autowired
    CustomDTOMapper customDTOMapper;

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    private Tree requestingTree;
    private Tree respondingTree;



    public List<Tree> mergeTrees(MergeTreeDTO mergeTreeDTO) throws MergeTreeException {
        this.requestingTree = findTree(mergeTreeDTO.getRequestingTreeId());
        this.respondingTree = findTree(mergeTreeDTO.getRespondingTreeId());

        for (Node node : requestingTree.getNodes()){
            System.out.println(node.getFirstName() + " : " + node.getLastName() + " : " + node.getFather() + " : " + node.getMother() + " : id :" + node.getId());
        }

        List<Node> parentsNodesRequester = findNodes(mergeTreeDTO.getParentsNodesRequester());
        List<Node> childrenNodesRequester = findNodes(mergeTreeDTO.getChildrenNodesRequester());
        
        basicCheck(parentsNodesRequester, childrenNodesRequester, requestingTree, respondingTree);
        // Node mergeNode = findUserNodeTree(respondingTree);
        Node mergeNode = findNode(9L); // Hardcoded node ID for merging.
        Node fakeMergeNode = createFakeMergeNode(parentsNodesRequester, mergeNode, requestingTree);
        deepCheck(fakeMergeNode, mergeNode);

        nodeRepository.save(fakeMergeNode);
        return new ArrayList<Tree>() {{ add(requestingTree); add(respondingTree);}};
    }

    
    private Tree findTree(Long id) throws MergeTreeException {
        return treeRepository.findById(id)
                .orElseThrow(() -> new MergeTreeException("Error: Cannot find tree id: " + id));
    }

    private Node findNode(Long id) throws MergeTreeException {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new MergeTreeException("Error: node " + id + " is not present"));
    }

    private List<Node> findNodes(Long[] ids) {
        return ids == null ? null : Arrays.stream(ids)
                .map(nodeRepository::getReferenceById)
                .collect(Collectors.toList());
    }

    private Node createFakeMergeNode(List<Node> parentsNodesRequester, Node mergeNode, Tree requestingTree) {
        return new Node(
                parentsNodesRequester.get(0), parentsNodesRequester.get(1),
                mergeNode.getFirstName(), mergeNode.getLastName(),
                mergeNode.getBirthDate(), mergeNode.getVisibility(), requestingTree
        );
    }

    private Node findUserNodeTree(Tree tree) throws MergeTreeException{
        if (tree.getOwner() == null ) {
            throw new MergeTreeException("Error: Tree user is not defined for tree " + tree.getId());
        }
        for (Node node : tree.getNodes()){
            if (node.getUserAccount() == tree.getOwner()){
                return node;
            }
        }
        return null;
    }





    public void basicCheck(List<Node> parentsNodesRequester, List<Node> childrenNodesRequester,
                           Tree requestingTree, Tree respondingTree) throws MergeTreeException {
        validateParentsNodes(parentsNodesRequester, requestingTree);
        validateChildrenNodes(childrenNodesRequester, requestingTree);
    }

    private void validateParentsNodes(List<Node> parents, Tree tree) throws MergeTreeException {
        if (parents == null) return;

        if (parents.size() > 2) {
            throw new MergeTreeException("Error: Maximum of 2 parents allowed.");
        }

        // for (Node node : tree.getNodes()){
        //     System.out.println(node.getFirstName() + ", " + node.getLastName());
        // }

        if (!parents.stream().allMatch(p -> tree.getNodes().contains(p))) {
            throw new MergeTreeException("Error: All selected parents for the merge must be in the Tree.");
        }
    }

    private void validateChildrenNodes(List<Node> children, Tree tree) throws MergeTreeException {
        if (children == null) return;

        if (children.isEmpty()) {
            throw new MergeTreeException("Error: At least one child is needed for merging.");
        }

        if (!children.stream().allMatch(c -> tree.getNodes().contains(c))) {
            throw new MergeTreeException("Error: All selected children for the merge must be in the Tree.");
        }
    }





    public Node deepCheck(Node node1, Node node2) throws MergeTreeException{

        if(node1 == null && node2 == null){
            return null;
        }

        if(node1 != null && node2 != null){
            checkNodeAttributes(node1, node2);
            checkAndSetParent(node1, node2, true);
            checkAndSetParent(node1, node2, false);
        } else {
            if(node1 == null) {
                Node father = deepCheck(null, node2.getFather());
                Node mother = deepCheck(null,node2.getMother());
                return nodeRepository.save(new Node(father, mother,
                        node2.getFirstName(), node2.getLastName(),
                        node2.getBirthDate(), node2.getVisibility(), this.requestingTree));
            }
            if(node2 == null) {
                Node father = deepCheck(node1.getFather(), null);
                Node mother = deepCheck(node1.getMother(),null);
                return nodeRepository.save(new Node(father, mother,
                        node1.getFirstName(), node1.getLastName(),
                        node1.getBirthDate(), node1.getVisibility(), this.respondingTree));
            }
        }
        return null;
    }

    private void checkNodeAttributes(Node node1, Node node2) throws MergeTreeException {
        if (!node1.getFirstName().equals(node2.getFirstName())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_FIRSTNAME");
        }

        if (!node1.getLastName().equals(node2.getLastName())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_LASTNAME");
        }

        if (!node1.getBirthDate().equals(node2.getBirthDate())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_BIRTHDATE");
        }
    }

    private void checkAndSetParent(Node node1, Node node2, boolean isFather) throws MergeTreeException {
        
        Node fakeParent = (node1 != null && isFather) ? node1.getFather() : (node1 != null) ? node1.getMother() : null;
        Node mergeParent = (node2 != null && isFather) ? node2.getFather() : (node2 != null) ? node2.getMother() : null;
        
        if ((fakeParent != null && mergeParent != null) || (fakeParent == null && mergeParent == null)) {
            deepCheck(fakeParent, mergeParent);
        } else {
            if (fakeParent == null) {
                Node parent = deepCheck(null, mergeParent);
                if (isFather) {
                    node1.setFather(parent);
                } else {
                    node1.setMother(parent);
                }
            }
            if (mergeParent == null ) {
                Node parent = deepCheck(fakeParent, null);
                if (isFather) {
                    node2.setFather(parent);
                } else {
                    node2.setMother(parent);
                }
            }
        }
    }

}

