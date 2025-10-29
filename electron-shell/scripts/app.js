/**
 * Point d'entrée principal de l'application Electron
 * Initialise tous les gestionnaires et écrans
 */

// Exposer les gestionnaires globalement pour faciliter la communication inter-écrans
window.appState = appState;
window.screenManager = screenManager;
window.messageManager = messageManager;
window.authScreen = authScreen;
window.menuScreen = menuScreen;
window.createCharacterScreen = createCharacterScreen;
window.characterScreen = characterScreen;
window.gameScreen = gameScreen;

// Initialisation au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    console.log(' Interface Electron chargée - Prêt à jouer !');
    console.log(' État de l\'application initialisé');
    console.log(' Gestionnaires d\'écrans initialisés');
    
    // Test de connexion Front-Back
    console.log(' Test de connexion Front-Back...');
    if (window.api) {
        console.log(' window.api est disponible');
        console.log('   - window.api.register:', typeof window.api.register);
        console.log('   - window.api.login:', typeof window.api.login);
        console.log('   - window.api.getMenu:', typeof window.api.getMenu);
        
        // Test rapide de connexion backend
        fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: 'test', email: 'test@test.com', password: 'test123' })
        })
        .then(res => {
            console.log(` Backend accessible - Status: ${res.status}`);
            if (res.status === 409) {
                console.log('   (User déjà existant - normal pour un test)');
            }
        })
        .catch(err => {
            console.error(' Backend inaccessible:', err.message);
        });
    } else {
        console.error(' window.api est UNDEFINED - problème preload.js');
    }
    
    // Afficher l'écran d'authentification par défaut
    screenManager.show('auth');
});

// Gestion des erreurs globales
window.addEventListener('error', (event) => {
    console.error('Erreur globale:', event.error);
    messageManager.showAuthError('Une erreur est survenue. Veuillez réessayer.');
});

// Gestion des promesses rejetées non capturées
window.addEventListener('unhandledrejection', (event) => {
    console.error('Promesse rejetée non gérée:', event.reason);
    messageManager.showAuthError('Une erreur réseau est survenue.');
});
