package fr.cytech.mpf.controller;

import fr.cytech.mpf.dto.*;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.service.CustomDTOMapper;
import fr.cytech.mpf.service.NodeService;
import fr.cytech.mpf.utils.NodeVisibility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class TreeController {
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    TreeRepository treeRepository;
    @Autowired
    NodeService nodeService;
    @Autowired
    CustomDTOMapper customDTOMapper;

    ModelMapper modelMapper;

    TreeController() {
        modelMapper = new ModelMapper();
    }

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

    @PostMapping("/tree/byFilter")
    public ResponseEntity<Map<String, String>> getFilterTree(@RequestBody NodeFilterRequestDTO filterRequest){
        try{
            long treeId = filterRequest.getTreeId();
            String filterIs = filterRequest.getFilterIs();
            Map <String, String> filterInfo = filterRequest.getFilterInfo();
            // Appelez la méthode getTree avec les paramètres id et detail
            ResponseEntity<Tree> response = getTree(treeId, true); // Vous pouvez spécifier la valeur de 'detail' selon vos besoins

            // Si la réponse est OK, renvoyez la même réponse
            if (response.getStatusCode() == HttpStatus.OK) {
                return filterInfo;
            } else {
                // Si la réponse n'est pas OK, renvoyez la même réponse avec le code d'erreur approprié
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (NumberFormatException e) {
            // Gérez l'erreur si treeId n'est pas un nombre valide
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/tree")
    public ResponseEntity<Tree> createNewTree (@RequestBody TreeAddDTO treeDto) {
        Tree tree = modelMapper.map(treeDto, Tree.class);
        treeRepository.save(tree);
        //TODO: Add current user as default node
        return ResponseEntity.ok(tree);
    }

    @PutMapping("/tree")
    public ResponseEntity<Tree> editTree(@RequestBody TreeEditDTO treeEditDTO) {
        Optional<Tree> actualTree = treeRepository.findById(treeEditDTO.getTreeId());
        if(actualTree.isEmpty()) return ResponseEntity.notFound().build();
        actualTree.get().setName(treeEditDTO.getName());
        treeRepository.save(actualTree.get());
        return ResponseEntity.ok(actualTree.get());
    }

    @PostMapping("/tree/node")
    public ResponseEntity<Node> addNewNodeToTree (@RequestBody NodeAddDTO nodeDto) {
        Node node = customDTOMapper.nodeAddDtoToNode(nodeDto);
        nodeRepository.save(node);
        // TODO: Check has permissions to edit
        return ResponseEntity.ok(node);
    }

    @DeleteMapping("/tree/node")
    public ResponseEntity<String> removeNode (@RequestBody NodeDeleteDTO nodeDto) {
        List<Node> nodesPA = nodeRepository.findAllByParentAId(nodeDto.nodeId);
        List<Node> nodesPB = nodeRepository.findAllByParentBId(nodeDto.nodeId);
        nodesPA.forEach(n -> {
            n.setParentA(null);
        });
        nodesPB.forEach(n -> {
            n.setParentB(null);
        });
        nodeRepository.deleteById(nodeDto.nodeId);
        // TODO: Check has permissions to edit
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/tree/node")
    public ResponseEntity<Node> editNode(@RequestBody NodeEditDTO nodeDto) {
        Node node = customDTOMapper.nodeAddDtoToNode(nodeDto);
        node.setId(nodeDto.getId());
        // TODO: Check has permissions to edit
        nodeRepository.save(node);
        return ResponseEntity.ok(node);
    }

    @PostMapping("/createTestTree")
    public ResponseEntity<String> createTestTree() {
        Tree tree = new Tree();
        tree.setName("Famille du Z");
        treeRepository.save(tree);
        return ResponseEntity.ok("ok");
    }

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


    @GetMapping("/getTestTree")
    public ResponseEntity<Tree> getTestTree() {
        Tree tree = treeRepository.findAll().get(0);
        System.out.println(tree);
        return ResponseEntity.ok(tree);
    }
}
