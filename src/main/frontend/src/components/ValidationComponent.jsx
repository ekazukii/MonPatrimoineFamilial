import React from 'react';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from "react-bootstrap/Container";

const ValidationFront = ({ title, textBody }) => {
    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col xs lg="6" className="d-flex text-center flex-column">
                    <h2>{title}</h2>
                    <i className="fi fi-ss-check-circle fa-5x text-success"></i>
                    <p className="text-center">{textBody}</p>
                </Col>
            </Row>
        </Container>
    );
};

export default ValidationFront;
