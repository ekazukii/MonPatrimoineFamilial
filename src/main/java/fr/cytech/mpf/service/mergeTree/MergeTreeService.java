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

        for (Node node : tree.getNodes()){
            System.out.println(node.getFirstName() + ", " + node.getLastName());
        }

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





    public Node deepCheck(Node fakeMergeNode, Node mergeNode) throws MergeTreeException{

        if(fakeMergeNode == null && mergeNode == null){
            return null;
        }

        if(fakeMergeNode != null && mergeNode != null){
            checkNodeAttributes(fakeMergeNode, mergeNode);
            checkAndSetParent(fakeMergeNode, mergeNode, true);
            checkAndSetParent(fakeMergeNode, mergeNode, false);
        } else {
            if(fakeMergeNode == null) {
                Node father = deepCheck(null, mergeNode.getFather());
                Node mother = deepCheck(null,mergeNode.getMother());
                return nodeRepository.save(new Node(father, mother,
                        mergeNode.getFirstName(), mergeNode.getLastName(),
                        mergeNode.getBirthDate(), mergeNode.getVisibility(), this.requestingTree));
            }
            if(mergeNode == null) {
                Node father = deepCheck(fakeMergeNode.getFather(), null);
                Node mother = deepCheck(fakeMergeNode.getMother(),null);
                return nodeRepository.save(new Node(father, mother,
                        fakeMergeNode.getFirstName(), fakeMergeNode.getLastName(),
                        fakeMergeNode.getBirthDate(), fakeMergeNode.getVisibility(), this.respondingTree));
            }
        }
        return null;
    }

    private void checkNodeAttributes(Node fakeNode, Node realNode) throws MergeTreeException {
        if (!fakeNode.getFirstName().equals(realNode.getFirstName())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_FIRSTNAME");
        }

        if (!fakeNode.getLastName().equals(realNode.getLastName())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_LASTNAME");
        }

        if (!fakeNode.getBirthDate().equals(realNode.getBirthDate())) {
            throw new MergeTreeException("CONFLICTS: NOT_THE_SAME_BIRTHDATE");
        }
    }

    private void checkAndSetParent(Node fakeMergeNode, Node mergeNode, boolean isFather) throws MergeTreeException {
        
        Node fakeParent = (fakeMergeNode != null && isFather) ? fakeMergeNode.getFather() : (fakeMergeNode != null) ? fakeMergeNode.getMother() : null;
        Node mergeParent = (mergeNode != null && isFather) ? mergeNode.getFather() : (mergeNode != null) ? mergeNode.getMother() : null;
        
        if ((fakeParent != null && mergeParent != null) || (fakeParent == null && mergeParent == null)) {
            deepCheck(fakeParent, mergeParent);
        } else {
            if (fakeParent == null) {
                Node parent = deepCheck(null, mergeParent);
                if (isFather) {
                    fakeMergeNode.setFather(parent);
                } else {
                    fakeMergeNode.setMother(parent);
                }
            }
            if (mergeParent == null ) {
                Node parent = deepCheck(fakeParent, null);
                if (isFather) {
                    mergeNode.setFather(parent);
                } else {
                    mergeNode.setMother(parent);
                }
            }
        }
    }

}

