import React, { useEffect, useState } from 'react';
import styles from '../pages/souvenirs.module.css';
import { Image } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import { format } from 'date-fns';
import {useSession} from "../hooks/useSession.jsx";


const SouvenirComponent = ({ data }) => {
    const date = new Date(data.date);
    const formattedYear = format(date, 'yyyy');
    const formattedDate = format(date, 'dd/MM');
    const timestampDate = new Date(data.timestamp);
    const formattedTimestampDate = format(timestampDate, 'dd/MM/yyyy HH:mm');

    const [isDivVisible, setIsDivVisible] = useState(false);

    const handleButtonClick = () => {
        setIsDivVisible(!isDivVisible);
    };

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
            <a>John Smith</a> <small></small>
          </span>
                    <span className="pull-right text-muted">{formattedTimestampDate}</span>
                </div>
                <div className={styles['timeline-content']}>
                    <p>{data.message}</p>
                </div>
                <div className={styles['timeline-footer']}>
                    <a onClick={handleButtonClick} className="m-r-15 text-inverse-lighter">
                        <i className="fa fa-comments fa-fw fa-lg m-r-3"></i>
                    </a>
                    <a className="m-r-15 text-inverse-lighter">
                        <i className="fa fa-gears fa-fw fa-lg m-r-3"></i>
                    </a>
                </div>
                <div
                    className={`${styles['timeline-comment-box']} ${
                        styles['transition-div']
                    } ${isDivVisible ? styles['visible'] : styles['hidden']}`}
                >
                    <GetCommentaire id_souvenir={data.id} id_conv={4} />
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

    const date = new Date();
    const formattedDate = format(date, 'dd/MM/yyyy HH:mm');

    const postSouvenir = async (id_conv, user_id, message, date) => {
        const data = await fetch('http://localhost:8080/conversation/msg', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                conv: id_conv,
                user_id: myUserInfo.id,
                message: message,
                date: date
            }),
        });
        return await data.json();
    };

    const handleSouvenirSubmit = async () => {
        if (souvenir.trim() === '') {
            return;
        }

        await postSouvenir(id_conv, 1, souvenir, dateSouvenir);
        updateSouvenir();
        setSouvenir('');
        setDateSouvenir('');
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
    const [json, setJson] = useState([]);
    const { user, isLoggedIn, setSession, login, refreshData, logout } = useSession();

    // Fonction pour mettre à jour les souvenirs après l'ajout d'un nouveau souvenir
    const updateSouvenir = async () => {
        const data = await fetch('http://localhost:8080/conversation/famille?conv=4');
        const jsonData = await data.json();
        const sortedJsonData = [...jsonData].sort((a, b) => new Date(b.date) - new Date(a.date));

        setJson(sortedJsonData);
    };

    useEffect(() => {
        const fetchData = async () => {
            const data = await fetch('http://localhost:8080/conversation/famille?conv=4');
            const jsonData = await data.json();
            const sortedJsonData = [...jsonData].sort((a, b) => new Date(b.date) - new Date(a.date));

            setJson(sortedJsonData);
        };

        fetchData();
    }, []);

    return (
        <ul className={styles.timeline}>
            {isLoggedIn && <NewSouvenir id_conv={4} myUserInfo={user} updateSouvenir={updateSouvenir} />}
            {json.map((data) => (
                <SouvenirComponent key={data.id} data={data} />
            ))}
        </ul>
    );
}