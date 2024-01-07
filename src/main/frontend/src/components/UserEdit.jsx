import Form from "react-bootstrap/Form";
import Stack from "react-bootstrap/Stack";
import Button from "react-bootstrap/Button";
import {useEffect, useState} from "react";

const UserEdit = ({user, isAdmin, handleSubmit}) => {
    const [email, setEmail] = useState("");
    const [username, setUserame] = useState("");
    const [firstName, setFirstName] = useState("");
    const [familyName, setFamilyName] = useState("");
    const [isMale, setIsMale] = useState(true);
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [ssn, setSsn] = useState("");
    const [birthdate, setBirthdate] = useState("");
    const [message, setMessage] = useState(null);

    useEffect(() => {
        if(!user) return;
        setEmail(user.email);
        setUserame(user.username);
        setFamilyName(user.lastname);
        setFirstName(user.firstname);
        setIsMale(user.male);
        setSsn(user.socialSecurityNumber)
        setBirthdate(user.birthdate);
    }, [user]);

    const handleEditUser = async () => {

        if(handleValidation()){
            setMessage(handleValidation);
            return;
        } else {
            setMessage(null);
        }

        const response = await fetch(`http://localhost:8080/user`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: user.id,
                email,
                username,
                firstname: firstName,
                lastname: familyName,
                male: isMale,
                socialSecurityNumber: ssn,
                birthdate,
                oldPassword: oldPassword,
                newPassword: newPassword
            }),
        });
        
        if (!response.ok) {
            let errorBody = await response.text();
            errorBody = "Erreur : " + errorBody;
            setMessage(errorBody);
            return;
        } else {
            setMessage("Les informations ont correctment été enregistrées")
        }

        // handleSubmit();
    };

    const handleValidation = () => {

        // Vérification de la longueur minimale pour le nom et le prénom
        if (firstName.length < 2) {
            return "Erreur : Le nom doit avoir au moins 2 caractères.";
        }

        if (familyName.length < 2) {
            return "Erreur : Le prénom doit avoir au moins 2 caractères.";
        }

        // Validation de l'adresse e-mail
        const emailPattern = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
        if (!emailPattern.test(email)) {
            return "Erreur : L'adresse e-mail n'est pas valide.";
        }

        if (newPassword.length !== 0 && oldPassword.length === 0) {
            return "Erreur: L'ancien mot de passe est vide";
        }

        //Vérification de la longueur minimale du mot de passe
        if (oldPassword.length !== 0 && newPassword.length < 8) {
            return "Erreur: Le nouveau mot de passe doit avoir au moins 8 caractères.";
        }


        //Vérification du format du mot de passe avec un regex (exemple : au moins une lettre majuscule, une lettre minuscule et un chiffre)
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]/;
        if (oldPassword.length !== 0  && !passwordRegex.test(newPassword)) {
            return "Erreur: Le nouveau mot de passe doit contenir au moins une lettre majuscule, une lettre minuscule et un chiffre.";
        }

    };

    if(!user) return <></>

    return(
        <Form>
            <Form.Group className="mb-3" controlId="formBasicUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" disabled={!isAdmin} value={username}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicSecurityNumber">
                <Form.Label>Social security number</Form.Label>
                <Form.Control type="number" placeholder="Enter social security number" disabled={!isAdmin} value={ssn} />
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicBirthd">
                <Form.Label>Birth information</Form.Label>
                <Stack direction="horizontal" gap={3}>
                    <Form.Control type="date" placeholder="Enter Birth Date" value={birthdate} disabled={!isAdmin}/>
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
            {message && (
                    <div className={`${message.startsWith("Erreur") ? "alert alert-danger" : "alert alert-success"} mt-4`}>
                        {message}
                    </div>
                )}
            <Stack direction="horizontal" gap={3}>
                <Button variant="primary" onClick={() => handleEditUser()}>
                    Submit
                </Button>
            </Stack>
        </Form>
    )
}

export default UserEdit;