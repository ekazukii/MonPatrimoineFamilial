import React, {useEffect, useState} from "react";
import {addPartnersId} from "../utils/tree.js";
import {useLocation} from "react-router-dom";
import FamilyTree from "../components/MyTree.jsx";

function useQuery() {
    const { search } = useLocation();

    return React.useMemo(() => new URLSearchParams(search), [search]);
}

const TreePage = () => {
    const query = useQuery();
    const id = query.get("id");
    const [nodes, setNodes] = useState();

    const refreshData = async () => {
        const data = await fetch(`http://localhost:8080/tree?detail=true&id=${id}`)
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
        setNodes(nodes);
    };

    const postView = async () => {
        const data = await fetch("http://localhost:8080/tree/view", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                viewerId: 1,
                treeId: id
            })
        })
    }

    useEffect(() => {
        if(!id) return;
        refreshData();
        postView();
    }, [id])

    return <>
        <FamilyTree nodes={nodes} readOnly />
    </>
}

export default TreePage;