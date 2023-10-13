import Breadcrumb from 'react-bootstrap/Breadcrumb';
import { useLocation } from 'react-router-dom';

function  CheminPages() {
    const location = useLocation();
    const currentPath = location.pathname;

    const tableauDeMots = currentPath.split("/");
    let cheminRelatif = "";

    console.log(currentPath);
    return (
        <Breadcrumb>
            <Breadcrumb.Item href="/">Accueil</Breadcrumb.Item>
            {tableauDeMots.map((mot, index) => {
                if (mot !== "") {
                    cheminRelatif = cheminRelatif + "/" + mot;
                    return (
                        <Breadcrumb.Item key={index} active={index === tableauDeMots.length - 1} href={cheminRelatif}>
                            {mot}
                        </Breadcrumb.Item>
                    );
                }
                return null;
            })}
        </Breadcrumb>
    );
}

export default CheminPages;