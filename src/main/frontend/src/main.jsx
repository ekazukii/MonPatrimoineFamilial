import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import {SessionProvider} from "./hooks/useSession.jsx";
import {Toaster} from "react-hot-toast";

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
      <SessionProvider>
          <div><Toaster position="top-left"/></div>
          <App />
      </SessionProvider>
  </React.StrictMode>,
)
