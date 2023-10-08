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
    let parentA, parentB;
    for(const node of nodes) {
        if(node.id === mid) {
            parentA = node;
        } else if(node.id === fid) {
            parentB = node;
        }
    }

    return [parentA, parentB];
}

const addPartnersId = (nodes) => {
    for(const node of nodes) {
        if(node.mid && node.fid) {
            const [parentA, parentB] = findParents(nodes, node.mid, node.fid);
            parentB.pids.push(parentA.id);
            parentA.pids.push(parentB.id);
        }
    }
}

export default function OrgChartTree() {

    const [nodeList, setNodeList] = useState([]);

    const fetchData = async () => {
        const data = await fetch("http://localhost:8080/tree?detail=true&id=1")
        const json = await data.json();


        const nodes = json.nodes.map(data => {
            return {
                id: data.id,
                birthday: data.birthDate,
                name: data.firstName + ' ' + data.lastName,
                mid: data.parentA,
                fid: data.parentB,
                pids: []
            }
        });

        addPartnersId(nodes);
        console.log(nodes)
        setNodeList(nodes);
    }

    useEffect(() => {
        fetchData();
    }, [])

    return (
        <div>
            <FamilyTree nodes={nodeList} />
        </div>
    );
}