import React, {useEffect, useState} from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Form from "react-bootstrap/Form";
import Stack from "react-bootstrap/Stack";
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
import {useSession} from "../hooks/useSession.jsx";

//{email, username, firstName, familyName, isMale}
const Account = () => {
    const { user, isLoggedIn } = useSession();
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [familyName, setFamilyName] = useState("");
    const [isMale, setIsMale] = useState("");
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");

    useEffect(() => {
        if(!user) return;
        setEmail(user.email)
        setFamilyName(user.lastname);
        setFirstName(user.firstname);
        setIsMale(user.male);
    }, [user]);

    if(!isLoggedIn) return <></>

    //TODO: Edit profile picture
    return (
        <Container>
            <Row className="justify-content-md-center">
                <h1 className="text-center">Account</h1>
                    <Col xs lg="6">
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicUsername">
                                <Form.Label>Username</Form.Label>
                                <Form.Control type="text" disabled value={user.username}/>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicSecurityNumber">
                                <Form.Label>Social security number</Form.Label>
                                <Form.Control type="number" placeholder="Enter social security number" disabled value={user.socialSecurityNumber} />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicBirthd">
                                <Form.Label>Birth information</Form.Label>
                                <Stack direction="horizontal" gap={3}>
                                    <Form.Control type="date" placeholder="Enter Birth Date" value={user.birthdate} disabled/>
                                    <Form.Select value={isMale ? "m" : "f"} onChange={(e) => setIsMale(e.target.value === "m")}>
                                        <option value="m">Male</option>
                                        <option value="f">Female</option>
                                    </Form.Select>
                                </Stack>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicUsername">
                                <Form.Label>Name</Form.Label>
                                <Stack direction="horizontal" gap={3}>
                                    <Form.Control type="text" placeholder="Enter First Name" value={firstName} onChange={(e) => setFirstName(e.target.value)}/>
                                    <Form.Control type="text" placeholder="Enter Family Name" value={familyName} onChange={(e) => setFamilyName(e.target.value)}/>
                                </Stack>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicEmail">
                                <Form.Label>Email address</Form.Label>
                                <Form.Control type="email" placeholder="Enter email" value={email} onChange={(e) => setEmail(e.target.value)}/>
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Old Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>New Password</Form.Label>
                                <Form.Control type="password" placeholder="Password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}/>
                            </Form.Group>

                            <Stack direction="horizontal mx-auto" gap={3}>
                                <Button variant="primary" type="submit">
                                    Submit
                                </Button>
                            </Stack>
                        </Form>
                    </Col>
            </Row>
        </Container>
    );
};

export default Account;
