/**
 * Gestionnaire de messages (erreurs, succès, warnings, infos)
 */
class MessageManager {
    constructor() {
        this.authErrorElement = document.getElementById('errorMsg');
        this.createCharErrorElement = document.getElementById('createCharError');
    }

    showAuthError(message) {
        if (this.authErrorElement) {
            this.authErrorElement.textContent = message;
            this.authErrorElement.classList.add('show');
        }
    }

    hideAuthError() {
        if (this.authErrorElement) {
            this.authErrorElement.textContent = '';
            this.authErrorElement.classList.remove('show');
        }
    }

    showCreateCharError(message) {
        if (this.createCharErrorElement) {
            this.createCharErrorElement.textContent = message;
            this.createCharErrorElement.classList.add('show');
        }
    }

    hideCreateCharError() {
        if (this.createCharErrorElement) {
            this.createCharErrorElement.classList.remove('show');
        }
    }

    hideAllErrors() {
        this.hideAuthError();
        this.hideCreateCharError();
    }

    createSuccessMessage(text) {
        const div = document.createElement('div');
        div.className = 'success';
        div.textContent = text;
        return div;
    }

    createInfoMessage(text) {
        const div = document.createElement('div');
        div.className = 'info';
        div.textContent = text;
        return div;
    }

    createWarningMessage(text) {
        const div = document.createElement('div');
        div.className = 'warning';
        div.textContent = text;
        return div;
    }
}

// Export de l'instance singleton
const messageManager = new MessageManager();
