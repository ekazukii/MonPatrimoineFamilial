import React, {useEffect, useState} from "react";
import {ListGroup, Modal, Stack, Tab, Tabs} from "react-bootstrap";
import {BsCheckCircleFill, BsXCircleFill} from "react-icons/bs";
import Button from "react-bootstrap/Button";
import {Link} from "react-router-dom";
import {useSession} from "../hooks/useSession.jsx";

const DemandItem = ({myUser, userToMerge, handleAccept, handleRefuse}) => {
    const [isMerging, setIsMerging] = useState(false);
    const [mergeStatus, setMergeStatus] = useState(null);
    const mergeWith = async(user2) => {

        try {
            setIsMerging(true);

            const response = await fetch('http://localhost:8080/tree/mergeStrategy', {

                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    requestingTreeId: myUser.tree,
                    respondingTreeId: user2.tree,
                    // parentsNodesRequester: null,
                    // childrenNodesRequester: null,
                    idRequester: myUser.id,
                    idResponder: user2.id
                })
            });

            if (response.ok) {
                const responseBody = await response.text();
                setMergeStatus("Successfully merged");
            } else {
                const errorBody = await response.text();
                setMergeStatus("Erreur: " + errorBody); // Utilisez directement errorBody
            }

            setIsMerging(false);
        } catch (error) {
            setMergeStatus("Erreur de connexion au serveur");
            setIsMerging(false);
        }
    }

    return(
        <>
            <ListGroup.Item>
                <Stack direction="horizontal" gap={3}>
                    <div> {userToMerge.username} ({userToMerge.firstname} {userToMerge.lastname}) </div>
                    <div className="ms-auto">
                        <Button onClick={() => mergeWith(userToMerge) } disabled={isMerging}>Fusionner</Button>
                    </div>
                </Stack>
            </ListGroup.Item>
            <Modal
                show={mergeStatus}
                onHide={() => setModalMerge(false)}
                size="lg"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        <h1>Message</h1>
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {mergeStatus && (
                        <div className={`${mergeStatus.startsWith("Erreur") ? "alert alert-danger" : "alert alert-success"} mt-4`}>
                            {mergeStatus}
                        </div>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setMergeStatus(null)}>Fermer</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

const ViewItem = ({ user, total }) => {
    return (
        <ListGroup.Item>
            <Stack direction="horizontal" gap={3}>
                <div> {user.username} ({total} vues)</div>
                <div className="ms-auto">
                    <Link to={`/external?id=${user.tree}`}>
                        <Button>Aller vers le profil</Button>
                    </Link>
                </div>
            </Stack>
        </ListGroup.Item>
    );
};

const names = ["Tommy Bâillât", "Anatoly Panov", "Baptista Loisona"]

const TreeSettings = ({id, defItem, opened, handleClose}) => {
    const { user, isLoggedIn } = useSession();
    const [viewers, setViewers] = useState([]);
    const [usersToMerge, setUsersToMerge] = useState([]);
    useEffect(() => {
        fetchStats();
        fetchUsersPossibleToMerge();
    }, [id]);

    const fetchStats = async () => {
        if(!id) return;
        const data = await fetch("http://localhost:8080/tree/view?treeId="+id);
        const json = await data.json();
        setViewers(json);
    }

    const fetchUsersPossibleToMerge = async () => {
        if(!id) return;
        const data = await fetch("http://localhost:8080/tree/findUserAccount?treeId="+id);
        const json = await data.json();
        setUsersToMerge(json);
    }

    return (
        <Modal show={opened} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Paramètres de mon arbre</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Tabs
                    defaultActiveKey={defItem}
                >
                    <Tab title="Statistiques" eventKey="stats">
                        <h1>Stats</h1>
                        <p>Nombre d'utilisateurs ayant vu l'arbre : {viewers?.length || 0}</p>
                        <ListGroup>
                            {viewers.map((viewer, index) => <ViewItem key={index} user={viewer.viewer} total={viewer.total}/>)}
                        </ListGroup>
                    </Tab>
                    <Tab title="Fusions" eventKey="demands">
                        <h1>Fusion</h1>
                        <p>Membres de votre famille avec un compte :</p>
                        <ListGroup>
                            {usersToMerge.map((userToMerge, index) =>
                                userToMerge.username !== user.username ? <DemandItem key={index} userToMerge={userToMerge} myUser={user}/> : null
                            )}
                        </ListGroup>
                    </Tab>
                </Tabs>
            </Modal.Body>
        </Modal>
    );
}

export default TreeSettings;