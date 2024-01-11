import React, {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from "react-bootstrap/Container";
import {useSession} from "../hooks/useSession.jsx";
import {useNavigate} from "react-router-dom";
import {Image} from "react-bootstrap";
import img1 from "../../../resources/img/home/home-1-nobg.png";

const Log = () => {
    const [inputs, setInputs] = useState({});
    const { user, isLoggedIn, message, setSession, login, refreshData, logout } = useSession();
    const navigate = useNavigate();

    const handleChange = e => setInputs(prevState => ({ ...prevState, [e.target.name]: e.target.value }));
    const sendForm = async (e) => {
        e.preventDefault();
        const success = await login(inputs.username, inputs.password);
        if (success) return window.location.href = "/";
    }

    return (
        <>
            <Row className="justify-content-center">
                <Col lg={10} md={12} xl={8} className="position-relative justify-md-content-end justify-content-center d-flex">
                    <div className={`position-${window.innerWidth >= 768 ?'absolute start-0 top-50 translate-middle-y' : 'relative w-100'} p-4 rounded`} style={{ width:'60%', zIndex: 1, background: 'rgba(255, 255, 255, 0.55)', backdropFilter: 'blur(10px)', boxShadow: '0 0 25px rgba(0, 0, 0, 0.2)'}}>
                        <h2>Connexion</h2>
                        <Form onSubmit={sendForm}>
                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Username</Form.Label>
                                <Form.Control type="username" placeholder="Enter username" name="username" onChange={handleChange} value={inputs.username} />
                                <Form.Text className="text-muted">
                                    We'll never share your email with anyone else.
                                </Form.Text>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" name="password" onChange={handleChange} value={inputs.password} />
                            </Form.Group>
                            {message && (
                                <div className="alert alert-danger mt-4">
                                    {message}
                                </div>
                            )}
                            <Button variant="primary" type="submit">
                                Submit
                            </Button>
                        </Form>
                    </div>
                    <Image src={img1} className="d-none d-md-block"/>
                </Col>
            </Row>
        </>
    );
};

export default Log;
