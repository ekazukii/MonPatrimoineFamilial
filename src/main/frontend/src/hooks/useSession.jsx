import { createContext, useContext, useEffect, useState } from 'react';

const getBaseUrl = () => 'http://localhost:8080'

export const SessionContext = createContext([undefined, () => {}]);

const refreshData = cb => {
    return fetch(getBaseUrl() + '/userinfo', {
        credentials: 'include' // to send HTTP only cookies
    }).then(data => {
        data.json().then(json => {
            cb(json);
        });
    });
};

export const SessionProvider = ({ children }) => {
    const [session, setSession] = useState({ user: null, isLoggedIn: null });

    useEffect(() => {
        refreshData(json => {
            if (json.username) setSession({ user: json, isLoggedIn: true });
            else setSession({ user: null, isLoggedIn: false });
        });
    }, []);

    return <SessionContext.Provider value={[session, setSession]}>{children}</SessionContext.Provider>;
};

export const useSession = () => {
    const [session, setSession] = useContext(SessionContext);

    console.log(session)

    const login = async (username, password) => {
        const response = await fetch(getBaseUrl() + '/login', {
            credentials: 'include',
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({username, password})
        });

        let message = '';
        if (response.ok) {
            const json = await response.json();
            if (json.username) {
                refreshData(json => {
                    if (json.user) setSession({ user: json.user, isLoggedIn: true, message: 'Connexion réussie' });
                });
                return true;
            }
        } else {
            // Gestion des messages d'erreur en fonction du code de statut HTTP
            if(response.status === 404 || response.status === 403) {
                message = 'Usernername or passoword are incorrect';
            } else {
                message = 'Connection Error';
            }
            setSession({ user: null, isLoggedIn: false, message });
            return false;
        }

        return false;
    };

    const logout = () => {
        fetch(getBaseUrl() + '/logout', {
            method: 'GET',
            credentials: 'include'
        });

        setSession({ user: null, isLoggedIn: false, message: ''});
    };

    return { user: session.user, isLoggedIn: session.isLoggedIn, message: session.message, setSession, login, refreshData, logout };
};