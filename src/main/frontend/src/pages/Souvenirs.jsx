import React from 'react';
import Container from "react-bootstrap/Container";
import styles from './souvenirs.module.css';
import {Image} from "react-bootstrap";
import Form from 'react-bootstrap/Form';
import InputGroupText from "react-bootstrap/InputGroupText"; // Importez les styles CSS gérés par les modules CSS
import SouvenirsComp from '../components/SouvenirsComponent.jsx'

const Souvenirs = () => {
    return (
        <Container>
            <SouvenirsComp />
        </Container>
    );
};

export default Souvenirs;
