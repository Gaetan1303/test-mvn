/**
 * Gestion de l'écran de création de personnage
 */
class CreateCharacterScreen {
    constructor() {
        this.selectedClass = null;
        this.availableClasses = [];
        this.initCreateButton();
        this.initLogoutButton();
    }

    async loadClasses() {
        const container = document.getElementById('classSelectContainer');
        container.innerHTML = '<div class="loading">Chargement des classes...</div>';
        
        // Récupérer les classes depuis le backend
        const result = await window.api.getCharacterClasses();
        
        if (result.success) {
            this.availableClasses = result.data;
            this.renderClasses();
        } else {
            container.innerHTML = '<div class="error show">Erreur de chargement des classes</div>';
            console.error('Erreur chargement classes:', result.error);
        }
    }

    renderClasses() {
        const container = document.getElementById('classSelectContainer');
        container.innerHTML = '';
        
        this.availableClasses.forEach(charClass => {
            const classDiv = document.createElement('div');
            classDiv.className = 'class-option';
            classDiv.dataset.class = charClass.name;
            
            // Catégoriser les classes pour le style visuel
            let category = 'physical';
            if (['WHITE_MAGE', 'BLACK_MAGE', 'TIME_MAGE', 'SUMMONER', 'MYSTIC'].includes(charClass.name)) {
                category = 'magic';
            } else if (['BARD', 'DANCER', 'MIME', 'CHEMIST', 'GEOMANCER'].includes(charClass.name)) {
                category = 'support';
            } else if (['DARK_KNIGHT'].includes(charClass.name)) {
                category = 'special';
            }
            classDiv.dataset.category = category;
            
            classDiv.innerHTML = `
                <div class="class-name">${charClass.emoji} ${charClass.displayName}</div>
                <div class="class-role">${charClass.role}</div>
                <div class="class-stats">
                    HP: ${charClass.baseHp} | MP: ${charClass.baseMp}
                </div>
                <div class="class-stats">
                    PA: ${charClass.basePa} | MA: ${charClass.baseMa}
                </div>
                <div class="class-stats">
                    Speed: ${charClass.baseSpeed} | Move: ${charClass.baseMove}
                </div>
            `;
            
            classDiv.addEventListener('click', () => {
                document.querySelectorAll('.class-option').forEach(o => o.classList.remove('selected'));
                classDiv.classList.add('selected');
                this.selectedClass = charClass.name;
            });
            
            container.appendChild(classDiv);
        });
    }

    initCreateButton() {
        const createBtn = document.getElementById('createCharBtn');
        const nameInput = document.getElementById('charName');
        
        createBtn.addEventListener('click', () => this.handleCreate());
        
        // Support de la touche Entrée
        nameInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.handleCreate();
        });
    }

    initLogoutButton() {
        const logoutBtn = document.getElementById('logoutFromCreateBtn');
        logoutBtn.addEventListener('click', () => {
            if (window.menuScreen) {
                window.menuScreen.logout();
            }
        });
    }

    async handleCreate() {
        const name = document.getElementById('charName').value.trim();
        
        // Validation du nom
        const nameValidation = FormValidator.validateCharacterName(name);
        if (!nameValidation.valid) {
            messageManager.showCreateCharError(nameValidation.error);
            return;
        }
        
        // Validation de la classe
        const classValidation = FormValidator.validateCharacterClass(this.selectedClass);
        if (!classValidation.valid) {
            messageManager.showCreateCharError(classValidation.error);
            return;
        }
        
        // Appel API
        const result = await window.api.createCharacter(
            appState.getToken(),
            name,
            this.selectedClass
        );
        
        if (result.success) {
            appState.setCharacter(result.data);
            messageManager.hideCreateCharError();
            
            // Afficher le personnage créé
            if (window.characterScreen) {
                await window.characterScreen.load();
            }
        } else {
            messageManager.showCreateCharError(result.error);
        }
    }

    reset() {
        document.getElementById('charName').value = '';
        document.querySelectorAll('.class-option').forEach(o => o.classList.remove('selected'));
        this.selectedClass = null;
        messageManager.hideCreateCharError();
    }
}

// Export de l'instance
const createCharacterScreen = new CreateCharacterScreen();
