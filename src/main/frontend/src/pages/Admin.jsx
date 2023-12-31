import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {useEffect, useState} from 'react';
import Modal from 'react-bootstrap/Modal';
import {
    DatatableWrapper,
    Filter,
    Pagination,
    PaginationOptions,
    TableBody,
    TableHeader,
    BulkCheckboxControl
} from 'react-bs-datatable';
import {Table} from "react-bootstrap";
import UserEdit from "../components/UserEdit.jsx";
import Button from "react-bootstrap/Button";

const UserValidation = ({dataUsers, handleSelectedUser}) => {
    // Liste des clés que vous souhaitez extraire
    const keysToExtract = ["id","username", "firstname", "lastname", "email", "validated"];
    // État pour stocker les données extraites
    const [bodies, setBodies] = useState([]);

    const handleDeleteUser = async (userId, username) => {
        if (window.confirm(`Voulez-vous supprimer l'utilisateur ${username}?`)) {
            try {
                const response = await fetch(`http://localhost:8080/user`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        userId: userId,
                    }),
                });

                if (response.ok) {
                    console.log('La suppression a réussi.');
                    // Mettre à jour l'état local en excluant la ligne supprimée
                    setBodies((prevData) => prevData.filter((row) => row.id !== userId));
                } else {
                    console.error('La suppression a échoué.');
                    // Ajoutez ici toute autre logique spécifique à la suppression échouée
                }
            } catch (error) {
                console.error('Erreur lors de la suppression :', error);
                // Ajoutez ici toute autre logique spécifique à l'erreur de suppression
            }
        }
    };

    const handleValidUser = async (userId, username) => {
        if (window.confirm(`Voulez-vous valider l'utilisateur ${username}?`)) {
            try {
                const response = await fetch(`http://localhost:8080/user`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        id: userId,
                        validated: true
                    }),
                });

                if (response.ok) {
                    console.log('La modification a réussi.');
                    // Mettre à jour l'état local en excluant la ligne supprimée
                    setBodies((prevData) => prevData.filter((row) => row.id !== userId));
                } else {
                    console.error('La modification a échoué.');
                    // Ajoutez ici toute autre logique spécifique à la suppression échouée
                }
            } catch (error) {
                console.error('Erreur lors de la modification :', error);
                // Ajoutez ici toute autre logique spécifique à l'erreur de suppression
            }
        }
    };

    useEffect(() => {
        const extractedData = dataUsers.map(record => {
            const extractedRecord = {};
            keysToExtract.forEach(key => {
                if (record.hasOwnProperty(key)) {
                    extractedRecord[key] = record[key];
                }
            });
            return extractedRecord;
        });
        setBodies(extractedData);
    }, [dataUsers]); // dataUsers est ajouté comme dépendance

    // Create table headers consisting of 4 columns.
    const headers= [
        {
            isFilterable: false,
            isSortable: true,
            prop: 'id',
            title: 'Id'
        },
        {
            isFilterable: true,
            isSortable: true,
            prop: 'username',
            title: 'Username'
        },
        {
            isFilterable: false,
            isSortable: true,
            prop: 'firstname',
            title: 'Firstname'
        },
        {
            isFilterable: false,
            isSortable: true,
            prop: 'lastname',
            title: 'Lastname'
        },
        {
            isFilterable: false,
            isSortable: false,
            prop: 'email',
            title: 'Email'
        },
        {
            prop: "button",
            title: "Actions",
            alignment: {
                horizontal: 'center'
            },
            cell: (row) => (
                <>
                    <i className="fi fi-ss-user-check me-2"
                       onClick={() => handleValidUser(row.id, row.username)}
                    ></i>
                    <i
                        className="fi fi-ss-delete-user me-2"
                        onClick={() => handleDeleteUser(row.id, row.username)}
                    ></i>
                    <i
                        className="fi fi-ss-user-gear me-2"
                        onClick={() => handleSelectedUser(row)}
                    ></i>
                </>
            )
        },
        {
            alignment: {
                horizontal: 'center'
            },
            checkbox: {
                className: 'table-checkbox',
                idProp: 'name'
            },
            prop: 'checkbox'
        }
    ];

    console.log(bodies);

    return (
        <Container>
            <DatatableWrapper
                headers={headers}
                body={bodies}
                checkboxProps={{
                    onCheckboxChange: function noRefCheck(){}
                }}
                paginationOptionsProps={{
                    initialState: {
                        options: [
                            5,
                            10,
                            15,
                            20
                        ],
                        rowsPerPage: 10
                    }
                }}
            >
                <Row className="mb-4">
                    <Col
                        className="d-flex flex-col justify-content-end align-items-end"
                        lg={4}
                        xs={6}
                    >
                        <Filter />
                    </Col>
                    <Col
                        className="mt-2"
                        lg={8}
                        xs={6}
                    >
                        <BulkCheckboxControl />
                    </Col>
                </Row>
                <Table>
                    <TableHeader />
                    <TableBody />
                </Table>
                <Row>
                    <Col
                        className="d-flex flex-col justify-content-lg-center align-items-center justify-content-sm-start mb-2 mb-sm-0"
                        lg={4}
                        sm={6}
                        xs={12}
                    >
                        <PaginationOptions alwaysShowPagination />
                    </Col>
                    <Col
                        className="d-flex flex-col justify-content-end align-items-end"
                        lg={8}
                        sm={6}
                        xs={12}
                    >
                        <Pagination alwaysShowPagination />
                    </Col>
                </Row>
            </DatatableWrapper>
        </Container>
    );
}

const AdminPage = () => {
    const [dataAllUsers, setAllUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);

    const fetchAllUsers = async () => {
        try {
            const data = await fetch("http://localhost:8080/users");
            if (!data.ok) {
                throw new Error(`Failed to fetch data. Status: ${data.status}`);
            }
            const jsonData = await data.json();
            setAllUsers(jsonData);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    const editUser = () => {
        setSelectedUser(null);
        fetchAllUsers();
    }

    useEffect(() => {
        fetchAllUsers();
    }, []); // Added an empty dependency array

    return (
        <>
            <Container>
                <Row>
                    <UserValidation dataUsers={dataAllUsers} handleSelectedUser={setSelectedUser}/>
                </Row>
                <Modal show={selectedUser != null} onHide={() => setSelectedUser(null)} size="lg">
                    <Modal.Header closeButton>
                        <Modal.Title>Edit User</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <UserEdit user={selectedUser} isAdmin={true} handleSubmit={editUser}/>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setSelectedUser(null)}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </Container>
        </>
    );
};

export default AdminPage;