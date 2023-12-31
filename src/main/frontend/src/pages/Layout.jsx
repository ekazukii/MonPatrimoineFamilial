import { Outlet, Link } from "react-router-dom";
import Navbar from "react-bootstrap/Navbar";
import classes from "./layout.module.css";
import { Container, Row, Col } from 'react-bootstrap';
import React from 'react';
import Offcanvas from "react-bootstrap/Offcanvas";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Breadcrumb from 'react-bootstrap/Breadcrumb';
import CheminPages from "./CheminPages.jsx";
import {useSession} from "../hooks/useSession.jsx";

const Layout = () => {
    const { user, isLoggedIn, setSession, login, refreshData, logout } = useSession();
    return (
        <div className={classes['wrapper']}>
            <Navbar key={false} expand={false} className={"mb-3 "+classes['custom-bg']}>
                <Container fluid>
                    <Navbar.Brand href="/">MonPapiFinito</Navbar.Brand>
                    <Navbar.Toggle aria-controls={`offcanvasNavbar-expand-${false}`} />
                    <Navbar.Offcanvas
                        id={`offcanvasNavbar-expand-${false}`}
                        aria-labelledby={`offcanvasNavbarLabel-expand-${false}`}
                        placement="end"
                    >
                        <Offcanvas.Header closeButton>
                            <Offcanvas.Title id={`offcanvasNavbarLabel-expand-${false}`}>
                                MonPapiFinito
                            </Offcanvas.Title>
                        </Offcanvas.Header>
                        <Offcanvas.Body>
                            <Nav className="justify-content-end flex-grow-1 pe-3">
                                <Nav.Link href="/">Accueil</Nav.Link>
                                {isLoggedIn ? (
                                    <>
                                    <Nav.Link href="/account">Mon compte</Nav.Link>
                                    <Nav.Link href="/tree">Mon Arbre</Nav.Link>
                                    <Nav.Link href="/souvenirs">Mes Souvenirs</Nav.Link>
                                    <Nav.Link href="/search">Recherche d'utilisateur</Nav.Link>
                                    <Nav.Link onClick={() => logout()}>Deconnexion</Nav.Link>
                                    </>
                                ) : (
                                    <>
                                        <Nav.Link href="/search">Recherche d'utilisateur</Nav.Link>
                                        <Nav.Link href="/login">Se connecter</Nav.Link>
                                        <Nav.Link href="/register">S'inscrire</Nav.Link>
                                    </>
                                )}
                            </Nav>
                        </Offcanvas.Body>
                    </Navbar.Offcanvas>
                </Container>
            </Navbar>
            <CheminPages />
            <div className={classes['contentWrapper']}>
                <Outlet/>
            </div>
            <footer className={classes['custom-bg']}>
                <Container>
                    <Row className="pt-2 pb-2">
                        <Col className="d-flex justify-content-center align-items-center">
                            <span>&copy; {new Date().getFullYear()} monPapiFinito</span>
                        </Col>
                        <Col className="d-flex justify-content-center align-items-center">
                            <span>Retrouvez-nous sur GitHub :</span>
                            <a href="https://github.com/votre-utilisateur" target="_blank" rel="noopener noreferrer" className="ms-1">
                                <i className="fa fa-github text-black fa-2x"/>
                            </a>
                        </Col>
                    </Row>
                </Container>
            </footer>
        </div>
    )
};

export default Layout;