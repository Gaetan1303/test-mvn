/**
 * Gestionnaire de navigation entre écrans
 */
class ScreenManager {
    constructor() {
        this.screens = {
            auth: document.getElementById('authScreen'),
            menu: document.getElementById('menuScreen'),
            createChar: document.getElementById('createCharScreen'),
            character: document.getElementById('characterScreen'),
            game: document.getElementById('gameScreen')
        };
    }

    show(screenName) {
        // Masquer tous les écrans
        Object.values(this.screens).forEach(screen => {
            if (screen) {
                screen.classList.remove('active');
            }
        });

        // Afficher l'écran demandé
        if (this.screens[screenName]) {
            this.screens[screenName].classList.add('active');
        } else {
            console.error(`Écran "${screenName}" non trouvé`);
        }
    }

    isActive(screenName) {
        return this.screens[screenName]?.classList.contains('active') || false;
    }
}

// Export de l'instance singleton
const screenManager = new ScreenManager();
