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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Objects;

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
        System.out.println("----------------------");
        for (Node node : respondingTree.getNodes()){
            System.out.println(node.getFirstName() + " : " + node.getLastName() + " : " + node.getFather() + " : " + node.getMother() + " : id :" + node.getId());
        }

        List<Node> parentsNodesRequester = findNodes(mergeTreeDTO.getParentsNodesRequester());
        List<Node> childrenNodesRequester = findNodes(mergeTreeDTO.getChildrenNodesRequester());

        basicCheck(parentsNodesRequester, childrenNodesRequester, requestingTree, respondingTree);

        // Node mergeNode = findUserNodeTree(respondingTree);
        Node mergeNode = findNode(null); // Hardcoded node ID for merging.
        Node fakeMergeNode = createFakeMergeNode(parentsNodesRequester, mergeNode, requestingTree);

        nodeRepository.save(fakeMergeNode);
        this.requestingTree.getNodes().add(fakeMergeNode);
        treeRepository.save(requestingTree);

        // TO DO: find a way to not change the `healthy` nodes at start 
        if (childrenNodesRequester != null){
            for (Node child : childrenNodesRequester) {
                if(fakeMergeNode.isMale()){
                    child.setFather(fakeMergeNode);
                    nodeRepository.save(child);
                } else {
                    child.setMother(fakeMergeNode);
                    nodeRepository.save(child);
                }
            }
        }

        FamilyTree familyTreeAscending1 = new FamilyTree(fakeMergeNode, false);
        FamilyTree familyTreeAscending2 = new FamilyTree(mergeNode, false);
        this.mergeFamilyTrees(familyTreeAscending1, familyTreeAscending2);
        
        FamilyTree familyTreeDescending1 = new FamilyTree(fakeMergeNode, true);
        FamilyTree familyTreeDescending2 = new FamilyTree(mergeNode, true);
        this.mergeFamilyTrees(familyTreeDescending1, familyTreeDescending2);
        return new ArrayList<Tree>() {{ add(requestingTree); add(respondingTree);}};
    }





    private Tree findTree(Long id) throws MergeTreeException {
        return treeRepository.findById(id)
                .orElseThrow(() -> new MergeTreeException("Error: Cannot find tree id: " + id));
    }

    private Node findNode(UUID id) throws MergeTreeException {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new MergeTreeException("Error: node " + id + " is not present"));
    }

    private List<Node> findNodes(UUID[] ids) {

        if (ids == null){
            return null;
        }

        List<Node> nodes = new ArrayList<>();

        for(int i = 0 ; i < ids.length ; i++ ){
            if(ids[i] == null){
                nodes.add(null);
            } else {
                nodes.add(nodeRepository.getReferenceById(ids[i]));
            }
        }

        return nodes;
    }

    private Node createFakeMergeNode(List<Node> parentsNodesRequester, Node mergeNode, Tree requestingTree) {
        return new Node(
                parentsNodesRequester.get(0), parentsNodesRequester.get(1),
                mergeNode.getFirstName(), mergeNode.getLastName(),
                mergeNode.getBirthDate(), mergeNode.getVisibility(), 
                requestingTree, mergeNode.isMale()
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

        validateParentsNodes(parentsNodesRequester, requestingTree, respondingTree);
        validateChildrenNodes(childrenNodesRequester, requestingTree);

    }

    private void validateParentsNodes(List<Node> parents, Tree requestingTree, Tree respondingTree) throws MergeTreeException {
        if (parents == null) return;

        if (parents.size() != 2) {
            throw new MergeTreeException("Error: `parentsNodesRequester` should have two parents [one of them can be null]");
        }

        if (parents.stream().filter(Objects::nonNull).anyMatch(p -> !requestingTree.getNodes().contains(p))) {
            throw new MergeTreeException("Error: All non-null selected parents for the merge must be in the Tree.");
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

    



    public void mergeFamilyTrees(FamilyTree f1, FamilyTree f2) throws MergeTreeException {

        if (f1 == null && f2 == null) {
            return;
        } 

        //parents updater 
        for(int i =  0 ; i <  2 ; i++) {

            if((f1 != null && f1.getParents()[i] == null) && (f2 != null && f2.getParents()[i] == null)) {
                continue;
            }

            if((f1 != null && f1.getParents()[i] != null) && (f2 != null && f2.getParents()[i] != null)){
                if(!f1.getParents()[i].equals(f2.getParents()[i])) {
                    throw new MergeTreeException("Node are not equals");
                }
            } else {
               if(f1 !=  null && f2 != null &&  f1.getParents()[i] == null ) {
                    f1.getParents()[i] = nodeRepository.save(new Node(null, null,
                                        f2.getParents()[i].getFirstName(), f2.getParents()[i].getLastName(),
                                        f2.getParents()[i].getBirthDate(), f2.getParents()[i].getVisibility(), 
                                        this.requestingTree, f2.getParents()[i].isMale() ));
                }
                if(f2 != null && f1 != null && f2.getParents()[i] == null) {
                    f2.getParents()[i] = nodeRepository.save(new Node(null, null,
                                        f1.getParents()[i] .getFirstName(), f1.getParents()[i] .getLastName(),
                                        f1.getParents()[i] .getBirthDate(), f1.getParents()[i] .getVisibility(), 
                                        this.respondingTree, f1.getParents()[i].isMale() ));                    
                            
                }                
            } 

        }

        // set the list of pair children 1 / children 2
        List<Pair<Node, Node>>  childrenPair = null;
        if (f1 == null) {
            childrenPair = Pair.findPairs(null, f2.getChildren());
        } else if (f2 == null) {
            childrenPair = Pair.findPairs(f1.getChildren(), null);
        } else {
            childrenPair = Pair.findPairs(f1.getChildren(), f2.getChildren());
        }

        // update children pair
        for (Pair<Node, Node> pair : childrenPair) {

            Node child1 = pair.getFirst();
            Node child2 = pair.getSecond();

            if(child1 == null && child2 == null){
                continue;
            } 

            if(child1 != null && child2 != null){
                if(!child1.equals(child2)) {
                    throw new MergeTreeException("Node are note equals");
                }
            } else {
                if(f1 != null && child1 == null) {
                    f1.getChildren().add(nodeRepository.save(new Node(f1.getParents()[0], f1.getParents()[1],
                                        child2.getFirstName(), child2.getLastName(),
                                        child2.getBirthDate(), child2.getVisibility(), 
                                        this.requestingTree, child2.isMale())));
                }
                if(f2 != null && child2 == null) {
                    f2.getChildren().add(nodeRepository.save(new Node(f2.getParents()[0], f2.getParents()[1],
                                        child1.getFirstName(), child1.getLastName(),
                                        child1.getBirthDate(), child1.getVisibility(), 
                                        this.respondingTree, child1.isMale()
                    )));                    

                            
                }
            }
        }

        //travel
        List<FamilyTree> f1ParentsFamilyTreesList = null;
        List<FamilyTree> f2ParentsFamilyTreesList = null;
        if(f1 != null) {
            f1ParentsFamilyTreesList = f1.travelParents();     
            f1.travelChildren();
        }
        
        if(f2 != null) {
            f2ParentsFamilyTreesList = f2.travelParents();      
            f2.travelChildren();   
        }
        
        // i == 0 is is for family trees fathers, i == 1 is for family trees mothers
        for (int i = 0; i < 2; i++) {
            FamilyTree f1ParentFamilyTree = null;
            FamilyTree f2ParentFamilyTree = null; 
        
            if (f1ParentsFamilyTreesList != null && f1ParentsFamilyTreesList.size() > i) {
                f1ParentFamilyTree = f1ParentsFamilyTreesList.get(i);
            }
            
            if (f2ParentsFamilyTreesList != null && f2ParentsFamilyTreesList.size() > i) {
                f2ParentFamilyTree = f2ParentsFamilyTreesList.get(i);
            }

            this.mergeFamilyTrees(f1ParentFamilyTree, f2ParentFamilyTree);
        }
        
        //get the updated children pair
        if (f1 == null) {
            childrenPair = Pair.findPairs(null, f2.getChildren());
        } else if (f2 == null) {
            childrenPair = Pair.findPairs(f1.getChildren(), null);
        } else {
            childrenPair = Pair.findPairs(f1.getChildren(), f2.getChildren());
        }
        
        //get f1 & f2 family trees from children
        if(childrenPair != null && (f1 != null || f2 != null)) {
            for (Pair<Node, Node> pair : childrenPair) {
                if(f1 == null) {
                    mergeFamilyTrees(null, f2.travelChildren(pair.getSecond()));
                } else if(f2 ==  null) {
                    mergeFamilyTrees(f1.travelChildren(pair.getFirst()), null);
                } else {
                    mergeFamilyTrees(f1.travelChildren(pair.getFirst()), f2.travelChildren(pair.getSecond()));
                }

            }
        }

        //update parents of children 
        if (f1 != null) {
            for (Node child : f1.getChildren()) {
                child.setFather(f1.getParents()[0]);
                child.setMother(f1.getParents()[1]);
                nodeRepository.save(child);
            }
        }
        if (f2 != null) {
            for (Node child : f2.getChildren()) {
                child.setFather(f2.getParents()[0]);
                child.setMother(f2.getParents()[1]);
                nodeRepository.save(child);
            }
        }
        
    }
}

