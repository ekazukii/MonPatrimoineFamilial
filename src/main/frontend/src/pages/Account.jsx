import React, {useEffect, useState} from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {useSession} from "../hooks/useSession.jsx";
import UserEdit from "../components/UserEdit.jsx";

//{email, username, firstName, familyName, isMale}
const Account = () => {
    const { user, isLoggedIn } = useSession();

    if(!isLoggedIn) return <></>

    //TODO: Edit profile picture
    return (
        <Container>
            <Row className="justify-content-md-center">
                <h1 className="text-center">Account</h1>
                    <Col xs lg="6">
                        <UserEdit user={user}/>
                    </Col>
            </Row>
        </Container>
    );
};

export default Account;
