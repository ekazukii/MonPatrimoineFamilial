import React, {useEffect, useState} from 'react';
import FamilyTree from "./MyTree.jsx";

const dasdqf = [
    { id: 1, pids: [2], name: 'Amber McKenzie', gender: 'female', birthday: '18/06/1972', username: 'Person1' },
    { id: 2, pids: [1], name: 'Ava Field', gender: 'male', birthday: '06/08/1974', username: 'Person2'  },
    { id: 3, mid: 1, fid: 2, name: 'Peter Stevens', gender: 'male', birthday: '12/02/2001', username: 'Person3'  },
    { id: 4, mid: 1, fid: 2, name: 'Savin Stevens', gender: 'male', birthday: '28/11/1999', username: 'Person4'  },
    { id: 5, mid: 1, fid: 2, name: 'Emma Stevens', gender: 'female', birthday: '02/04/2005', username: 'Person5'  }
]

const findParents = (nodes, mid, fid) => {
    let father, mother;
    for(const node of nodes) {
        if(node.id === mid) {
            mother = node;
        } else if(node.id === fid) {
            father = node;
        }
    }

    return [father, mother];
}

const getParents = (tree, mid, fid) => {
    return [tree.get(fid), tree.get(mid)];
}

const addPartnersId = (nodes) => {
    for(const node of nodes) {
        if(node.mid && node.fid) {
            const [father, mother] = findParents(nodes, node.mid, node.fid);
            father.pids.push(mother.id);
            mother.pids.push(father.id);
        }
    }
}

const hasDiff = (nodes, node) => {
    const oldNode = nodes.find(n => n.id === node.id);
    if(!oldNode) return true;

    return oldNode.name !== oldNode || oldNode.birthday !== oldNode;
}

const parentExists = (nodes, node) => {
    const parents = nodes.filter(n => n.id === node.fid || n.id === node.mid);
    if(node.fid || node.mid) {
        if(node.fid && node.mid && parents.length != 2) return false;
        if(parents.length != 1) return false;
    }
    return parents.every(p => p.nodeId != null);
}

const getChildren = (nodes, node) => {
    return nodes.filter(n =>
        (n.mid != null && n.mid === node.id) || (n.fid != null && n.fid === node.id)
    );
}

export default function OrgChartTree() {

    const [nodeList, setNodeList] = useState([]);

    const fetchData = async () => {
        const data = await fetch("http://localhost:8080/tree?detail=true&id=1")
        const json = await data.json();

        const nodes = json.nodes.map(data => {
            return {
                id: data.id,
                nodeId: data.id,
                birthday: data.birthDate,
                name: data.firstName + ' ' + data.lastName,
                mid: data.father,
                fid: data.mother,
                gender: data.male ? "male" : "female",
                pids: []
            }
        });

        addPartnersId(nodes);
        nodes[0].tags = ["registered"]
        setNodeList(nodes);
    }

    const addNodeToTree = async (name, birthday, isMale, fid, mid) => {
        const data = await fetch("http://localhost:8080/tree/node", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: name.split(" ")[0],
                lastName: name.split(" ")[1],
                birthDate: birthday,
                fatherId: fid,
                motherId: mid,
                treeId: 1,
                nodeVisibility: "Public",
                male: isMale
            })
        });
        const json = await data.json();
        return json.id;
    }

    const updateNode = async (nodeId, name, birthday, isMale, fid, mid) => {
        const data = await fetch("http://localhost:8080/tree/node", {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: name.split(" ")[0],
                lastName: name.split(" ")[1],
                birthDate: birthday,
                fatherId: fid,
                motherId: mid,
                treeId: 1,
                nodeVisibility: "Public",
                id: nodeId,
                male: isMale
            })
        });
        return nodeId;
    }

    const updateOneNode = async (node, tree) => {
        if(node.birthday === undefined || node.name === undefined) return;
        const [fNode, mNode] = getParents(tree, node.fid, node.mid);
        const isMale = node.gender === 'male';

        if(node.nodeId === undefined) {
            return addNodeToTree(node.name, node.birthday, isMale, fNode?.nodeId, mNode?.nodeId);
        } else {
            return updateNode(node.nodeId, node.name, node.birthday, isMale, fNode?.nodeId, mNode?.nodeId);
        }
    }

    const updateOneChildren = async (node, tree, newParNodeId, newParId) => {
        if(node.birthday === undefined || node.name === undefined) return;
        const [fNode, mNode] = getParents(tree, node.fid, node.mid);
        if(fNode?.id === newParId) fNode.nodeId = newParNodeId;
        if(mNode?.id === newParId) mNode.nodeId = newParNodeId;
        const isMale = node.gender === 'male';

        return updateNode(node.nodeId, node.name, node.birthday, isMale, fNode?.nodeId, mNode?.nodeId);
    }

    const onUpdate = async (tree, node) => {
        const nodes = Object.values(tree.nodes)
        alert("update")
        if(!parentExists(nodes, node)) {
            alert("parent error");
            return;
        };
        const parentId = await updateOneNode(node, tree);
        const children = getChildren(nodes, node);
        for (const child of children) {
            const childNodeData = tree.get(child.id)
            await updateOneChildren(childNodeData, tree, parentId, node.id);
        }
        await fetchData();
    }

    useEffect(() => {
        fetchData();
    }, [])

    return (
        <div>
            <FamilyTree nodes={nodeList} onUpdate={onUpdate} />
        </div>
    );
}