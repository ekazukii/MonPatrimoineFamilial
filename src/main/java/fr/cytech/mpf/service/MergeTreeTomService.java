package fr.cytech.mpf.service;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.UserRepository;
import fr.cytech.mpf.utils.NodeVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MergeTreeTomService {

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    public Tree mergeTrees(Tree treeA, Tree treeB) {
        Tree mergedTree = new Tree();
        mergedTree.setName("Merged Tree");
        initializeNodesList(mergedTree);
        treeRepository.save(mergedTree);

        // MyFamilyTree not supported
//        System.out.println("----------------------Recherche d'une FamilyTree déjà existante-------------------");
//        if(getFirstFamilyTree(treeA, treeB) == null){
//            System.out.println("null");
//        }else{
//            System.out.println(getFirstFamilyTree(treeA, treeB).getName());
//        }
//        if (getFirstFamilyTree(treeA, treeB) != null) {
//            mergedTree = getFirstFamilyTree(treeA, treeB);
//        } else {
//            mergedTree = new Tree();
//            mergedTree.setName("Merged Tree");
//            initializeNodesList(mergedTree);
//            treeRepository.save(mergedTree);
//        }
        System.out.println("----------------------Initialisation de FamilyTree-------------------");

        for (Node nodeA : treeA.getNodes()) {
            System.out.println("----------------------NodeA à ajouter-------------------");
            System.out.println("Personne == " + nodeA.getFirstName() + " " + nodeA.getLastName() + "Pere == " + nodeA.getFather()+ " , Mere == " + nodeA.getMother());
            addNodeToMergedTree(mergedTree, nodeA);
        }

        for (Node nodeB : treeB.getNodes()) {
            if (!mergedTreeContainsNode(mergedTree, nodeB)) {
                System.out.println("----------------------NodeB à ajouter-------------------");
                System.out.println("Personne == " + nodeB.getFirstName() + " " + nodeB.getLastName() + "Pere == " + nodeB.getFather()+ " , Mere == " + nodeB.getMother());
                addNodeToMergedTree(mergedTree, nodeB);
            }
        }

        System.out.println("----------------------Modifications des parents-------------------");
        for(Node node : mergedTree.getNodes()){
            System.out.println("----------------------"+node.getFirstName()+" "+node.getLastName()+"-------------------");
            Node currentA = getNodeFromTree(treeA, node);
            if(currentA != null && currentA.getFather() != null && currentA.getMother() != null){
                System.out.println("----------------------Trouvé dans Arbre 1-------------------");
                Node father = getNodeFromTree(mergedTree, currentA.getFather());
                Node mother = getNodeFromTree(mergedTree, currentA.getMother());
                if(father != null){
                    node.setFather(father);
                }
                if(mother != null){
                    node.setMother(mother);
                }
            }else{
                Node currentB = getNodeFromTree(treeB, node);
                System.out.println("----------------------Trouvé dans Arbre 2-------------------");
                if(currentB != null && currentB.getFather() != null && currentB.getMother() != null){
                    System.out.println("----------------------Trouvé dans Arbre 2-------------------");
                    Node father = getNodeFromTree(mergedTree, currentB.getFather());
                    Node mother = getNodeFromTree(mergedTree, currentB.getMother());
                    if(father != null){
                        node.setFather(father);
                    }
                    if(mother != null){
                        node.setMother(mother);
                    }
                }
            }
            System.out.println("Ajout du compte utilisateur");
            System.out.println(getUserAccount(treeA, treeB, node));
            node.setUserAccount(getUserAccount(treeA, treeB, node));
            System.out.println("----------------------Suivant-------------------");
        }

        // MyFamilyTree not supported
        // nodeRepository.saveAll(mergedTree.getNodes());
        // for(User user : getNonNullUsersFromTree(mergedTree)){
        //    user.setMyFamilyTree(mergedTree);
        //    userRepository.save(user);
        //}
        updateTreeWithMerged(mergedTree, treeA);
        return treeA;
    }

    public Boolean findCommonNodes(Tree treeA, Tree treeB) {
        for (Node nodeA : treeA.getNodes()) {
            for (Node nodeB : treeB.getNodes()) {
                if (areNodesEqual(nodeA, nodeB)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void addNodeToMergedTree(Tree mergedTree, Node node) {
        Node newNode = new Node(node.getFirstName(), node.getLastName(), node.getBirthDate(), NodeVisibility.Public, mergedTree, node.isMale());
        mergedTree.getNodes().add(newNode);
        // MyFamilyTree not supported
        // nodeRepository.save(newNode);
    }

    private void initializeNodesList(Tree tree) {
        if (tree.getNodes() == null) {
            tree.setNodes(new ArrayList<>());
        }
    }

    public boolean mergedTreeContainsNode(Tree mergedTree, Node node) {
        return mergedTree.getNodes().stream()
                .anyMatch(n -> areNodesEqual(node, n));
    }

    private boolean areNodesEqual(Node nodeA, Node nodeB) {
        return nodeA.getFirstName().equals(nodeB.getFirstName()) &&
                nodeA.getLastName().equals(nodeB.getLastName()) &&
                nodeA.getBirthDate().equals(nodeB.getBirthDate());
    }

    private Node getNodeFromTree(Tree tree, Node node) {
        return tree.getNodes().stream()
                .filter(n -> areNodesEqual(node, n))
                .findFirst()
                .orElse(null); // Retourne null si aucun nœud correspondant n'est trouvé
    }

    private User getUserAccount(Tree treeA, Tree treeB, Node node){
        Node userNodeA = getNodeFromTree(treeA, node);
        Node userNodeB = getNodeFromTree(treeB, node);
        if(userNodeA != null){
            if(userNodeA.getUserAccount() != null){
                return userNodeA.getUserAccount();
            }
        }
        if(userNodeB != null){
            return userNodeB.getUserAccount();
        }
        return null;
    }

    private List<User> getNonNullUsersFromTree(Tree tree) {
        List<User> users = new ArrayList<>();
        for (Node node : tree.getNodes()) {
            if (node.getUserAccount() != null) {
                users.add(node.getUserAccount());
            }
        }
        return users;
    }

//    private void updateTreeWithMerged(Tree treeMerged, Tree treeToUpdate){
//        for(Node nodeMerged : treeMerged.getNodes()){
//            Node nodeToUpdate;
//            if(!mergedTreeContainsNode(treeToUpdate, nodeMerged)){
//                addNodeToMergedTree(treeToUpdate, nodeMerged);
//                nodeRepository.saveAll(treeToUpdate.getNodes());
//            }
//            nodeToUpdate = getNodeFromTree(treeMerged, nodeMerged);
//            System.out.println("------------------"+nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " -------------------");
//            if(nodeMerged.getFather() != null){
//                System.out.println(nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " - Rentre dans le IF Pere");
//                if(mergedTreeContainsNode(treeToUpdate, nodeMerged.getFather())){
//                    System.out.println("L'abre contient déjà le père :" + nodeMerged.getFather().getFirstName() + " " + nodeMerged.getFather().getLastName());
//                    System.out.println("Le noeud père récupéré :" + getNodeFromTree(treeToUpdate, nodeMerged.getFather()).getFirstName() + " " + getNodeFromTree(treeToUpdate, nodeMerged.getFather()).getLastName());
//                    nodeToUpdate.setFather(getNodeFromTree(treeToUpdate, nodeMerged.getFather()));
//                }else{
//                    addNodeToMergedTree(treeToUpdate, nodeMerged.getFather());
//                    nodeToUpdate.setFather(nodeMerged.getFather());
//                }
//                nodeRepository.saveAll(treeToUpdate.getNodes());
//            }
//            if(nodeMerged.getMother() != null){
//                System.out.println(nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " - Rentre dans le IF Mere");
//                if(mergedTreeContainsNode(treeToUpdate, nodeMerged.getMother())){
//                    System.out.println("L'abre contient déjà la mère :" + nodeMerged.getMother().getFirstName() + " " + nodeMerged.getMother().getLastName());
//                    nodeToUpdate.setMother(getNodeFromTree(treeToUpdate, nodeMerged.getMother()));
//                }else{
//                    addNodeToMergedTree(treeToUpdate, nodeMerged.getMother());
//                    nodeToUpdate.setMother(nodeMerged.getMother());
//                }
//                nodeRepository.saveAll(treeToUpdate.getNodes());
//            }
//            nodeRepository.saveAll(treeToUpdate.getNodes());
//            System.out.println("----------------------Suivant-------------------");
//        }
//    }
private void updateTreeWithMerged(Tree treeMerged, Tree treeToUpdate) {
    for (Node nodeMerged : treeMerged.getNodes()) {
        Node nodeToUpdate;

        // Vérifie si le nœud existe déjà dans l'arbre à mettre à jour
        if (!mergedTreeContainsNode(treeToUpdate, nodeMerged)) {
            addNodeToMergedTree(treeToUpdate, nodeMerged);
        }

        // Récupère le nœud à mettre à jour
        nodeToUpdate = getNodeFromTree(treeToUpdate, nodeMerged);

        // Affiche des informations de débogage
        System.out.println("------------------" + nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " -------------------");

        // Mise à jour du père si présent
        if (nodeMerged.getFather() != null) {
            System.out.println(nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " - Rentre dans le IF Pere");

            if (mergedTreeContainsNode(treeToUpdate, nodeMerged.getFather())) {
                System.out.println("L'arbre contient déjà le père :" + nodeMerged.getFather().getFirstName() + " " + nodeMerged.getFather().getLastName());
                System.out.println("Le nœud père récupéré :" + getNodeFromTree(treeToUpdate, nodeMerged.getFather()).getFirstName() + " " + getNodeFromTree(treeToUpdate, nodeMerged.getFather()).getLastName());

            } else {
                addNodeToMergedTree(treeToUpdate, nodeMerged.getFather());
                nodeRepository.saveAll(treeToUpdate.getNodes());
            }
            nodeToUpdate.setFather(getNodeFromTree(treeToUpdate, nodeMerged.getFather()));
        }

        // Mise à jour de la mère si présente
        if (nodeMerged.getMother() != null) {
            System.out.println(nodeToUpdate.getFirstName() + " " + nodeToUpdate.getLastName() + " - Rentre dans le IF Mere");

            if (mergedTreeContainsNode(treeToUpdate, nodeMerged.getMother())) {
                System.out.println("L'arbre contient déjà la mère :" + nodeMerged.getMother().getFirstName() + " " + nodeMerged.getMother().getLastName());

            } else {
                addNodeToMergedTree(treeToUpdate, nodeMerged.getMother());
                nodeRepository.saveAll(treeToUpdate.getNodes());
            }
            nodeToUpdate.setMother(getNodeFromTree(treeToUpdate, nodeMerged.getMother()));
        }
        nodeToUpdate.setUserAccount(nodeMerged.getUserAccount());
        // Sauvegarde tous les nœuds à la fin de chaque itération
        nodeRepository.saveAll(treeToUpdate.getNodes());

        // Affiche des informations de débogage
        System.out.println("----------------------Suivant-------------------");
    }
}

    // MyFamilyTree not supported
//    private Tree getFirstFamilyTree(Tree treeA, Tree treeB){
//        for(User user : getNonNullUsersFromTree(treeA)){
//            if(user.getMyFamilyTree() != null){
//                return user.getMyFamilyTree();
//            }
//        }
//        for(User user : getNonNullUsersFromTree(treeB)){
//            if(user.getMyFamilyTree() != null){
//                return user.getMyFamilyTree();
//            }
//        }
//        return null;
//    }
}


//package fr.cytech.mpf.service;
//
//import fr.cytech.mpf.entity.Node;
//import fr.cytech.mpf.entity.Tree;
//import fr.cytech.mpf.entity.User;
//import fr.cytech.mpf.repository.NodeRepository;
//import fr.cytech.mpf.repository.TreeRepository;
//import fr.cytech.mpf.repository.UserRepository;
//import fr.cytech.mpf.utils.NodeVisibility;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MergeTreeTomService {
//
//    @Autowired
//    private TreeRepository treeRepository;
//
//    @Autowired
//    private NodeRepository nodeRepository;
//
//    public Tree mergeTrees(Tree treeA, Tree treeB) {
//        Tree mergedTree = new Tree();
//        mergedTree.setName("Merged Tree");
//        initializeNodesList(mergedTree);
//        treeRepository.save(mergedTree);
//
//        // MyFamilyTree not supported
////        System.out.println("----------------------Recherche d'une FamilyTree déjà existante-------------------");
////        if(getFirstFamilyTree(treeA, treeB) == null){
////            System.out.println("null");
////        }else{
////            System.out.println(getFirstFamilyTree(treeA, treeB).getName());
////        }
////        if (getFirstFamilyTree(treeA, treeB) != null) {
////            mergedTree = getFirstFamilyTree(treeA, treeB);
////        } else {
////            mergedTree = new Tree();
////            mergedTree.setName("Merged Tree");
////            initializeNodesList(mergedTree);
////            treeRepository.save(mergedTree);
////        }
//        System.out.println("----------------------Initialisation de FamilyTree-------------------");
//
//        for (Node nodeA : treeA.getNodes()) {
//            System.out.println("----------------------NodeA à ajouter-------------------");
//            System.out.println("Personne == " + nodeA.getFirstName() + " " + nodeA.getLastName() + "Pere == " + nodeA.getFather()+ " , Mere == " + nodeA.getMother());
//            addNodeToMergedTree(mergedTree, nodeA);
//        }
//
//        for (Node nodeB : treeB.getNodes()) {
//            if (!mergedTreeContainsNode(mergedTree, nodeB)) {
//                System.out.println("----------------------NodeB à ajouter-------------------");
//                System.out.println("Personne == " + nodeB.getFirstName() + " " + nodeB.getLastName() + "Pere == " + nodeB.getFather()+ " , Mere == " + nodeB.getMother());
//                addNodeToMergedTree(mergedTree, nodeB);
//            }
//        }
//
//        System.out.println("----------------------Modifications des parents-------------------");
//        for(Node node : mergedTree.getNodes()){
//            System.out.println("----------------------"+node.getFirstName()+" "+node.getLastName()+"-------------------");
//            Node currentA = getNodeFromTree(treeA, node);
//            if(currentA != null && currentA.getFather() != null && currentA.getMother() != null){
//                System.out.println("----------------------Trouvé dans Arbre 1-------------------");
//                Node father = getNodeFromTree(mergedTree, currentA.getFather());
//                Node mother = getNodeFromTree(mergedTree, currentA.getMother());
//                if(father != null){
//                    node.setFather(father);
//                }
//                if(mother != null){
//                    node.setMother(mother);
//                }
//            }else{
//                Node currentB = getNodeFromTree(treeB, node);
//                System.out.println("----------------------Trouvé dans Arbre 2-------------------");
//                if(currentB != null && currentB.getFather() != null && currentB.getMother() != null){
//                    System.out.println("----------------------Trouvé dans Arbre 2-------------------");
//                    Node father = getNodeFromTree(mergedTree, currentB.getFather());
//                    Node mother = getNodeFromTree(mergedTree, currentB.getMother());
//                    if(father != null){
//                        node.setFather(father);
//                    }
//                    if(mother != null){
//                        node.setMother(mother);
//                    }
//                }
//            }
//            node.setUserAccount(getUserAccount(treeA, treeB, node));
//            System.out.println("----------------------Suivant-------------------");
//        }
//
//        // MyFamilyTree not supported
//         nodeRepository.saveAll(mergedTree.getNodes());
//        // for(User user : getNonNullUsersFromTree(mergedTree)){
//        //    user.setMyFamilyTree(mergedTree);
//        //    userRepository.save(user);
//        //}
//
//        return mergedTree;
//    }
//
//    private void addNodeToMergedTree(Tree mergedTree, Node node) {
//        Node newNode = new Node(node.getFirstName(), node.getLastName(), node.getBirthDate(), NodeVisibility.Public, mergedTree, node.isMale());
//        mergedTree.getNodes().add(newNode);
//        // MyFamilyTree not supported
//        nodeRepository.save(newNode);
//    }
//
//    private void initializeNodesList(Tree tree) {
//        if (tree.getNodes() == null) {
//            tree.setNodes(new ArrayList<>());
//        }
//    }
//
//    private boolean mergedTreeContainsNode(Tree mergedTree, Node node) {
//        return mergedTree.getNodes().stream()
//                .anyMatch(n -> areNodesEqual(node, n));
//    }
//
//    private boolean areNodesEqual(Node nodeA, Node nodeB) {
//        return nodeA.getFirstName().equals(nodeB.getFirstName()) &&
//                nodeA.getLastName().equals(nodeB.getLastName()) &&
//                nodeA.getBirthDate().equals(nodeB.getBirthDate());
//    }
//
//    private Node getNodeFromTree(Tree tree, Node node) {
//        return tree.getNodes().stream()
//                .filter(n -> areNodesEqual(node, n))
//                .findFirst()
//                .orElse(null); // Retourne null si aucun nœud correspondant n'est trouvé
//    }
//
//    private User getUserAccount(Tree treeA, Tree treeB, Node node){
//        Node userNodeA = getNodeFromTree(treeA, node);
//        Node userNodeB = getNodeFromTree(treeB, node);
//        if(userNodeA != null){
//            if(userNodeA.getUserAccount() != null){
//                return userNodeA.getUserAccount();
//            }
//        }
//        if(userNodeB != null){
//            return userNodeB.getUserAccount();
//        }
//        return null;
//    }
//
//    private List<User> getNonNullUsersFromTree(Tree tree) {
//        List<User> users = new ArrayList<>();
//        for (Node node : tree.getNodes()) {
//            if (node.getUserAccount() != null) {
//                users.add(node.getUserAccount());
//            }
//        }
//        return users;
//    }
//
//    // MyFamilyTree not supported
////    private Tree getFirstFamilyTree(Tree treeA, Tree treeB){
////        for(User user : getNonNullUsersFromTree(treeA)){
////            if(user.getMyFamilyTree() != null){
////                return user.getMyFamilyTree();
////            }
////        }
////        for(User user : getNonNullUsersFromTree(treeB)){
////            if(user.getMyFamilyTree() != null){
////                return user.getMyFamilyTree();
////            }
////        }
////        return null;
////    }
//}
