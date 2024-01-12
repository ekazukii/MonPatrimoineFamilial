    import React, {useEffect, useState} from 'react';
    import styles from '../pages/souvenirs.module.css';
    import {Image} from 'react-bootstrap';
    import Form from 'react-bootstrap/Form';
    import {format} from 'date-fns';
    import {useSession} from "../hooks/useSession.jsx";


    const SouvenirComponent = ({ data, user }) => {
        const date = new Date(data.date);
        const formattedYear = format(date, 'yyyy');
        const formattedDate = format(date, 'dd/MM');
        const timestampDate = new Date(data.timestamp);
        const formattedTimestampDate = format(timestampDate, 'dd/MM/yyyy HH:mm');

        const [isDivVisible, setIsDivVisible] = useState(false);
        const [imageSouvenir, setImageSouvenir] = useState(null);

        const handleButtonClick = () => {
            setIsDivVisible(!isDivVisible);
        };

        const handleButtonDelete = async (msgId) => {
            if (window.confirm(`Voulez-vous vraiment supprimer le souvenir?`)) {
                try {
                    const response = await fetch(`http://localhost:8080/famille/message?msgId=${msgId}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });

                    if (response.ok) {
                        console.log('La suppression a réussi.');
                    } else {
                        console.error('La suppression a échoué.');
                        // Ajoutez ici toute autre logique spécifique à la suppression échouée
                    }
                } catch (error) {
                    console.error('Erreur lors de la suppression :', error);
                    // Ajoutez ici toute autre logique spécifique à l'erreur de suppression
                }
            }
        }

        useEffect(() => {
            const fetchImage = async () => {
                if (data.file_id !== 0) {
                    const imageInfo = await getImage(data.file_id);
                    setImageSouvenir(imageInfo);
                }
            };

            fetchImage();
        }, [data.file_id]);

        const getImage = async (id_file) => {
            const dataFile = await fetch(`http://localhost:8080/files/info?id=${id_file}`);
            return await dataFile.json();
        };

        console.log(data);
        return (
            <li>
                <div className={styles['timeline-time']}>
                    <span className={styles.date}>{formattedYear}</span>
                    <span className={styles.time}>{formattedDate}</span>
                </div>
                <div className={styles['timeline-icon']}>
                    <a>&nbsp;</a>
                </div>
                <div className={styles['timeline-body']}>
                    <div className={styles['timeline-header']}>
              <span className={styles.userimage}>
                <Image src="src/utils/avatar.jpeg" />
              </span>
                        <span className={styles.username}>
                <a>{user.firstname + " " + user.lastname}</a> <small></small>
              </span>
                        <span className="pull-right text-muted">{formattedTimestampDate}</span>
                    </div>
                    <div className={styles['timeline-content']}>
                        <p>{data.message}</p>
                        {imageSouvenir && imageSouvenir.fileName && (
                            <Image src={`src/utils/${imageSouvenir.fileName}`} />
                        )}
                    </div>
                    <div className={styles['timeline-footer']}>
                        <a onClick={handleButtonClick} className="m-r-15 text-inverse-lighter">
                            <i className="fa fa-comments fa-fw fa-lg m-r-3"></i>
                        </a>
                        {
                            user.id === data.user_id && (
                            <a onClick={() => handleButtonDelete(data.id)} className="m-r-15 text-inverse-lighter">
                                <i className="fa fa-trash fa-fw fa-lg m-r-3"></i>
                            </a>
                            )}
                    </div>
                    <div
                        className={`${styles['timeline-comment-box']} ${
                            styles['transition-div']
                        } ${isDivVisible ? styles['visible'] : styles['hidden']}`}
                    >
                        <GetCommentaire id_souvenir={data.id} id_conv={data.user_id} />
                    </div>
                </div>
            </li>
        );
    };

    const GetCommentaire = ({ id_conv, id_souvenir }) => {
        const [json, setJson] = useState([]);

        const updateComments = async () => {
            const data = await fetch(`http://localhost:8080/conversation/commentary?conv=${id_conv}&souvenir=${id_souvenir}`);
            const jsonData = await data.json();
            setJson(jsonData);
        };

        useEffect(() => {
            const fetchData = async () => {
                const data = await fetch(`http://localhost:8080/conversation/commentary?conv=${id_conv}&souvenir=${id_souvenir}`);
                const jsonData = await data.json();
                setJson(jsonData);
            };

            fetchData();
        }, [id_conv, id_souvenir]);

        useEffect(() => {
            updateComments();
        }, [id_conv, id_souvenir]);

        return (
            <>
                {json.map((data) => (
                    <div key={data.id}>
                        <div className={styles.user}>
                            <Image src="src/utils/avatar.jpeg" />
                        </div>
                        <div className={styles.input}>
                            <div className="input-group">
                                <Form.Control type="text" placeholder={data.message} disabled={true} />
                            </div>
                        </div>
                    </div>
                ))}
                <PostCommentary updateComments={updateComments} id_conv={id_conv} id_souvenir={id_souvenir} />
            </>
        );
    };

    const PostCommentary = ({ updateComments, id_conv, id_souvenir }) => {
        const [comment, setComment] = useState('');

        const postComm = async (id_conv, id_souvenir, message) => {
            const data = await fetch('http://localhost:8080/conversation/commentary', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    conv: id_conv,
                    souvenir: id_souvenir,
                    message: message,
                }),
            });
            return await data.json();
        };

        const handleCommentSubmit = async () => {
            if (comment.trim() === '') {
                return;
            }

            await postComm(id_conv, id_souvenir, comment);
            updateComments();
            setComment('');
        };

        return (
            <div>
                <div className={styles.user}>
                    <Image src="src/utils/avatar.jpeg" />
                </div>
                <div className={styles.input}>
                    <form action="">
                        <div className="input-group">
                            <Form.Control
                                type="text"
                                placeholder="Votre commentaire..."
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                            />
                            <span className="input-group-btn p-l-10">
                  <button onClick={handleCommentSubmit} className="btn btn-primary f-s-12 rounded-corner" type="button">
                    Comment
                  </button>
                </span>
                        </div>
                    </form>
                </div>
            </div>
        );
    };

    const NewSouvenir = ({id_conv, myUserInfo, updateSouvenir}) => {
        const [souvenir, setSouvenir] = useState('');
        const [dateSouvenir, setDateSouvenir] = useState('');
        const [fileSouvenir, setFileSouvenir] = useState(null);

        const [fileInputKey, setFileInputKey] = useState(Date.now());

        const date = new Date();
        const formattedDate = format(date, 'dd/MM/yyyy HH:mm');

        const postSouvenir = async (id_conv, user_id, message, date, fileUpload) => {
            let fileId = null;
            if (fileUpload !== null) {
                const formData = new FormData();
                formData.append('file', fileUpload);

                const fileUploadResponse = await fetch('http://localhost:8080/files/upload', {
                    method: 'POST',
                    body: formData,
                });

                if (fileUploadResponse.ok) {
                    const fileUploadData = await fileUploadResponse.json();
                    fileId = fileUploadData.id;
                    console.log(user_id);
                    return postMessage(id_conv, user_id, message, date, fileId);
                } else {
                    console.error('Error uploading file:', fileUploadResponse.statusText);
                    // Handle the error, e.g., show an error message to the user
                    throw new Error('Failed to upload file');
                }
            } else {
                console.log(user_id);
                return postMessage(id_conv, user_id, message, date, fileId);
            }
        };

        const postMessage = async (id_conv, user_id, message, date, fileId) =>{
            const messageResponse = await fetch('http://localhost:8080/conversation/msg', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    conv: id_conv,
                    user_id: user_id,
                    message: message,
                    date: date,
                    file_id: fileId, // Include the file ID in the message request
                }),
            });

            if (messageResponse.ok) {
                return await messageResponse.json();
            } else {
                console.error('Error sending message:', messageResponse.statusText);
                throw new Error('Failed to send message');
            }
        }

        const handleSouvenirSubmit = async () => {
            if (souvenir.trim() === '') {
                return;
            }
            console.log(myUserInfo);
            await postSouvenir(id_conv, myUserInfo.id, souvenir, dateSouvenir, fileSouvenir);
            updateSouvenir();
            setSouvenir('');
            setDateSouvenir('');
            setFileSouvenir(null);
            setFileInputKey(Date.now());
        };

        return(
            <li>
                <div className={styles['timeline-time']}>
                    <span className={styles.date}>Année</span>
                    <span className={styles.time}>Jour/Mois</span>
                </div>
                <div className={styles['timeline-icon']}>
                    <a>&nbsp;</a>
                </div>
                <div className={styles['timeline-body']}>
                    <div className={styles['timeline-header']}>
                          <span className={styles.userimage}>
                            <Image src="src/utils/avatar.jpeg" />
                          </span>
                                    <span className={styles.username}>
                            <a>{myUserInfo.firstname + " " + myUserInfo.lastname}</a> <small></small>
                          </span>
                          <span className="pull-right text-muted">{formattedDate}</span>
                    </div>
                    <div className={styles['timeline-content']}>
                        <Form.Label>La date de votre souvenir :</Form.Label>
                        <Form.Control
                            type="date"
                            value={dateSouvenir}
                            onChange={(e) => setDateSouvenir(e.target.value)}
                            className="mb-2"
                        />
                        <Form.Label>Le contenu de votre souvenir :</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Votre souvenir..."
                            value={souvenir}
                            onChange={(e) => setSouvenir(e.target.value)}
                        />
                        <Form.Label>La photo de votre souvenir :</Form.Label>
                        <Form.Control
                            key={fileInputKey}
                            type="file"
                            onChange={(e) => setFileSouvenir(e.target.files[0])}
                        />
                    </div>
                    <div className={styles['timeline-footer']}>
                        <a onClick={handleSouvenirSubmit} className="m-r-15 text-inverse-lighter">
                            <i className="fa fa-send fa-fw fa-lg m-r-3"></i>
                        </a>
                    </div>
                </div>
            </li>
        );
    }

    export default function GetSouvenirs() {
        const [jsonMsg, setJsonMsg] = useState([]);
        const [jsonUsers, setJsonUsers] = useState([]);
        const { user, isLoggedIn, setSession, login, refreshData, logout } = useSession();

        // Fonction pour mettre à jour les souvenirs après l'ajout d'un nouveau souvenir
        const updateSouvenir = async () => {
            const data = await fetch('http://localhost:8080/conversation/famille/messages', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user.id),
            });
            const jsonData = await data.json();
            const sortedJsonData = [...jsonData].sort((a, b) => new Date(b.date) - new Date(a.date));

            setJsonMsg(sortedJsonData);
        };

        useEffect(() => {
            const getMessagesConnected = async () => {
                const data = await fetch('http://localhost:8080/conversation/famille/messages', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(user.id),
                });
                if(data.ok){
                    const jsonData = await data.json();
                    const sortedJsonData = [...jsonData].sort((a, b) => new Date(b.date) - new Date(a.date));
                    setJsonMsg(sortedJsonData);
                }
            };

            const getInfoUsers = async () => {
                const response = await fetch(`http://localhost:8080/conversation/famille/users`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(user.id),
                });
                const jsonData = await response.json();
                setJsonUsers(jsonData);
            }

            getInfoUsers();
            getMessagesConnected();
        }, []);

        return (
            <ul className={styles.timeline}>
                {isLoggedIn && <NewSouvenir id_conv={user.id} myUserInfo={user} updateSouvenir={updateSouvenir} />}
                {jsonMsg.map((data) => {
                    const currentUser = jsonUsers.find(user => user.id === data.user_id);
                    return currentUser && <SouvenirComponent key={data.id} data={data} user={currentUser} />;
                })}
            </ul>
        );
    }