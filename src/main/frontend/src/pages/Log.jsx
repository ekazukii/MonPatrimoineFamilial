import React, {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from "react-bootstrap/Container";
import {useSession} from "../hooks/useSession.jsx";
import {useNavigate} from "react-router-dom";

const Log = () => {
    const [inputs, setInputs] = useState({});
    const { user, isLoggedIn, setSession, login, refreshData, logout } = useSession();
    const navigate = useNavigate();

    const handleChange = e => setInputs(prevState => ({ ...prevState, [e.target.name]: e.target.value }));
    const sendForm = async (e) => {
        e.preventDefault();
        const success = await login(inputs.username, inputs.password);
        if(!success) return alert("Connexion failed");
        navigate("/home");
    }

    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col xs lg="6">
                    <h2>Connexion</h2>
                    <Form onSubmit={sendForm}>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Label>Username</Form.Label>
                            <Form.Control type="username" placeholder="Enter username" name="username" onChange={handleChange} value={inputs.username}/>
                            <Form.Text className="text-muted">
                                We'll never share your email with anyone else.
                            </Form.Text>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" name="password" onChange={handleChange} value={inputs.password}/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicCheckbox">
                            <Form.Check type="checkbox" label="Check me out" />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Submit
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
};

export default Log;
