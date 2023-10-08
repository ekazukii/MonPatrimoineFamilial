import Button from 'react-bootstrap/Button';
import TreeComponent from '../components/TreeComponent';

const Home = () => {
    const fetchTestUsr = async () => {
        const data = await fetch("http://localhost:8080/userrdm");
        const json = await data.json();

        alert(json);
    }

    return (<div>
        <h1>Home</h1>
        <TreeComponent/>
        <Button onClick={fetchTestUsr}>Boostrap button example</Button>
    </div>);
};

export default Home;