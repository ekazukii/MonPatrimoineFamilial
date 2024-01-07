import React, { useState, useEffect } from 'react';

const ErrorPopup = () => {
    const [showPopup, setShowPopup] = useState(false);

    const testConnection = async () => {
        console.log("begin");
        try {
            const response = await fetch('http://localhost:8080/health', {
                mode: 'no-cors' // Utiliser 'no-cors' pour les requêtes cross-origin
            });

            // Vérifiez le type de réponse
            if (response.type === 'opaque') {
                // Serveur en ligne mais réponse opaque due à 'no-cors'
                setShowPopup(false);
            }
        } catch (error) {
            // Gestion des erreurs de réseau
            setShowPopup(true);
        }
        console.log("end");
    };

    useEffect(() => {
        testConnection();
        const intervalId = setInterval(testConnection, 5000); // Vérifie toutes les 5 secondes
        return () => clearInterval(intervalId); // Nettoie l'intervalle lors du démontage
    }, []);

    // Affiche le Popup si showPopup est true
    if (!showPopup) return null;
    return (
        <div style={{ position: 'fixed', top: '20px', right: '20px', backgroundColor: 'red', padding: '10px' }}>
            Impossible de se connecter au serveur.
        </div>
    );
};

export default ErrorPopup;
