import { Outlet, Link } from "react-router-dom";
import Navbar from "react-bootstrap/Navbar";
import classes from "./layout.module.css";
import Container from "react-bootstrap/Container";
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
        <>
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
                                    <Nav.Link href="/souvenirs">Mes Souvenirs</Nav.Link>
                                    <Nav.Link onClick={() => logout()}>Deconnexion</Nav.Link>
                                        <NavDropdown
                                        title="Dropdown"
                                        id={`offcanvasNavbarDropdown-expand-${false}`}
                                        >
                                            <NavDropdown.Item href="#action3">Action</NavDropdown.Item>
                                            <NavDropdown.Item href="#action4">
                                                Another action
                                            </NavDropdown.Item>
                                            <NavDropdown.Divider />
                                            <NavDropdown.Item href="#action5">
                                                Something else here
                                            </NavDropdown.Item>
                                        </NavDropdown>
                                    </>
                                ) : (
                                    <>
                                        <Nav.Link href="/login">Se connecter</Nav.Link>
                                        <Nav.Link href="/register">S'inscrire</Nav.Link>
                                    </>
                                )}
                            </Nav>
                            <Form className="d-flex">
                                <Form.Control
                                    type="search"
                                    placeholder="Search"
                                    className="me-2"
                                    aria-label="Search"
                                />
                                <Button variant="outline-success">Search</Button>
                            </Form>
                        </Offcanvas.Body>
                    </Navbar.Offcanvas>
                </Container>
            </Navbar>
            <CheminPages />
            <Outlet />
        </>
    )
};

export default Layout;