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
import { getBackUrl } from '../utils/urls';

const UserSearch = () => {
    const {isLoggedIn, user} = useSession();

    const [query, setQuery] = useState('');
    const [gender, setGender] = useState("null");
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
            query
        });

        if (gender !== "null") {
            queryParams.append('gender', gender);
        }

        // Add birthdate to queryParams only if it's not null
        if (birthdate !== null) {
            queryParams.append('birthdate', birthdate);
        }


        try {
            // Make the GET request using fetch
            const response = await fetch(`${getBackUrl()}/user/search?${queryParams}`, {
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
        setGender("null");
        setBirthdate(null);
        setShowModal(false);
    }

    const handleSearchChange = (e) => {
        setQuery(e.target.value);
    }      

    const mergeWith = async(user2) => {

        try {
            setIsMerging(true); 

            const response = await fetch(getBackUrl() + '/tree/mergeStrategy', {

                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    requestingTreeId: user.tree,
                    respondingTreeId: user2.tree,
                    // parentsNodesRequester: null,
                    // childrenNodesRequester: null,
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

        const data = await fetch(getBackUrl() + "/tree?detail=true&id="+treeId)
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
                                <td><a href={`/external?id=${user.tree}`}>Voir l'arbre</a></td>
                                {isLoggedIn && (<td><Button onClick={() => mergeWith(user) } disabled={isMerging}>Fusionner</Button></td>)}
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
                        <Form.Select value={gender ?? 'null'} onChange={e => setGender(e.target.value)}>
                            <option value="null">All</option>
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
            show={mergeStatus} 
            onHide={() => setModalMerge(false)}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                    <h1>Message</h1>
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {mergeStatus && (
                    <div className={`${mergeStatus.startsWith("Erreur") ? "alert alert-danger" : "alert alert-success"} mt-4`}>
                        {mergeStatus}
                    </div>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={() => setMergeStatus(null)}>Fermer</Button>
            </Modal.Footer>
        </Modal>
            
        </Container>
    );
};

export default UserSearch;
