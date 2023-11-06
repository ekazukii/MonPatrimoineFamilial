import {BrowserRouter, Route, Routes} from "react-router-dom";
import Layout from "./pages/Layout.jsx";
import Home from "./pages/Home.jsx";
import Log from "./pages/Log.jsx";
import NoPage from "./pages/NoPage.jsx";
import './index.css';
import Register from "./pages/Register.jsx";
import Souvenirs from "./pages/Souvenirs.jsx";
import TreePage from "./pages/TreePage.jsx";
import Account from "./pages/Account.jsx";

function App() {
  return (
    <>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path="home" element={<Home />} />
                    <Route path="login" element={<Log />} />
                    <Route path="register" element={<Register/>} />
                    <Route path="account" element={<Account user={{email: "myemail@gmail.com", username: "myUsername", firstName: "myFirstName", familyName: "myFamilyName", socialSecurityNumber: 1021011124981, birthdate: new Date()}}/>}/>
                    <Route path="souvenirs" element={<Souvenirs/>} />
                    <Route path="tree" element={<TreePage />} />
                    <Route path="*" element={<NoPage />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </>
  )
}

export default App
