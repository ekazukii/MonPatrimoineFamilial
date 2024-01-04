import React, {useEffect, useState} from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import Table from "react-bootstrap/Table";
import {useSession} from "../hooks/useSession.jsx";

const UserSearch = () => {
    const {isLoggedIn, user} = useSession();

    const [query, setQuery] = useState('');
    const [gender, setGender] = useState(null);
    const [birthdate, setBirthdate] = useState(null);
    const [showModal, setShowModal] = useState(false);
    
    const [users, setUsers] = useState([]);

    const [modalMerge, setModalMerge] = useState(false);
    const [nodeList, setNodeList] = useState([]);
    const [user2, setUser2] = useState(null);

    const [selectedEnfants, setSelectedEnfants] = useState([]);
    const [selectedParents, setSelectedParents] = useState([]);

    const [mergeStatus, setMergeStatus] = useState(null);

    const [isMerging, setIsMerging] = useState(false);

    const handleSelectEnfant = (e) => {
        const selectedNode = nodeList.find(node => node.name === e.target.value);
        if (!selectedEnfants.includes(selectedNode)) {
            setSelectedEnfants([...selectedEnfants, selectedNode]);
        }
    };

    const handleRemoveEnfant = (nodeToRemove) => {
        setSelectedEnfants(selectedEnfants.filter(node => node !== nodeToRemove));
    };

    const handleSelectParent = (e) => {
        const selectedNode = nodeList.find(node => node.name === e.target.value);
        if (!selectedParents.includes(selectedNode)) {
            setSelectedParents([...selectedParents, selectedNode]);
        }
    };

    const handleRemoveParent = (nodeToRemove) => {
        setSelectedParents(selectedParents.filter(node => node !== nodeToRemove));
    };

    const handleSearch = async () => {
        // Construct query parameters
        const queryParams = new URLSearchParams({
            query,
            gender,
            birthdate
        });

        try {
            // Make the GET request using fetch
            const response = await fetch(`http://localhost:8080/user/search?${queryParams}`, {
                method: 'GET',
                headers: {
                    // Add any required headers here
                    'Content-Type': 'application/json',
                    // 'Authorization': 'Bearer your-token-here', // If you need authorization
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Process the response
            const users = await response.json();
            setUsers(users);

        } catch (error) {
            console.error('Failed to fetch users:', error);
            // Here you can handle errors, such as updating the UI to show an error message.
        }
    };

    const clearChanges = () => {
        setGender(null);
        setBirthdate(null);
        setShowModal(false);
    }

    const handleSearchChange = (e) => {
        setQuery(e.target.value);
    }      

    const mergeWith = async() => {

        const childrenNodeIds = selectedEnfants.map(node => node.id);

        let parentNodeIds = [null, null];
        let maleCount = 0;
        let femaleCount = 0;
    
        if (selectedParents && selectedParents.length > 2) {
            setMergeStatus("Erreur: plus de deux parents sélectionnés");
            return; 
        }
    
        for (const node of selectedParents) {
            if (node) {
                if (node.gender === "male") {
                    maleCount++;
                    if (maleCount >= 2) {
                        setMergeStatus("Erreur: plus de deux hommes");
                        return; 
                    }
                    parentNodeIds[0] = parentNodeIds[0] ? parentNodeIds[0] : node.id;
                } else if (node.gender === "female") {
                    femaleCount++;
                    if (femaleCount >= 2) {
                        setMergeStatus("Erreur: plus de deux femmes");
                        return; 
                    }
                    parentNodeIds[1] = parentNodeIds[1] ? parentNodeIds[1] : node.id;
                }
            }
        }
 
        try {
            setIsMerging(true); 
            const response = await fetch('http://localhost:8080/tree/merge', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    requestingTreeId: user.tree,
                    respondingTreeId: user2.tree,
                    parentsNodesRequester: parentNodeIds,
                    childrenNodesRequester: childrenNodeIds,
                    idRequester: user.id,
                    idResponder: user2.id
                })
            });
    
            if (response.ok) {
                const responseBody = await response.text();
                setMergeStatus("Successfully merged");
            } else {
                const errorBody = await response.text();
                setMergeStatus("Erreur: " + errorBody); // Utilisez directement errorBody
            }
            
            setIsMerging(false); 
        } catch (error) {
            setMergeStatus("Erreur de connexion au serveur");
            setIsMerging(false); 
        }
    }

    const fetchData = async (treeId) => {

        const data = await fetch("http://localhost:8080/tree?detail=true&id="+treeId)
        // const data2 = await fetch("http://localhost:8080/tree?detail=falseid="+treeId)
        const json = await data.json();

        const nodes = json.nodes.map(data => {
            return {
                id: data.id,
                nodeId: data.id,
                birthday: data.birthDate,
                name: data.firstName + ' ' + data.lastName,
                mid: data.mother,
                fid: data.father,
                gender: data.male ? "male" : "female",
                registered: data.userAccount != null,
                pids: []
            }
        });
        setNodeList(nodes);
    }

    const handleOnMerge = (user2) => {
        //user2 -> user that we merge with but not the logged user
        setModalMerge(true);
        setUser2(user2);
        fetchData(user.tree);
    }

    useEffect(() => {
        handleSearch();
    }, [query, gender, birthdate])


    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col md={8}>
                    <Form>
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Search users"
                                value={query}
                                onChange={handleSearchChange}
                            />
                            <Button variant={(gender !== null || birthdate !== null) ? "outline-primary" : "outline-secondary"} onClick={() => setShowModal(true)}>
                                Advanced Settings
                            </Button>
                        </InputGroup>
                    </Form>

                    <Table striped bordered hover>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Username</th>
                            <th>See the tree</th>
                            {isLoggedIn && <th>Merge</th>}
                        </tr>
                        </thead>
                        <tbody>
                        {users.map((user) => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.firstname}</td>
                                <td>{user.lastname}</td>
                                <td>{user.email}</td>
                                <td>{user.username}</td>
                                <td><a href={`/external?id=${user.id}`}>Go to tree</a></td>
                                {isLoggedIn && (<td><Button onClick={() => handleOnMerge(user)}>Merge tree</Button></td>)}
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                </Col>
            </Row>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Advanced Settings</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group>
                        <Form.Label>Sexe</Form.Label>
                        <Form.Select onChange={e => setGender(e.target.value)}>
                            <option value="true">Male</option>
                            <option value="false">Female</option>
                        </Form.Select>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Date de naissance</Form.Label>
                        <Form.Control
                            type="date"
                            value={birthdate}
                            onChange={(e) => setBirthdate(e.target.value)}
                        />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => clearChanges()}>
                        Clear filters
                    </Button>
                    <Button variant="primary" onClick={() => setShowModal(false)}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal 
            show={modalMerge} 
            onHide={() => setModalMerge(false)}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    <h1>Choisir la position des enfants et/ou des parents pour le merge</h1>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p ClassName="display-6">
                    Pour effectuer la fusion il est nécessaire de connaître le 
                    point commun entre les deux arbres.
                </p>
                <p ClassName="display-6">
                    Vous devez choisir parmis les noeuds de votre arbre, 
                    {user2 && ` des nœuds qui seront les parents de ${user2.lastname}`} 
                    {user2 && ` ou des nœuds qui seront des enfants de ${user2.lastname}`}.
                </p>
                <Form.Group controlId="noeudsEnfantsSelect">
                    <Form.Label>Noeuds enfants</Form.Label>
                    <Form.Select aria-label="Noeuds enfants select" onChange={handleSelectEnfant}>
                        <option>Choisir un enfant</option>
                        {nodeList.map((node, index) => (
                            <option key={index}>{node.name}</option>
                        ))}
                    </Form.Select>
                    <div>
                        {selectedEnfants.map((node, index) => (
                            <span key={index} className="badge bg-secondary m-1">
                                {node.name} <Button size="sm" onClick={() => handleRemoveEnfant(node)}>x</Button>
                            </span>
                        ))}
                    </div>
                </Form.Group>


                <Form.Group controlId="noeudsParentsSelect">
                    <Form.Label>Noeuds Parents</Form.Label>
                    <Form.Select aria-label="Noeuds parents select" onChange={handleSelectParent}>
                        <option>Choisir un parent</option>
                        {nodeList.map((node, index) => (
                            <option key={index}>{node.name}</option>
                        ))}
                    </Form.Select>
                    <div>
                        {selectedParents.map((node, index) => (
                            <span key={index} className="badge bg-secondary m-1">
                                {node.name} <Button size="sm" onClick={() => handleRemoveParent(node)}>x</Button>
                            </span>
                        ))}
                    </div>
                </Form.Group>

                {/* Composant similaire pour les parents */}

                {/* {mergeStatus && <p>{mergeStatus}</p>} */}
                {mergeStatus && (
                    <div className={`${mergeStatus.startsWith("Erreur") ? "alert alert-danger" : "alert alert-success"} mt-4`}>
                        {mergeStatus}
                    </div>
                )}



            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => setModalMerge(false)}>Fermer</Button>
                <Button variant="primary" onClick={() => mergeWith()} disabled={isMerging}>Valider</Button>
            </Modal.Footer>
        </Modal>
            
        </Container>
    );
};

export default UserSearch;
