import React, {useState} from 'react';
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
            nom: "",
            mail: "",
            numeroSecu: "",
            password: "",
            password2: "",
            prenom: "",
            dateNaissance: "",
            nationalite: ""
        },
        documentImport: {
            carteIdentite: "",
            photo: ""
        }
    });

    const afficherEtapeSuivante = () => {
        setEtp(printEtp + 1);
        setActiveKey(`link-${printEtp + 1}`)
        setPrintEtp(printEtp + 1);
    }
    const handlePersonalInfoChange = (personalInfoData) => {
        setFormData({ ...formData, personalInfo: personalInfoData });
        afficherEtapeSuivante();
    };

    const handleDocumentImportChange = (documentImportData) => {
        setFormData({ ...formData, documentImport: documentImportData });
        afficherEtapeSuivante();
    };

    const handleValidation = () => {
        console.log(formData);
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
                        {printEtp === 0 && (
                            <RegisterComponent.personalInfo onPersonalInfoChange={handlePersonalInfoChange} personalInfo={formData.personalInfo}/>
                        )}
                        {printEtp === 1 && (
                            <RegisterComponent.documentImport onDocumentImport={handleDocumentImportChange} documentImport={formData.documentImport}/>
                        )}
                        {printEtp === 2 && (
                            <RegisterComponent.validationInfo formData={formData}/>
                        )}
                    </Form>
                {printEtp === 2 && (
                    <Button onClick={handleValidation}>Valider</Button>
                )}
            </Row>
        </Container>
    );
};

export default Register;
