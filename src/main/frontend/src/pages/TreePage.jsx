import React, {useEffect, useState} from "react";
import {addPartnersId} from "../utils/tree.js";
import {useParams} from "react-router-dom";
import FamilyTree from "../components/MyTree.jsx";

const TreePage = ({match}) => {
    console.log(match);
    const { id } = useParams();
    const [nodes, setNodes] = useState();

    const refreshData = async () => {
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
        setNodes(nodes);
    };

    useEffect(() => {
        refreshData();
    }, [id])

    return <>
        <FamilyTree nodes={nodes} readOnly />
    </>
}

export default TreePage;