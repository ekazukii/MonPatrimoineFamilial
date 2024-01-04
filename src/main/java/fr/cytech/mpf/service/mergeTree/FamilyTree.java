package fr.cytech.mpf.service.mergeTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FamilyTree {
    
    private Node instigatorNode;
    private boolean instigatorNodeIsParent; //false : is child 
    private Node[] parents = new Node[2];
    private List<Node> children;
    private Tree tree;

    public FamilyTree(Node node, boolean isParent) throws MergeTreeException{

        if (node == null) {
            throw new MergeTreeException("Error : in FamilyTree(Node node, boolean isParent) Node must not be null");
        }

        this.instigatorNode = node;
        this.instigatorNodeIsParent = isParent;
        this.tree = node.getTree();

        if(this.instigatorNodeIsParent) {

            if(instigatorNode.isMale()) {
                this.parents[0] = instigatorNode;
            } else {
                this.parents[1] = instigatorNode;
            }

            this.children = this.getChildren(this.parents[0], this.parents[1]);

            //find the second parents
            if (this.children != null) {

                for(Node child : this.children) {

                    if(instigatorNode.isMale()) {

                        if(this.parents[0] != child.getFather()) {
                            throw new MergeTreeException("Error: Child dosen't match with the parent [father] instigator");    
                        }

                        if(this.parents[1] != null && this.parents[1] != child.getMother()) {
                            throw new MergeTreeException("Error: Child dosen't match with the parent [mother] instigator");                                
                        }

                        this.parents[1] = child.getMother();

                    } else {

                        if(this.parents[1] != child.getMother()) {
                            throw new MergeTreeException("Error: Child dosen't match with the parent [mother] instigator");                                
                        }
                        
                        if(this.parents[0] != null && this.parents[0] != child.getFather()) {
                            throw new MergeTreeException("Error: Child dosen't match with the parent [father] instigator");    
                        }

                        this.parents[0] = child.getFather();                            

                    }

                }

            }
        }

        if(!this.instigatorNodeIsParent) {

            this.parents[0] = this.instigatorNode.getFather();
            this.parents[1] = this.instigatorNode.getMother();

            this.children = this.getChildren(this.parents[0], this.parents[1]);

            if(this.children == null) {
                this.children = new ArrayList<>(Arrays.asList(this.instigatorNode));
            }

        }

        if (this.parents[0] != null && !this.parents[0].isMale()) {
            throw new MergeTreeException("Error : parents[0] " + parents[0] + " must be a male");
        }

        if (this.parents[1] != null && this.parents[1].isMale()) {
            throw new MergeTreeException("Error : parents[1] " + parents[1] + " must be a female");
        }

        for (Node parent : parents) {
            if (children.contains(parent)) {
                throw new MergeTreeException("Error : There are parents that are children or children that are parents");
            }
        }

        for (Node child : children) {
            for (Node parent : parents) {
                if (child.equals(parent)) {
                    throw new MergeTreeException("Error : There are parents that are children or children that are parents");
                }
            }
        }
        
    }

    public List<FamilyTree> travelParents() throws MergeTreeException {

        List<FamilyTree> familyTree = new ArrayList<>();

        for(int i = 0 ; i < this.parents.length ; i++ ) {

            if((instigatorNodeIsParent && parents[i] == instigatorNode) || parents[i] == null) {
                continue; 
            }

            familyTree.add(new FamilyTree(parents[i], false));

        }
        return familyTree;

    }

    public List<FamilyTree> travelChildren() throws MergeTreeException {

        List<FamilyTree> familyTree = new ArrayList<>();

        if (this.children != null) {

            for(Node child : this.children) {

                if((!instigatorNodeIsParent && child == instigatorNode) || child == null) {
                    continue;
                }

                FamilyTree fm = new FamilyTree(child, true);
                familyTree.add(fm);
            }

        }
        return familyTree;
    }

    public FamilyTree travelChildren(Node child) throws MergeTreeException {

        if(!this.getChildren().contains(child)){
            throw new MergeTreeException("Error: Child is not not in family");
        }

        if (this.children != null) {

            for(Node ch : this.children) {

                if((!instigatorNodeIsParent && ch == instigatorNode) || ch == null || ch != child) {
                    continue;
                }

                return new FamilyTree(child, true);
            }

        }
        return null;
    }

    private List<Node> getChildren(Node father, Node mother) throws MergeTreeException {

        List<Node> children = new ArrayList<>();

        if (father == null && mother == null){
            return null;
        }

        if (father == null){
            return this.getChildren(mother);
        }

        if (mother == null){
            return this.getChildren(father);
        }

        if (father.getTree() != mother.getTree() || father.getTree() == null || mother.getTree() == null) {
            throw new MergeTreeException("Error: mother and father dosen't have the same tree");
        }

        Tree tree = father.getTree(); // or mother.getTree();
        for(Node node : tree.getNodes()){

            if(node.getFather() == father && node.getMother() == mother) {
                children.add(node);
            }

            if (node.getFather() == father && node.getMother() != mother){
                children.add(node);
            }

            if (node.getFather() != father&& node.getMother() == mother){
                children.add(node);
            }
        }
        
        return children;

    }

    private List<Node> getChildren(Node parent) throws MergeTreeException {

        List<Node> children = new ArrayList<>();
        
        if ((parent == null)){
            throw new MergeTreeException("Error: parents cannot be null");
        }
        
        if(parent.isMale()) {
            for (Node node : this.tree.getNodes()){   
                if(node.getFather() != null && node.getFather().getId() == parent.getId()) {
                    if (node.getFather() != parent) {
                        throw new MergeTreeException("Error: Same id but not the same reference");
                    }

                    children.add(node);
                }
            }
        } else {
            for (Node node : parent.getTree().getNodes()){    
                if(node.getMother() != null && node.getMother().getId() == parent.getId()) {
                    if (node.getMother() != parent) {
                        throw new MergeTreeException("Error: Same id but not the same reference");
                    }
                    children.add(node);
                }
            }
        }
        return children;

    }

    @Override
    public String toString(){

        String name1 = this.parents[0] != null ? this.parents[0].getFirstName() : "null";
        String name2 = this.parents[1] != null ? this.parents[1].getFirstName() : "null";
        String parents = "[" + name1 + ", " + name2 + "]"; 

        String children = null;
        if (this.children != null) {
            for(Node child : this.children) {
                children = children + ", "  + child.getFirstName();
            }
        }
        children = "[" + children + "]";

        return "(parents : " + parents + "; children : " + children + ")";
    }
        

}