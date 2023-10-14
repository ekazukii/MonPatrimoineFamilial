import React from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from "react-bootstrap/Container";
import Nav from 'react-bootstrap/Nav';

const Register = () => {
    return (
        <Container className={"col-md-8 mx-auto bg-light"}>
            <Row className="justify-content-md-center">
                <Col>
                    <h2>Inscription</h2>
                    <Nav justify variant="tabs" defaultActiveKey="link-0">
                        <Nav.Item>
                            <Nav.Link eventKey="link-0">1. Informations personnelles</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link eventKey="link-1" disabled={true}>2. Pièces justificatives</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link eventKey="link-2" disabled={true}>3. Validation</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Col>
            </Row>
            <Row className="justify-content-md-center">
                    <Form className="d-flex flex-row justify-content-around">
                        <Col className="col-5">
                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Nom</Form.Label>
                                <Form.Control type="text" placeholder="Votre nom" />
                                <Form.Text className="text-muted">
                                    We'll never share your email with anyone else.
                                </Form.Text>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="text" placeholder="Votre prénom" />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Numéro sécurité sociale</Form.Label>
                                <Form.Control type="Date"/>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicCheckbox">
                                <Form.Check type="checkbox" label="Check me out" />
                            </Form.Group>
                        </Col>
                        <Col className="col-5">
                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Prénom</Form.Label>
                                <Form.Control type="text" placeholder="Votre nom" />
                                <Form.Text className="text-muted">
                                    We'll never share your email with anyone else.
                                </Form.Text>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Date de naissance</Form.Label>
                                <Form.Control type="text" placeholder="Votre prénom" />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Nationalité d'origine</Form.Label>
                                <Form.Control type="text" placeholder="Votre prénom" />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicCheckbox">
                                <Form.Check type="checkbox" label="Check me out" />
                            </Form.Group>
                            <Button variant="primary" type="submit">
                                Submit
                            </Button>
                        </Col>
                    </Form>
            </Row>
        </Container>
    );
};

export default Register;
