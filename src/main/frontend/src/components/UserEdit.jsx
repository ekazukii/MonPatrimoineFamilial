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
        await fetch(`http://localhost:8080/user`, {
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
            }),
        });
        handleSubmit();
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

            <Stack direction="horizontal" gap={3}>
                <Button variant="primary" onClick={() => handleEditUser()}>
                    Submit
                </Button>
            </Stack>
        </Form>
    )
}

export default UserEdit;