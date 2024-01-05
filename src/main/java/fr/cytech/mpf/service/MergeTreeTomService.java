package fr.cytech.mpf.service;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.utils.NodeVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class MergeTreeTomService {

    @Autowired
    private TreeRepository treeRepository;

    public Tree mergeTrees(Tree treeA, Tree treeB) {
        System.out.println("----------------------Début de la fusion-------------------");
        Tree mergedTree = new Tree();
        mergedTree.setName("Merged Tree");
        initializeNodesList(mergedTree);

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

        for(Node node : mergedTree.getNodes()){
            if(treeContainInfos(treeA, node)){

            }
        }

        treeRepository.save(mergedTree);
        return mergedTree;
    }
// Node(String firstName, String lastName, String birthDate, NodeVisibility visibility, Tree tree, boolean isMale, User userAccount) {
    private void addNodeToMergedTree(Tree mergedTree, Node node) {
        Node newNode = new Node(node.getFirstName(), node.getLastName(), node.getBirthDate(), NodeVisibility.Public, mergedTree, true);
        mergedTree.getNodes().add(newNode);
    }

    private void initializeNodesList(Tree tree) {
        if (tree.getNodes() == null) {
            tree.setNodes(new ArrayList<>());
        }
    }

    private boolean mergedTreeContainsNode(Tree mergedTree, Node node) {
        return mergedTree.getNodes().stream()
                .anyMatch(n -> areNodesEqual(node, n));
    }

    private boolean areNodesEqual(Node nodeA, Node nodeB) {
        return nodeA.getFirstName().equals(nodeB.getFirstName()) &&
                nodeA.getLastName().equals(nodeB.getLastName()) &&
                nodeA.getBirthDate().equals(nodeB.getBirthDate());
    }

    private boolean treeContainInfos(Tree Tree, Node node) {
        return Tree.getNodes().stream()
                .anyMatch(n -> (node.getFirstName().equals(n.getFirstName()) &&
                                node.getLastName().equals(n.getLastName()) &&
                                node.getBirthDate().equals(n.getBirthDate())));
    }
}
