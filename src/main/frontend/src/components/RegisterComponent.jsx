import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import React, {useState} from "react";
import Row from "react-bootstrap/Row";
import {FormGroup} from "react-bootstrap";

export const personalInfo = ({ onPersonalInfoChange, personalInfo, display }) => {
    const [localPersonalInfo, setLocalPersonalInfo] = useState(personalInfo);

    const [validationErrors, setValidationErrors] = useState({});

    const calculateAge = (birthDate) => {
        const today = new Date();
        const birthDateArray = birthDate.split("-");
        const birthYear = parseInt(birthDateArray[0]);
        const birthMonth = parseInt(birthDateArray[1]);
        const birthDay = parseInt(birthDateArray[2]);
        let age = today.getFullYear() - birthYear;

        if (
            today.getMonth() < birthMonth ||
            (today.getMonth() === birthMonth && today.getDate() < birthDay)
        ) {
            age--;
        }

        return age;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLocalPersonalInfo({ ...localPersonalInfo, [name]: value });
    };

    const handleValidation = () => {
        const errors = {};

        // Vérification de la longueur minimale pour le nom et le prénom
        if (localPersonalInfo.lastName.length < 2) {
            errors.lastName = "Le nom doit avoir au moins 2 caractères.";
        }
        if (localPersonalInfo.firstName.length < 2) {
            errors.firstName = "Le prénom doit avoir au moins 2 caractères.";
        }

        // Validation de l'adresse e-mail
        const emailPattern = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
        if (!emailPattern.test(localPersonalInfo.email)) {
            errors.email = "L'adresse e-mail n'est pas valide.";
        }

        // Validation du numéro de sécurité sociale (exemple : 15 caractères)
        if (localPersonalInfo.socialSecurityNumber.length !== 15) {
            errors.socialSecurityNumber = "Le numéro de sécurité sociale doit avoir 15 caractères.";
        }

        // Validation de l'âge (entre 18 et 100 ans)
        const age = calculateAge(localPersonalInfo.birthDate);
        if (age < 18 || age >= 100) {
            errors.birthDate = "L'âge doit être entre 18 et 99 ans.";
        }

        if (localPersonalInfo.male === "") {
            errors.sexe = "Vous devez selectionner votre sexe"
        }

        // Vérification des mots de passe identiques
        if (localPersonalInfo.password !== localPersonalInfo.password2) {
            errors.password2 = "Les mots de passe ne correspondent pas.";
        }

        // Vérification de la longueur minimale du mot de passe
        if (localPersonalInfo.password.length < 8) {
            errors.password = "Le mot de passe doit avoir au moins 8 caractères.";
        }

        // Vérification du format du mot de passe avec un regex (exemple : au moins une lettre majuscule, une lettre minuscule et un chiffre)
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]/;
        if (!passwordRegex.test(localPersonalInfo.password)) {
            errors.password = "Le mot de passe doit contenir au moins une lettre majuscule, une lettre minuscule et un chiffre.";
        }

        if (Object.keys(errors).length === 0) {
            // Aucune erreur de validation, envoi des données au parent
            onPersonalInfoChange(localPersonalInfo);
        } else {
            // Il y a des erreurs, mettez-les à jour
            setValidationErrors(errors);
        }
    };

    return(
        <>
            <Row className={display ? "d-flex flex-row justify-content-around" : "d-none"} >
                <Col className="col-5">
                    <Form.Group className="mb-3" controlId="formInfoFirstName">
                        <Form.Label>Nom {validationErrors.lastName && (<span className="text-danger">{validationErrors.lastName}</span>)}</Form.Label>
                        <Form.Control
                            type="text"
                            name="lastName"
                            placeholder="Votre nom"
                            value={localPersonalInfo.lastName}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoMail">
                        <Form.Label>Email {validationErrors.email && (<span className="text-danger">{validationErrors.email}</span>)}</Form.Label>
                        <Form.Control
                            type="email"
                            name="email"
                            placeholder="Votre email"
                            value={localPersonalInfo.email}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoSocial">
                        <Form.Label>Numéro sécurité sociale {validationErrors.socialSecurityNumber && (<span className="text-danger">{validationErrors.socialSecurityNumber}</span>)}</Form.Label>
                        <Form.Control
                            type="number"
                            name="socialSecurityNumber"
                            placeholder="Numéro sécurité sociale"
                            value={localPersonalInfo.socialSecurityNumber}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoPassword">
                        <Form.Label>Password {validationErrors.password && (<span className="text-danger">{validationErrors.password}</span>)}</Form.Label>
                        <Form.Control
                            type="password"
                            name="password"
                            placeholder="Mot de passe"
                            value={localPersonalInfo.password}
                            onChange={handleChange}
                        />
                        <Form.Text className="text-muted">
                            We'll never share your password with anyone else.
                        </Form.Text>
                    </Form.Group>
                </Col>
                <Col className="col-5">
                    <Form.Group className="mb-3" controlId="formInfoLastName">
                        <Form.Label>Prénom {validationErrors.firstName && (<span className="text-danger">{validationErrors.firstName}</span>)}</Form.Label>
                        <Form.Control
                            type="text"
                            name="firstName"
                            placeholder="Votre prénom"
                            value={localPersonalInfo.firstName}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoBirthday">
                        <Form.Label>Date de naissance {validationErrors.birthDate && (<span className="text-danger">{validationErrors.birthDate}</span>)}</Form.Label>
                        <Form.Control
                            type="date"
                            name="birthDate"
                            placeholder="Votre date de naissance"
                            value={localPersonalInfo.birthDate}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoOrigin">
                        <Form.Label>Sexe {validationErrors.sexe && (<span className="text-danger">{validationErrors.sexe}</span>)}</Form.Label>
                        <Form.Select name="male" value={localPersonalInfo.male} onChange={handleChange}>
                            <option value="">Select Gender</option> 
                            <option value="true">Male</option>
                            <option value="false">Female</option>
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoPasswordConfirm">
                        <Form.Label>Password {validationErrors.password2 && (<span className="text-danger">{validationErrors.password2}</span>)}</Form.Label>
                        <Form.Control
                            type="password"
                            name="password2"
                            placeholder="Mot de passe"
                            value={localPersonalInfo.password2}
                            onChange={handleChange}
                        />
                        <Form.Text className="text-muted">
                            We'll never share your password with anyone else.
                        </Form.Text>
                    </Form.Group>
                </Col>
            </Row>
            <Row className={display ? "" : "d-none"} >
                <Button onClick={handleValidation}>Etape suivante</Button>
            </Row>
        </>
    );
}

