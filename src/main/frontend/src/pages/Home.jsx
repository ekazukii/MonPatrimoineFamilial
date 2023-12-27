import Button from 'react-bootstrap/Button';
import TreeComponent from '../components/TreeComponent';
import Section from '../components/Section';
import Stack from 'react-bootstrap/Stack';
import img1 from "../../../resources/img/home/home-1-nobg.png";
import img2 from "../../../resources/img/home/home-2-nobg.png";
import img3 from "../../../resources/img/home/home-3-nobg.png";
import img4 from "../../../resources/img/home/home-4-nobg.png";
import img5 from "../../../resources/img/home/home-5-nobg.png";
import {useSession} from "../hooks/useSession.jsx";
import './Home.css';

const Home = () => {
    const fetchTestUsr = async () => {
        const data = await fetch("http://localhost:8080/userrdm");
        const json = await data.json();
        
        alert(json);
    }
    
    const { user, isLoggedIn, setSession, login, refreshData, logout } = useSession();

    return (
        <div>
            <div className="sections">
                
                <Section title="Section 1">
                    <div className='section'>
                        <div className="text">
                            <h1 className='title1'>Bienvenue sur Arbre Généalogique Pro ++</h1>
                            <p>Bienvenue dans le monde fascinant de la généalogie avec Arbre Généalogique Pro ++. Ici, vous pouvez créer, explorer et partager l'histoire unique de votre famille. Notre plateforme vous guide à travers un voyage passionnant dans votre passé, vous connectant avec vos racines et vos proches.</p>
                        </div>
                        <div className="image" >
                            <img src={img1} className="image-1"  alt="Description de l'image"/>
                        </div>
                    </div>
                </Section>

                <Section title="Section 2">
                    <div className='section-reverse'>
                        <div className="image" >
                            <img src={img2} className="image-2"  alt="Description de l'image"/>
                        </div>
                        <div className="text">
                            <h1 className='title1'>Explorez Nos Fonctionnalités</h1>
                            <p>
                                <ul>
                                    <li><strong>Création Facile d'Arbres Généalogiques :</strong> Notre outil intuitif vous permet de construire votre arbre généalogique en ajoutant des membres de la famille, leurs histoires, et des événements marquants. C'est simple, rapide et enrichissant.</li>
                                    <li><strong>Communauté et Partage :</strong>  Découvrez des liens familiaux insoupçonnés en partageant et en fusionnant votre arbre avec ceux d'autres membres. Ensemble, construisez une histoire familiale plus complète.</li>
                                    <li><strong>Échange de Souvenirs :</strong>  Partagez des photos, des lettres et des documents. Chaque image raconte une histoire, chaque document ajoute à votre récit familial.</li>
                                </ul>
                            </p>
                        </div>
                    </div>
                </Section>

                <Section title="Section 3">
                    <div className='section small-section'>
                        <div className="text">
                            <h1 className='title1'>Rejoignez-Nous</h1>
                            <p>Débutez votre aventure généalogique dès maintenant. Inscrivez-vous et connectez-vous pour découvrir et partager votre histoire familiale unique.</p>
                        </div>
                        <div className="image" >
                            <div className='connection'> 
                                {!isLoggedIn ? (
                                    <>
                                        <a href="/register">
                                            <button className="big-button">S'inscrire</button>
                                        </a>
                                        <a className="already-sub" href="/login">Je suis déjà inscrit</a>
                                    </>
                                ) : (
                                    <>
                                        <a href="/tree">
                                            <button className="big-button">Mon Arbre</button>
                                        </a>
                                    </>
                                )}
                            </div>
                        </div>
                    </div>
                </Section>

                <Section title="Section 4">
                    <div className='section-reverse'>
                        <div className="image" >
                            <img src={img4} className="image-4"  alt="Description de l'image"/>
                        </div>
                        <div className="text">
                            <h1 className='title1'>Découvrez Plus</h1>
                            <p>
                                <ul>
                                    <li><strong>Trouvez Votre Famille Éloignée :</strong> Utilisez notre plateforme pour rechercher et vous connecter avec des membres de votre famille éloignée. Vous serez surpris de voir jusqu'où s'étendent vos racines.</li>
                                    <li><strong>Nos Créateurs : </strong> Découvrez les esprits créatifs derrière Arbre Généalogique Pro ++. Une équipe dédiée à préserver et à célébrer les histoires de famille.</li>
                                </ul>
                            </p>
                        </div>
                    </div>
                </Section>

                <Section title="Section 5">
                    <div className='section'>
                        <div className="text">
                            <h1 className='title1'>Nos Statistiques et Personnalisations</h1>
                            <p>Explorez les fonctionnalités avancées pour personnaliser votre arbre et visualiser des statistiques fascinantes sur votre héritage familial. Découvrez comment des liens historiques et géographiques uniques façonnent votre histoire, et utilisez des outils interactifs pour creuser plus profondément dans vos origines. Partagez votre voyage de découverte avec la famille et les amis, en utilisant des graphiques attrayants et des chronologies personnalisables pour raconter l'histoire unique de votre lignée.</p>
                        </div>
                        <div className="image" >
                            <img src={img5} className="image-5"  alt="Description de l'image"/>
                        </div>
                    </div>
                </Section>
                
            </div>
        </div>
    );
};

export default Home;
