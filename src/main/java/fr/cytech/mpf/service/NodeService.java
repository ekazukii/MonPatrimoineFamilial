package fr.cytech.mpf.service;

import fr.cytech.mpf.dto.NodeAddDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.service.validation.ValidationException;
import fr.cytech.mpf.service.validation.ValidationResponse;
import fr.cytech.mpf.service.validation.ValidationStrategy;
import fr.cytech.mpf.service.validation.rules.BirthDateRule;
import fr.cytech.mpf.utils.NodeVisibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NodeService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    CustomDTOMapper customDTOMapper;

    /**
     * Given a NodeVisibility object return a List wil all NodeVisibility lower or equals this one
     * Example -> getVisibilityList(protected) -> [protected, public]
     * @param visibility
     * @return
     */
    public List<NodeVisibility> getVisibilityList(NodeVisibility visibility) {
        List<NodeVisibility> visibilities = new ArrayList<>();
        switch (visibility) {
            case Private:
                visibilities.add(NodeVisibility.Private);
            case Protected:
                visibilities.add(NodeVisibility.Protected);
            case Public:
                visibilities.add(NodeVisibility.Public);
        }

        return visibilities;
    }

    public Node addNode(NodeAddDTO nodeDto) {
        Node node = customDTOMapper.nodeAddDtoToNode(nodeDto);

        ValidationStrategy simpleValidationStrategy = new ValidationStrategy(Arrays.asList(new BirthDateRule()));
        ValidationResponse response = simpleValidationStrategy.validate(node);

        if (!response.isSucess()) {
            throw new ValidationException(response.getErrorMessage());
        }

        return nodeRepository.save(node);

    }
}
