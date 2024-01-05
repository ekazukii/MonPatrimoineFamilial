package fr.cytech.mpf.controller;

import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.*;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.TreeView;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.TreeViewRepository;
import fr.cytech.mpf.service.CustomDTOMapper;
import fr.cytech.mpf.service.MergeTreeTomService;
import fr.cytech.mpf.service.NodeService;
import fr.cytech.mpf.service.validation.ValidationException;
import fr.cytech.mpf.service.mergeTree.MergeTreeService;
import fr.cytech.mpf.service.mergeTree.MergeTreeException;
import fr.cytech.mpf.utils.NodeVisibility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TreeController {
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    TreeRepository treeRepository;
    @Autowired
    TreeViewRepository treeViewRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    MergeTreeService mergeTreeService;

    @Autowired
    CustomDTOMapper customDTOMapper;

    @Autowired
    private MergeTreeTomService mergeTreeTomService;

    ModelMapper modelMapper;

    TreeController() {
        modelMapper = new ModelMapper();
    }

    @MustBeLogged
    @GetMapping("/tree")
    public ResponseEntity<Tree> getTree(@RequestParam Long id,  @RequestParam Boolean detail) {
        Optional<Tree> treeOpt = treeRepository.findById(id);
        if(treeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tree tree = treeOpt.get();
        List<NodeVisibility> visibilities = nodeService.getVisibilityList(NodeVisibility.Private);
        List<Node> nodes = nodeRepository.findAllByTreeEqualsAndVisibilityIn(tree, visibilities);
        tree.setNodes(nodes);
    
        return ResponseEntity.ok(tree);
    }

    @MustBeLogged
    @PostMapping("/tree")
    public ResponseEntity<Tree> createNewTree (@RequestBody TreeAddDTO treeDto) {
        Tree tree = modelMapper.map(treeDto, Tree.class);
        treeRepository.save(tree);
        //TODO: Add current user as default node
        return ResponseEntity.ok(tree);
    }

    @MustBeLogged
    @PutMapping("/tree")
    public ResponseEntity<Tree> editTree(@RequestBody TreeEditDTO treeEditDTO) {
        Optional<Tree> actualTree = treeRepository.findById(treeEditDTO.getTreeId());
        if(actualTree.isEmpty()) return ResponseEntity.notFound().build();
        actualTree.get().setName(treeEditDTO.getName());
        treeRepository.save(actualTree.get());
        return ResponseEntity.ok(actualTree.get());
    }

    @MustBeLogged
    @PostMapping("/tree/node")
    public ResponseEntity<?> addNewNodeToTree (@RequestBody NodeAddDTO nodeDto) {
        try {
            // TODO: Check has permissions to edit
            Node node = nodeService.addNode(nodeDto);
            nodeService.notifyChange(node.getTree());
            return ResponseEntity.ok(node);
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @MustBeLogged
    @DeleteMapping("/tree/node")
    public ResponseEntity<String> removeNode (@RequestBody NodeDeleteDTO nodeDto) {
        List<Node> nodesPA = nodeRepository.findAllByFatherId(nodeDto.nodeId);
        List<Node> nodesPB = nodeRepository.findAllByMotherId(nodeDto.nodeId);
        nodesPA.forEach(n -> {
            n.setFather(null);
        });
        nodesPB.forEach(n -> {
            n.setMother(null);
        });
        nodeRepository.deleteById(nodeDto.nodeId);
        // TODO: Check has permissions to edit
        return ResponseEntity.ok("ok");
    }

    @MustBeLogged
    @PutMapping("/tree/node")
    public ResponseEntity<Node> editNode(@RequestBody NodeEditDTO nodeDto) {
        Node node = nodeRepository.findById(nodeDto.getId()).orElse(null);
        if(node == null) return ResponseEntity.notFound().build();
        customDTOMapper.nodeAddDtoToNode(node, nodeDto);
        // TODO: Check has permissions to edit
        nodeService.notifyChange(node.getTree());
        nodeRepository.save(node);
        return ResponseEntity.ok(node);
    }

    @MustBeLogged
    @PostMapping("/tree/merge")
    public ResponseEntity<?> mergeTree (@RequestBody  MergeTreeDTO mergeTreeDto) {
        try {
            List<Tree> mergedTrees = mergeTreeService.mergeTrees(mergeTreeDto);
            return ResponseEntity.ok("ok");
        } catch (MergeTreeException ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(
                "Cause: " + ex.getCause() + "\n" +
                "Class: " + ex.getClass().getName() + ":" + ex.getStackTrace()[0].getLineNumber() + "\n" +
                "Message: " + ex.getMessage() + "\n" );
        }
    }

    @MustBeLogged
    @PostMapping("/tree/mergeTom")
    public ResponseEntity<?> mergeTreeTom(@RequestBody MergeTreeDTO mergeTreeDto) {
        try {
            Optional<Tree> treeA = treeRepository.findTreeByIdWithNodes(mergeTreeDto.getRequestingTreeId());
            Optional<Tree> treeB = treeRepository.findTreeByIdWithNodes(mergeTreeDto.getRespondingTreeId());

            if (treeA.isPresent() && treeB.isPresent()) {
                System.out.println("----------------------Avant fusion TreeA-------------------");
                System.out.println(treeA.get().getNodes());
                System.out.println("----------------------Avant fusion TreeB-------------------");
                System.out.println(treeB.get().getNodes());
                Tree mergedTree = mergeTreeTomService.mergeTrees(treeA.get(), treeB.get());
                return ResponseEntity.ok(mergedTree);
            } else {
                return ResponseEntity.badRequest().body("Arbres non trouvés");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la fusion : " + ex.getMessage());
        }
    }


    @MustBeLogged
    @PostMapping("/createTestTree")
    public ResponseEntity<String> createTestTree() {
        Tree tree = new Tree();
        tree.setName("Famille du Z");
        treeRepository.save(tree);
        return ResponseEntity.ok("ok");
    }

    @MustBeLogged
    @PostMapping("/createTestNodes")
    public ResponseEntity<String> createTestNodes() {
        Tree tree = treeRepository.findAll().get(0);
        Node node1 = new Node("Manu", "Macron", "06/06/1970", NodeVisibility.Public, tree);
        Node node2 = new Node("Marine", "Le Crayon", "06/06/1970", NodeVisibility.Public, tree);
        Node leZ = new Node(node1, node2, "Eriku", "Zemuru", "12/12/1990", NodeVisibility.Public, tree);

        nodeRepository.save(node1);
        nodeRepository.save(node2);
        nodeRepository.save(leZ);

        return ResponseEntity.ok("ok");
    }

    @MustBeLogged
    @GetMapping("/getTestTree")
    public ResponseEntity<Tree> getTestTree() {
        Tree tree = treeRepository.findAll().get(0);
        System.out.println(tree);
        return ResponseEntity.ok(tree);
    }

    @MustBeLogged
    @PostMapping("/tree/view")
    public ResponseEntity<String> addView(@RequestBody TreeViewAddDTO addViewDTO) {
        TreeView treeView = customDTOMapper.addViewToTreeView(addViewDTO);
        treeViewRepository.save(treeView);
        return ResponseEntity.ok("Ok");
    }

    @MustBeLogged
    @GetMapping("/tree/view")
    public ResponseEntity<List<TreeView>> getViews(@RequestParam Long treeId) {
        List<TreeView> treeViews = treeViewRepository.getAllByTreeId(treeId);
        return ResponseEntity.ok(treeViews);
    }
}
