package fr.cytech.mpf.service;

import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.utils.NodeVisibility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NodeService {
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
}
