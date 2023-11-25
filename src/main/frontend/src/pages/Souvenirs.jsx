import React from 'react';
import Container from "react-bootstrap/Container";
import styles from './souvenirs.module.css';
import {Image} from "react-bootstrap";
import Form from 'react-bootstrap/Form';
import InputGroupText from "react-bootstrap/InputGroupText"; // Importez les styles CSS gérés par les modules CSS
import SouvenirsComp from '../components/SouvenirsComponent.jsx'

const Souvenirs = () => {
    return (
        <Container>
            <SouvenirsComp />
            <ul className={styles.timeline}>
                <li>
                    <div className={styles['timeline-time']}>
                        <span className={styles.date}>today</span>
                        <span className={styles.time}>04:20</span>
                    </div>
                    <div className={styles['timeline-icon']}>
                        <a >&nbsp;</a>
                    </div>
                    <div className={styles['timeline-body']}>
                        <div className={styles['timeline-header']}>
                            <span className={styles.userimage}><Image src="src/utils/avatar.jpeg"/></span>
                            <span className={styles.username}><a>John Smith</a> <small></small></span>
                            <span className='pull-right text-muted'>18 Views</span>
                        </div>
                        <div className={styles['timeline-content']}>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus turpis quis tincidunt luctus.
                                Nam sagittis dui in nunc consequat, in imperdiet nunc sagittis.
                            </p>
                        </div>
                        <div className={styles['timeline-footer']}>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-comments fa-fw fa-lg m-r-3'></i></a>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-gears fa-fw fa-lg m-r-3'></i></a>
                        </div>
                        <div className={styles['timeline-comment-box']}>
                            <div className={styles.user}><Image src="src/utils/avatar.jpeg"/></div>
                            <div className={styles.input}>
                                <form action="">
                                    <div className='input-group'>
                                        <Form.Control type="text" placeholder="Votre commentaire..." />
                                        <span className='input-group-btn p-l-10'>
                                            <button className='btn btn-primary f-s-12 rounded-corner' type="button">Comment</button>
                                        </span>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div className={styles['timeline-time']}>
                        <span className={styles.date}>today</span>
                        <span className={styles.time}>04:20</span>
                    </div>
                    <div className={styles['timeline-icon']}>
                        <a >&nbsp;</a>
                    </div>
                    <div className={styles['timeline-body']}>
                        <div className={styles['timeline-header']}>
                            <span className={styles.userimage}><Image src="src/utils/avatar.jpeg"/></span>
                            <span className={styles.username}><a>John Smith</a> <small></small></span>
                            <span className='pull-right text-muted'>18 Views</span>
                        </div>
                        <div className={styles['timeline-content']}>
                            <h4 className="template-title">
                                <i className="fa fa-map-marker text-danger fa-fw"></i>
                                795 Folsom Ave, Suite 600 San Francisco, CA 94107
                            </h4>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus turpis quis tincidunt luctus.
                                Nam sagittis dui in nunc consequat, in imperdiet nunc sagittis.
                            </p>
                        </div>
                        <div className={styles['timeline-footer']}>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-comments fa-fw fa-lg m-r-3'></i></a>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-gears fa-fw fa-lg m-r-3'></i></a>
                        </div>
                    </div>
                </li>
                <li>
                    <div className={styles['timeline-time']}>
                        <span className={styles.date}>today</span>
                        <span className={styles.time}>04:20</span>
                    </div>
                    <div className={styles['timeline-icon']}>
                        <a >&nbsp;</a>
                    </div>
                    <div className={styles['timeline-body']}>
                        <div className={styles['timeline-header']}>
                            <span className={styles.userimage}></span>
                            <span className={styles.username}><a>John Smith</a> <small></small></span>
                            <span className='pull-right text-muted'>18 Views</span>
                        </div>
                        <div className={styles['timeline-content']}>
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc faucibus turpis quis tincidunt luctus.
                                Nam sagittis dui in nunc consequat, in imperdiet nunc sagittis.
                            </p>
                            <Image src="src/utils/img.png"/>
                        </div>
                        <div className={styles['timeline-likes']}>
                            <div className={styles['stats-right']}>
                                <span className={styles['stats-text']}>259 Shares</span>
                                <span className={styles['stats-text']}>21 Comments</span>
                            </div>
                            <div className={styles.stats}>

                            </div>
                        </div>
                        <div className='timeline-footer'>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-thumbs-up fa-fw fa-lg m-r-3'></i></a>
                            <a  className='m-r-15 text-inverse-lighter'><i className='fa fa-comments fa-fw fa-lg m-r-3'></i></a>
                        </div>
                        <div className={styles['timeline-comment-box']}>
                            <div className={styles.user}></div>
                            <div className={styles.input}>
                                <form action="">
                                    <div className='input-group'>
                                        <span className='input-group-btn p-l-10'>
                                            <button className='btn btn-primary f-s-12 rounded-corner' type="button">Comment</button>
                                        </span>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </li>
                {/* Les autres éléments de la timeline */}
            </ul>
        </Container>
    );
};

export default Souvenirs;
