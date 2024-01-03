package fr.cytech.mpf.service;

import fr.cytech.mpf.dto.TreeViewAddDTO;
import fr.cytech.mpf.dto.NodeAddDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.TreeView;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomDTOMapper {
    ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private UserRepository userRepository;

    CustomDTOMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    public Node nodeAddDtoToNode(NodeAddDTO nodeDTO) {
        Node node = modelMapper.map(nodeDTO, Node.class);
        node.setTree(treeRepository.findById(nodeDTO.getTreeId()).get());
        if(nodeDTO.getFatherId() != null) node.setFather(nodeRepository.findById(nodeDTO.getFatherId()).get());
        if(nodeDTO.getMotherId() != null) node.setMother(nodeRepository.findById(nodeDTO.getMotherId()).get());
        node.setId(nodeDTO.getId());
        return node;
    }

    public Node nodeAddDtoToNode(Node node, NodeAddDTO nodeDTO) {
        node.setVisibility(nodeDTO.getNodeVisibility());
        node.setFirstName(nodeDTO.getFirstName());
        node.setLastName(nodeDTO.getLastName());
        node.setBirthDate(nodeDTO.getBirthDate());
        node.setMale(nodeDTO.isMale());
        node.setTree(treeRepository.findById(nodeDTO.getTreeId()).get());
        if(nodeDTO.getFatherId() != null) node.setFather(nodeRepository.findById(nodeDTO.getFatherId()).get());
        if(nodeDTO.getMotherId() != null) node.setMother(nodeRepository.findById(nodeDTO.getMotherId()).get());
        node.setId(nodeDTO.getId());
        return node;
    }

    public TreeView addViewToTreeView(TreeViewAddDTO viewAddDTO) {
        TreeView treeView = new TreeView();
        treeView.setViewer(userRepository.findById(viewAddDTO.getViewerId()).get());
        treeView.setTree(treeRepository.findById(viewAddDTO.getTreeId()).get());
        return treeView;
    }
}
