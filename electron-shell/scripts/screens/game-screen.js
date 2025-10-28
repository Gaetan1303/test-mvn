/**
 * Gestion de l'écran de jeu
 */
class GameScreen {
    constructor() {
        this.initButtons();
    }

    initButtons() {
        const exitBtn = document.getElementById('exitGameBtn');
        exitBtn.addEventListener('click', () => this.exit());
    }

    launch() {
        screenManager.show('game');
        console.log('🎮 Jeu lancé avec personnage:', appState.getCharacter());
        
        // TODO: Initialiser le canvas, WebSocket, etc.
        this.initGameCanvas();
    }

    initGameCanvas() {
        const canvas = document.getElementById('gameCanvas');
        const character = appState.getCharacter();
        
        if (!character) {
            canvas.innerHTML = '<div>Erreur : Aucun personnage chargé</div>';
            return;
        }

        canvas.innerHTML = `
            <div class="game-placeholder">
                <h3>🎮 Jeu lancé !</h3>
                <p><strong>${character.name}</strong> (${character.classDisplayName})</p>
                <p>Niveau ${character.level} - ${character.currentHp}/${character.maxHp} HP</p>
                <p style="margin-top: 20px; opacity: 0.7;">
                    Prochaine étape : WebSocket + Canvas pour le mouvement temps réel
                </p>
            </div>
        `;
    }

    exit() {
        if (window.characterScreen) {
            screenManager.show('character');
        }
    }

    // Méthodes pour la future implémentation WebSocket
    connectWebSocket() {
        // TODO: Connexion STOMP avec JWT
    }

    handleMovement(direction) {
        // TODO: Envoyer mouvement via WebSocket
    }

    updatePlayerPosition(x, y) {
        // TODO: Mettre à jour position sur canvas
    }
}

// Export de l'instance
const gameScreen = new GameScreen();
