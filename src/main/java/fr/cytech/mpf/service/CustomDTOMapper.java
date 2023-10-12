package fr.cytech.mpf.service;

import fr.cytech.mpf.dto.NodeAddDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
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

    CustomDTOMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    public Node nodeAddDtoToNode(NodeAddDTO nodeDTO) {
        Node node = modelMapper.map(nodeDTO, Node.class);
        node.setTree(treeRepository.findById(nodeDTO.getTreeId()).get());
        if(nodeDTO.getFatherId() != null) node.setFather(nodeRepository.findById(nodeDTO.getFatherId()).get());
        if(nodeDTO.getMotherId() != null) node.setMother(nodeRepository.findById(nodeDTO.getMotherId()).get());
        node.setId(null);
        return node;
    }
}
