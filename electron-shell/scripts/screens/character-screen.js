/**
 * Gestion de l'écran du personnage
 */
class CharacterScreen {
    constructor() {
        this.initButtons();
    }

    initButtons() {
        const launchBtn = document.getElementById('launchGameBtn');
        const logoutBtn = document.getElementById('logoutBtn');
        const deleteBtn = document.getElementById('deleteCharacterBtn');

        launchBtn.addEventListener('click', () => this.launchGame());
        logoutBtn.addEventListener('click', () => {
            if (window.menuScreen) {
                window.menuScreen.logout();
            }
        });

        if (deleteBtn) {
            deleteBtn.addEventListener('click', () => this.confirmDelete());
        }
    }

    async load() {
        const result = await window.api.getMyCharacter(appState.getToken());
        
        if (result.success) {
            appState.setCharacter(result.data);
            screenManager.show('character');
            this.display(result.data);
        } else {
            messageManager.showAuthError('Erreur lors du chargement du personnage : ' + result.error);
        }
    }

    display(character) {
        const charInfo = document.getElementById('charInfo');
        
        const stateEmoji = {
            'HUB': '🏰',
            'COMBAT': '⚔️',
            'SOCIAL': '💬',
            'MORT': '💀'
        };
        
        charInfo.innerHTML = `
            <h3>${character.name} - ${character.classDisplayName} ${stateEmoji[character.state] || ''}</h3>
            <div style="margin-bottom: 15px;">
                <strong>Niveau ${character.level}</strong> | ${character.experience} XP
            </div>
            <div class="stat-grid">
                <div class="stat-item">
                    <div class="stat-label">❤️ Points de Vie</div>
                    <div class="stat-value">${character.currentHp} / ${character.maxHp}</div>
                </div>
                <div class="stat-item">
                    <div class="stat-label">💪 Force</div>
                    <div class="stat-value">${character.strength}</div>
                </div>
                <div class="stat-item">
                    <div class="stat-label">🏃 Agilité</div>
                    <div class="stat-value">${character.agility}</div>
                </div>
                <div class="stat-item">
                    <div class="stat-label">🧠 Intelligence</div>
                    <div class="stat-value">${character.intelligence}</div>
                </div>
            </div>
            <div style="margin-top: 15px; padding: 10px; background: white; border-radius: 6px; text-align: center;">
                📍 Position: (${character.positionX.toFixed(1)}, ${character.positionY.toFixed(1)})
            </div>
        `;
    }

    launchGame() {
        if (window.gameScreen) {
            window.gameScreen.launch();
        }
    }

    confirmDelete() {
        const confirmed = confirm('⚠️ Êtes-vous sûr de vouloir supprimer votre personnage ? Cette action est irréversible !');
        if (confirmed) {
            this.deleteCharacter();
        }
    }

    async deleteCharacter() {
        const result = await window.api.deleteCharacter(appState.getToken());
        
        if (result.success) {
            alert('✅ Personnage supprimé avec succès');
            appState.clearCharacter();
            
            // Retour au menu
            if (window.menuScreen) {
                window.menuScreen.load();
            }
        } else {
            alert('❌ Erreur lors de la suppression : ' + result.error);
        }
    }
}

// Export de l'instance
const characterScreen = new CharacterScreen();
