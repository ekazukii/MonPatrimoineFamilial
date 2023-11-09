import React, {useRef, useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from "react-bootstrap/Container";
import Nav from 'react-bootstrap/Nav';
import * as RegisterComponent from '../components/RegisterComponent.jsx'

const Register   = () => {
    const [printEtp, setPrintEtp] = useState(0);
    const [etp, setEtp] = useState(0);
    const [activeKey, setActiveKey] = useState("link-0");
    const [formData, setFormData] = useState({
        personalInfo: {
            lastName: "",
            email: "",
            socialSecurityNumber: "",
            password: "",
            password2: "",
            firstName: "",
            birthDate: "",
            nationality: ""
        }
    });
    const idCardRef = useRef(null);
    const photoRef = useRef(null);

    const afficherEtapeSuivante = () => {
        setEtp(printEtp + 1);
        setActiveKey(`link-${printEtp + 1}`)
        setPrintEtp(printEtp + 1);
    }
    const handlePersonalInfoChange = (personalInfoData) => {
        setFormData({ ...formData, personalInfo: personalInfoData });
        afficherEtapeSuivante();
    };

    const handleValidation = async () => {
        const formDataObject = new FormData();

        const persInfo = formData.personalInfo;
        persInfo.isMale = true;
        persInfo.username = persInfo.firstName.charAt(0) + persInfo.lastName.split(" ").join("");

        // Append the personalInfo data as JSON
        formDataObject.append('personalInfo', JSON.stringify(persInfo));

        const idCard = idCardRef.current.files[0];
        const photo = photoRef.current.files[0];

        formDataObject.append('carteIdentite', idCard);
        formDataObject.append('photo', photo);

        const response = await fetch("http://localhost:8080/register", {
            method: "POST",
            body: formDataObject,
        });

        console.log(formDataObject);
    };

    return (
        <Container className={"col-md-8 mx-auto bg-light"}>
            <Row className="justify-content-md-center">
                <Col>
                    <h2>Inscription</h2>
                    <Nav justify variant="tabs" activeKey={activeKey}>
                        <Nav.Item>
                            <Nav.Link eventKey="link-0" onClick={() => {setPrintEtp(0); setActiveKey(`link-0`);}}>1. Informations personnelles</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link eventKey="link-1" disabled={etp < 1} onClick={() => {setPrintEtp(1); setActiveKey("link-1")}}>2. Pièces justificatives</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link eventKey="link-2" disabled={true} onClick={() => {setPrintEtp(2); setActiveKey("link-2")}}>3. Validation</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Col>
            </Row>
            <Row className="justify-content-md-center">
                    <Form>
                        <RegisterComponent.personalInfo onPersonalInfoChange={handlePersonalInfoChange} personalInfo={formData.personalInfo} display={printEtp === 0}/>
                        <RegisterComponent.documentImport display={printEtp === 1} handleGoNext={() => afficherEtapeSuivante()} idCardRef={idCardRef} photoRef={photoRef}/>
                        <RegisterComponent.validationInfo formData={formData} display={printEtp === 2} idCardRef={idCardRef} photoRef={photoRef}/>
                    </Form>
                {printEtp === 2 && (
                    <Button onClick={handleValidation}>Valider</Button>
                )}
            </Row>
        </Container>
    );
};

export default Register;
