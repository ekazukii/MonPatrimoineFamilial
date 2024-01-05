package fr.cytech.mpf.service.mergeTree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cytech.mpf.dto.MergeTreeDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    private Tree requestingTree;
    private Tree respondingTree;


    public List<Tree> mergeTrees(MergeTreeDTO mergeTreeDTO) throws MergeTreeException {

        // Récupération des infos utilisateurs
        User userRequester = userRepository.getReferenceById(mergeTreeDTO.getIdRequester());
        User userResponder = userRepository.getReferenceById(mergeTreeDTO.getIdResponder());

        // Récupération des arbres des 2 utilisateurs
        this.requestingTree = findTree(mergeTreeDTO.getRequestingTreeId());
        this.respondingTree = findTree(mergeTreeDTO.getRespondingTreeId());
      
        Node userNodeRequester = findUserNode(userRequester, requestingTree);
        Node userNodeResponder = findUserNode(userResponder, respondingTree);

        if(userNodeRequester == null || userNodeResponder == null) {
            throw new MergeTreeException("Error: Can't find connected user in one of both tree");
        }

        Node mergeNode = null;
        Node fakeMergeNode = null;
        // find common node in both tree 
        Boolean mergeStatus = false;
        for (Node nodeRequester : requestingTree.getNodes()) {
            for (Node nodeResponder : respondingTree.getNodes()) {
                if (nodeRequester.equals(nodeResponder)) {
                    mergeStatus = true;
                    mergeNode = nodeResponder;
                    fakeMergeNode = nodeRequester; // TO DO : change variable name
                }
            }
        }

        if(!mergeStatus){
            throw new MergeTreeException("Error: No matching node in your tree");
        }

        FamilyTree familyTreeAscending1 = new FamilyTree(fakeMergeNode, false);
        FamilyTree familyTreeAscending2 = new FamilyTree(mergeNode, false);
        this.mergeFamilyTrees(familyTreeAscending1, familyTreeAscending2);

        System.out.println("----------------------familyTreeAscending1-------------------");
        System.out.println(familyTreeAscending1);
        System.out.println("----------------------familyTreeAscending2-------------------");
        System.out.println(familyTreeAscending2);
        System.out.println("----------------------this-------------------");
        System.out.println(this);

        FamilyTree familyTreeDescending1 = new FamilyTree(fakeMergeNode, true);
        FamilyTree familyTreeDescending2 = new FamilyTree(mergeNode, true);
        this.mergeFamilyTrees(familyTreeDescending1, familyTreeDescending2);

        // Add star to tree 
        for (Node node : respondingTree.getNodes()) {
            if (node.equals(userNodeRequester)){
                node.setUserAccount(userRequester);
                nodeRepository.save(node);
            }
        }

        for (Node node : requestingTree.getNodes()) {
            if (node.equals(userNodeResponder)){
                node.setUserAccount(userRequester);
                nodeRepository.save(node);
            }
        }

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
                requestingTree, mergeNode.isMale(),
                mergeNode.getUserAccount()  
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

    private Node findUserNode(User user, Tree userTree) throws MergeTreeException {

        if (user == null || userTree == null) {
            throw new MergeTreeException("Error: Tree or User is null");
        }

        for (Node node : userTree.getNodes() ) {
            if (node.getUserAccount() == user) {
                return node;
            }
        }

        return null;
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
                                        this.requestingTree, f2.getParents()[i].isMale(),
                                        f2.getParents()[i].getUserAccount()));
                }
                if(f2 != null && f1 != null && f2.getParents()[i] == null) {
                    f2.getParents()[i] = nodeRepository.save(new Node(null, null,
                                        f1.getParents()[i] .getFirstName(), f1.getParents()[i] .getLastName(),
                                        f1.getParents()[i] .getBirthDate(), f1.getParents()[i] .getVisibility(), 
                                        this.respondingTree, f1.getParents()[i].isMale(),
                                        f1.getParents()[i].getUserAccount()));                    
                            
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
                                        this.requestingTree, child2.isMale(),
                                        child2.getUserAccount()
                                        )));
                }
                if(f2 != null && child2 == null) {
                    f2.getChildren().add(nodeRepository.save(new Node(f2.getParents()[0], f2.getParents()[1],
                                        child1.getFirstName(), child1.getLastName(),
                                        child1.getBirthDate(), child1.getVisibility(), 
                                        this.respondingTree, child1.isMale(),
                                        child1.getUserAccount()
                    )));                    

                            
                }
            }
        }

        //travel
        List<FamilyTree> f1ParentsFamilyTreesList = null;
        List<FamilyTree> f2ParentsFamilyTreesList = null;
        if(f1 != null) {
            f1ParentsFamilyTreesList = f1.travelParents();    
            //f1.travelChildren();
        }
        
        if(f2 != null) {
            f2ParentsFamilyTreesList = f2.travelParents();      
            //f2.travelChildren();   
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