export const documentImport = ({ display, handleGoNext, idCardRef, photoRef }) => {
    const [validationErrors, setValidationErrors] = useState([]);

    const validateFileExtensions = (file, allowedExtensions) => {
        if (file) {
            const fileNameParts = file.name.split(".");
            const fileExtension = fileNameParts[fileNameParts.length - 1].toLowerCase();

            if (!allowedExtensions.includes(fileExtension)) {
                return `L'extension du fichier "${file.name}" n'est pas valide.`;
            }
        }

        return null;
    };

    const handleValidation = () => {
        const errors = [];
        const allowedExtensions = ["pdf", "jpeg", "jpg", "png"];

        console.log(idCardRef.current.files[0].name)
        console.log(photoRef.current.files[0].name)

        const carteIdentiteError = validateFileExtensions(idCardRef.current.files[0], allowedExtensions);
        if (carteIdentiteError) {
            errors.push(carteIdentiteError);
        }

        const photoError = validateFileExtensions(photoRef.current.files[0], allowedExtensions);
        if (photoError) {
            errors.push(photoError);
        }

        if (errors.length === 0) {
            handleGoNext();
        } else {
            // Il y a des erreurs, mettez-les à jour
            setValidationErrors(errors);
        }
    };

    return (
        <>
            <Row className={display ? "" : "d-none"}>
                <Col className="col-10">
                    <Form.Group controlId="formFileCarteIdentite" className="mb-3">
                        <Form.Label>Carte d'identité</Form.Label>
                        <Form.Control type="file" name="carteIdentite" ref={idCardRef}/>
                    </Form.Group>
                    <Form.Group controlId="formFilePhoto" className="mb-3">
                        <Form.Label>Photo</Form.Label>
                        <Form.Control type="file" name="photo" ref={photoRef}/>
                    </Form.Group>
                </Col>
            </Row>
            <Row className={display ? "" : "d-none"}>
                {validationErrors.length > 0 && (
                    <div className="text-danger">
                        {validationErrors.map((error, index) => (
                            <div key={index}>{error}</div>
                        ))}
                    </div>
                )}
            </Row>
            <Row className={display ? "" : "d-none"}>
                <Button onClick={handleValidation}>Etape suivante</Button>
            </Row>
        </>
    );
};

export const validationInfo = ({ formData, display, idCardRef, photoRef }) => {
    return (
        <Form className={display ? "d-flex flex-row justify-content-around" : "d-none"}>
            <Col className="col-5">
                <FormGroup>
                    <Form.Label>Nom</Form.Label>
                    <Form.Control type="text" value={formData.personalInfo.lastName} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Prénom</Form.Label>
                    <Form.Control type="text" value={formData.personalInfo.firstName} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={formData.personalInfo.email} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Numéro de sécurité sociale</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.personalInfo.socialSecurityNumber}
                        disabled
                    />
                </FormGroup>
            </Col>
            <Col className="col-5">
                <FormGroup>
                    <Form.Label>Date de naissance</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.personalInfo.birthDate}
                        disabled
                    />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Nationalité d'origine</Form.Label>
                    <Form.Select value={formData.personalInfo.male} disabled={true}>
                        <option value="true">Male</option>
                        <option value="false">Female</option>
                    </Form.Select>
                </FormGroup>
                <FormGroup>
                    <Form.Label>Carte d'identité</Form.Label>
                    <Form.Control
                        type="text"
                        value={idCardRef.current?.files[0]?.name || "No file"}
                        disabled
                    />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Photo</Form.Label>
                    <Form.Control
                        type="text"
                        value={photoRef.current?.files[0]?.name || "No file"}
                        disabled
                    />
                </FormGroup>
            </Col>
        </Form>
    );
}