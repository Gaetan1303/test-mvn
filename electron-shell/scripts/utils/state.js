/**
 * Gestion de l'état global de l'application
 */
class AppState {
    constructor() {
        this.token = null;
        this.username = null;
        this.character = null;
    }

    setAuth(token, username) {
        this.token = token;
        this.username = username;
    }

    setCharacter(character) {
        this.character = character;
    }

    clearCharacter() {
        this.character = null;
    }

    clearAuth() {
        this.token = null;
        this.username = null;
        this.character = null;
    }

    isAuthenticated() {
        return !!this.token;
    }

    hasCharacter() {
        return !!this.character;
    }

    getToken() {
        return this.token;
    }

    getUsername() {
        return this.username;
    }

    getCharacter() {
        return this.character;
    }
}

// Export de l'instance singleton
const appState = new AppState();
