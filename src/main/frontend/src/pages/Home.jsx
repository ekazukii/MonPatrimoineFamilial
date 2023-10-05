import Button from 'react-bootstrap/Button';

const Home = () => {
    const fetchTestUsr = async () => {
        const data = await fetch("http://localhost:8080/userrdm");
        const json = await data.json();

        alert(json);
    }

    return (<div>
        <h1>Home</h1>
        <Button onClick={fetchTestUsr}>Boostrap button example</Button>
    </div>);
};

export default Home;