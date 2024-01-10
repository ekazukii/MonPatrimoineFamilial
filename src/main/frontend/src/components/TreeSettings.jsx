import {useEffect, useState} from "react";
import {ListGroup, Modal, Stack, Tab, Tabs} from "react-bootstrap";
import {BsCheckCircleFill, BsXCircleFill} from "react-icons/bs";
import Button from "react-bootstrap/Button";

const DemandItem = ({name, handleAccept, handleRefuse}) => {
    return(
        <ListGroup.Item>
            <Stack direction="horizontal" gap={3}>
                <div> {name} </div>
                <div className="ms-auto">
                    <BsCheckCircleFill onClick={handleAccept}/> <BsXCircleFill onClick={handleRefuse}/>
                </div>
            </Stack>
        </ListGroup.Item>
    );
}

const ViewItem = ({name}) => {
    return(
        <ListGroup.Item>
            <Stack direction="horizontal" gap={3}>
                <div> {name} </div>
                <div className="ms-auto">
                    <Button>Aller vers le profil</Button>
                </div>
            </Stack>
        </ListGroup.Item>
    )
}

const names = ["Tommy Bâillât", "Anatoly Panov", "Baptista Loisona"]

const TreeSettings = ({id, defItem, opened, handleClose}) => {
    const [users, setUsers] = useState([]);
    useEffect(() => {
        fetchStats();
    }, [id]);

    const fetchStats = async () => {
        if(!id) return;
        const data = await fetch("http://localhost:8080/tree/view?treeId="+id);
        const json = await data.json();
        setUsers(json.map(viewData => viewData.viewer));
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
                        <p>Vue du profil : {users?.length || 0}</p>
                        <ListGroup>
                            {users.map((user, index) => <ViewItem key={index} name={user.username}/>)}
                        </ListGroup>
                    </Tab>
                    <Tab title="Demandes" eventKey="demands">
                        <h1>Demandes</h1>
                        <ListGroup>
                            {names.map((name, index) => <DemandItem key={index} name={name}/>)}
                        </ListGroup>
                    </Tab>
                    <Tab title="Parmètre généraux" eventKey="settings">
                        <h1>A remplir</h1>
                    </Tab>
                </Tabs>
            </Modal.Body>
        </Modal>
    );
}

export default TreeSettings;