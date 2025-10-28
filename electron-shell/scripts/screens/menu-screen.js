/**
 * Gestion de l'écran de menu principal
 */
class MenuScreen {
    async load() {
        screenManager.show('menu');
        
        const result = await window.api.getMenu(appState.getToken());
        
        if (result.success) {
            this.display(result.data);
        } else {
            messageManager.showAuthError('Erreur lors du chargement du menu : ' + result.error);
            this.logout();
        }
    }

    display(menuData) {
        const menuContent = document.getElementById('menuContent');
        menuContent.innerHTML = '';
        
        // Message de bienvenue
        const welcomeDiv = messageManager.createSuccessMessage(
            `Bienvenue, ${menuData.username} ! 👋`
        );
        menuContent.appendChild(welcomeDiv);
        
        // Message du serveur
        const messageP = document.createElement('p');
        messageP.style.marginBottom = '20px';
        messageP.textContent = menuData.message;
        menuContent.appendChild(messageP);
        
        // Boutons selon l'état
        if (menuData.hasCharacter) {
            // L'utilisateur a déjà un personnage
            const viewCharBtn = document.createElement('button');
            viewCharBtn.textContent = '👤 Voir mon personnage';
            viewCharBtn.addEventListener('click', () => {
                if (window.characterScreen) {
                    window.characterScreen.load();
                }
            });
            menuContent.appendChild(viewCharBtn);
        } else {
            // L'utilisateur doit créer un personnage
            const createCharBtn = document.createElement('button');
            createCharBtn.textContent = 'Créer mon personnage';
            createCharBtn.addEventListener('click', async () => {
                screenManager.show('createChar');
                // Charger les classes depuis le backend
                if (window.createCharacterScreen) {
                    await window.createCharacterScreen.loadClasses();
                }
            });
            menuContent.appendChild(createCharBtn);
        }
        
        // Bouton de déconnexion
        const logoutBtn = document.createElement('button');
        logoutBtn.className = 'secondary';
        logoutBtn.textContent = 'Déconnexion';
        logoutBtn.addEventListener('click', () => this.logout());
        menuContent.appendChild(logoutBtn);
    }

    logout() {
        appState.clearAuth();
        if (window.authScreen) {
            window.authScreen.reset();
        }
        screenManager.show('auth');
    }
}

// Export de l'instance
const menuScreen = new MenuScreen();
