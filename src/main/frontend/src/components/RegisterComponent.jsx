import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import React, {useState} from "react";
import Row from "react-bootstrap/Row";
import {FormGroup} from "react-bootstrap";

export const personalInfo = ({ onPersonalInfoChange, personalInfo }) => {
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
        if (localPersonalInfo.nom.length < 2) {
            errors.nom = "Le nom doit avoir au moins 2 caractères.";
        }
        if (localPersonalInfo.prenom.length < 2) {
            errors.prenom = "Le prénom doit avoir au moins 2 caractères.";
        }

        // Validation de l'adresse e-mail
        const emailPattern = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
        if (!emailPattern.test(localPersonalInfo.mail)) {
            errors.mail = "L'adresse e-mail n'est pas valide.";
        }

        // Validation du numéro de sécurité sociale (exemple : 15 caractères)
        if (localPersonalInfo.numeroSecu.length !== 15) {
            errors.numeroSecu = "Le numéro de sécurité sociale doit avoir 15 caractères.";
        }

        // Validation de l'âge (entre 18 et 100 ans)
        const age = calculateAge(localPersonalInfo.dateNaissance);
        if (age < 18 || age >= 100) {
            errors.dateNaissance = "L'âge doit être entre 18 et 99 ans.";
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
            <Row className="d-flex flex-row justify-content-around">
                <Col className="col-5">
                    <Form.Group className="mb-3" controlId="formInfoFirstName">
                        <Form.Label>Nom {validationErrors.nom && (<span className="text-danger">{validationErrors.nom}</span>)}</Form.Label>
                        <Form.Control
                            type="text"
                            name="nom"
                            placeholder="Votre nom"
                            value={localPersonalInfo.nom}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoMail">
                        <Form.Label>Email {validationErrors.mail && (<span className="text-danger">{validationErrors.mail}</span>)}</Form.Label>
                        <Form.Control
                            type="email"
                            name="mail"
                            placeholder="Votre email"
                            value={localPersonalInfo.mail}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoSocial">
                        <Form.Label>Numéro sécurité sociale {validationErrors.numeroSecu && (<span className="text-danger">{validationErrors.numeroSecu}</span>)}</Form.Label>
                        <Form.Control
                            type="number"
                            name="numeroSecu"
                            placeholder="Numéro sécurité sociale"
                            value={localPersonalInfo.numeroSecu}
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
                        <Form.Label>Prénom {validationErrors.prenom && (<span className="text-danger">{validationErrors.prenom}</span>)}</Form.Label>
                        <Form.Control
                            type="text"
                            name="prenom"
                            placeholder="Votre prénom"
                            value={localPersonalInfo.prenom}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoBirthday">
                        <Form.Label>Date de naissance {validationErrors.dateNaissance && (<span className="text-danger">{validationErrors.dateNaissance}</span>)}</Form.Label>
                        <Form.Control
                            type="date"
                            name="dateNaissance"
                            placeholder="Votre date de naissance"
                            value={localPersonalInfo.dateNaissance}
                            onChange={handleChange}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formInfoOrigin">
                        <Form.Label>Nationalité d'origine</Form.Label>
                        <Form.Control
                            type="text"
                            name="nationalite"
                            placeholder="Nationalité"
                            value={localPersonalInfo.nationalite}
                            onChange={handleChange}
                        />
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
            <Row>
                <Button onClick={handleValidation}>Etape suivante</Button>
            </Row>
        </>
    );
}

export const documentImport = ({ onDocumentImport, documentImport }) => {
    const [validationErrors, setValidationErrors] = useState([]);
    const [localDocumentImport, setLocalDocumentImport] = useState(documentImport);

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

    const handleFileChange = (e) => {
        const { name, files } = e.target;
        const file = files[0];
        setLocalDocumentImport({ ...localDocumentImport, [name]: file });

        // Afficher le nom du fichier sélectionné à côté de l'élément <input>
        const fileNameDisplay = document.querySelector(`#${name}-file-name-display`);
        if (fileNameDisplay) {
            fileNameDisplay.textContent = file ? file.name : "";
        }
    };

    const handleValidation = () => {
        const errors = [];
        const allowedExtensions = ["pdf", "jpeg", "jpg", "png"];

        const carteIdentiteError = validateFileExtensions(localDocumentImport.carteIdentite, allowedExtensions);
        if (carteIdentiteError) {
            errors.push(carteIdentiteError);
        }

        const photoError = validateFileExtensions(localDocumentImport.photo, allowedExtensions);
        if (photoError) {
            errors.push(photoError);
        }

        if (errors.length === 0) {
            // Aucune erreur de validation, envoi des données au parent
            onDocumentImport(localDocumentImport);
        } else {
            // Il y a des erreurs, mettez-les à jour
            setValidationErrors(errors);
        }
    };

    return (
        <>
            <Row>
                <Col className="col-10">
                    <Form.Group controlId="formFileCarteIdentite" className="mb-3">
                        <Form.Label>Carte d'identité</Form.Label>
                        <Form.Control type="file" name="carteIdentite" onChange={handleFileChange}/>
                        {localDocumentImport.carteIdentite.name !== "" && (
                            <Form.Text className="text-muted">
                                Document importé : {localDocumentImport.carteIdentite.name}
                            </Form.Text>
                        )}
                    </Form.Group>
                    <Form.Group controlId="formFilePhoto" className="mb-3">
                        <Form.Label>Photo</Form.Label>
                        <Form.Control type="file" name="photo" onChange={handleFileChange}/>
                        {localDocumentImport.photo.name !== "" && (
                            <Form.Text className="text-muted">
                                Document importé : {localDocumentImport.photo.name}
                            </Form.Text>
                        )}
                    </Form.Group>
                </Col>
            </Row>
            <Row>
                {validationErrors.length > 0 && (
                    <div className="text-danger">
                        {validationErrors.map((error, index) => (
                            <div key={index}>{error}</div>
                        ))}
                    </div>
                )}
            </Row>
            <Row>
                <Button onClick={handleValidation}>Etape suivante</Button>
            </Row>
        </>
    );
};

export const validationInfo = ({ formData }) => {
    return (
        <Form className="d-flex flex-row justify-content-around">
            <Col className="col-5">
                <FormGroup>
                    <Form.Label>Nom</Form.Label>
                    <Form.Control type="text" value={formData.personalInfo.nom} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Prénom</Form.Label>
                    <Form.Control type="text" value={formData.personalInfo.prenom} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={formData.personalInfo.mail} disabled />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Numéro de sécurité sociale</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.personalInfo.numeroSecu}
                        disabled
                    />
                </FormGroup>
            </Col>
            <Col className="col-5">
                <FormGroup>
                    <Form.Label>Date de naissance</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.personalInfo.dateNaissance}
                        disabled
                    />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Nationalité d'origine</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.personalInfo.nationalite}
                        disabled
                    />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Carte d'identité</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.documentImport.carteIdentite.name}
                        disabled
                    />
                </FormGroup>
                <FormGroup>
                    <Form.Label>Photo</Form.Label>
                    <Form.Control
                        type="text"
                        value={formData.documentImport.photo.name}
                        disabled
                    />
                </FormGroup>
            </Col>
        </Form>
    );
}