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

    const mergeWith = (id) => {
        // INIT MERGE
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
                                {isLoggedIn && (<td><Button onClick={() => mergeWith(user.id)}>Merge tree</Button></td>)}
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
        </Container>
    );
};

export default UserSearch;
