package fr.cytech.mpf.service;

import fr.cytech.mpf.dto.NodeAddDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.UserRepository;
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
import java.util.stream.Collectors;

@Service
public class NodeService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    CustomDTOMapper customDTOMapper;

    MailService mailService;

    public NodeService(MailService mailService) {
        this.mailService = mailService;
    }

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

    /**
     * Add a node to a tree if validation strategy passes
     * @param nodeDto NodeDTO Object
     * @return the added node
     */
    public Node addNode(NodeAddDTO nodeDto) {
        Node node = customDTOMapper.nodeAddDtoToNode(nodeDto);

        ValidationStrategy simpleValidationStrategy = new ValidationStrategy(Arrays.asList(new BirthDateRule()));
        ValidationResponse response = simpleValidationStrategy.validate(node);

        if (!response.isSucess()) {
            throw new ValidationException(response.getErrorMessage());
        }

        return nodeRepository.save(node);

    }


    /**
     * Notify all user that contains the users that changed his tree
     * @param tree tree that has been updated
     */
    public void notifyChange(Tree tree) {
//        List<Node> nodesToNotify = nodeRepository.findByUserAccountIdAndTreeIdNot(tree.getOwner().getId(), tree.getId());
//        List<Tree> treesToNotify = nodesToNotify.stream().map(Node::getTree).toList();
//        List<User> usersToNotify = treesToNotify.stream().map(Tree::getOwner).toList();
          List<Long> userListConnected = findUserAccountIdsByTreeIdAndUserToSearch(tree.getId(), tree.getOwner().getId());
          List<User> usersToNotify = userRepository.findAllById(userListConnected);
          for (User user : usersToNotify) {
              if(user != tree.getOwner()) mailService.sendUpdateMessage(user, tree.getOwner());
        }
    }

    public List<Long> findUserAccountIdsByTreeIdAndUserToSearch(Long treeId, Long userToSearchId) {
        List<Long> userAccountIds = nodeRepository.findDistinctUserAccountIdsByTreeId(treeId);

        List<Long> userPublic = new ArrayList<>(userAccountIds.stream()
                .filter(userId -> {
                    NodeVisibility visibility = nodeRepository
                            .findByUserAccountIdAndTreeId(userId, treeId)
                            .get(0)
                            .getVisibility();

                    return visibility.equals(NodeVisibility.Public) || visibility.equals(NodeVisibility.Protected);
                })
                .toList());
        if(!userPublic.contains(treeRepository.getReferenceById(treeId).getOwner().getId())){
            userPublic.add(treeRepository.getReferenceById(treeId).getOwner().getId());
        }
        // Filtrer la liste pour inclure uniquement les userAccountIds qui ont userToSearchId dans leur liste
        List<Long> filteredUserAccountIds = userPublic.stream()
                .filter(userId -> nodeRepository.findDistinctUserAccountIdsByTreeId(userRepository.findById(userId).get().getTree().getId()).contains(userToSearchId))
                .collect(Collectors.toList());

        return filteredUserAccountIds;
    }

}
