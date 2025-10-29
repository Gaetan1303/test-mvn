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
            
            <div style="margin-bottom: 20px;">
                <h4 style="margin: 10px 0; color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 5px;">
                    ⚔️ Stats de Base FFT
                </h4>
                <div class="stat-grid">
                    <div class="stat-item">
                        <div class="stat-label">❤️ HP</div>
                        <div class="stat-value">${character.currentHp} / ${character.maxHp}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">💙 MP</div>
                        <div class="stat-value">${character.currentMp} / ${character.maxMp}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">⚔️ PA</div>
                        <div class="stat-value">${character.pa}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">� MA</div>
                        <div class="stat-value">${character.ma}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">⚡ Speed</div>
                        <div class="stat-value">${character.speed}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">🏃 Move</div>
                        <div class="stat-value">${character.move}</div>
                    </div>
                </div>
            </div>

            <div style="margin-bottom: 20px;">
                <h4 style="margin: 10px 0; color: #2c3e50; border-bottom: 2px solid #e74c3c; padding-bottom: 5px;">
                    🛡️ Défense & Précision
                </h4>
                <div class="stat-grid">
                    <div class="stat-item">
                        <div class="stat-label">🛡️ PDef</div>
                        <div class="stat-value">${character.pDef}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">✨ MDef</div>
                        <div class="stat-value">${character.mDef}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">� Hit</div>
                        <div class="stat-value">${character.hit}%</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">🌟 MagicHit</div>
                        <div class="stat-value">${character.magicHit}%</div>
                    </div>
                </div>
            </div>

            <div style="margin-bottom: 20px;">
                <h4 style="margin: 10px 0; color: #2c3e50; border-bottom: 2px solid #27ae60; padding-bottom: 5px;">
                    💨 Esquive & Critique
                </h4>
                <div class="stat-grid">
                    <div class="stat-item">
                        <div class="stat-label">💨 Evade</div>
                        <div class="stat-value">${character.evade}%</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">🌀 MagicEvade</div>
                        <div class="stat-value">${character.magicEvade}%</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">💥 CritRate</div>
                        <div class="stat-value">${character.critRate}%</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">${character.destiny >= 0 ? '😇' : '😈'} Destiny</div>
                        <div class="stat-value" style="color: ${character.destiny > 0 ? '#27ae60' : character.destiny < 0 ? '#e74c3c' : '#95a5a6'}">
                            ${character.destiny > 0 ? '+' : ''}${character.destiny}
                            ${character.destiny > 0 ? ' (Bon)' : character.destiny < 0 ? ' (Mauvais)' : ' (Neutre)'}
                        </div>
                    </div>
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
