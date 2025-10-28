/**
 * Utilitaires de validation de formulaires
 */
class FormValidator {
    static validateUsername(username) {
        if (!username || username.length < 3) {
            return { valid: false, error: 'Le nom d\'utilisateur doit contenir au moins 3 caractères' };
        }
        if (username.length > 50) {
            return { valid: false, error: 'Le nom d\'utilisateur ne peut pas dépasser 50 caractères' };
        }
        return { valid: true };
    }

    static validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email) {
            return { valid: false, error: 'L\'email est requis' };
        }
        if (!emailRegex.test(email)) {
            return { valid: false, error: 'L\'email n\'est pas valide' };
        }
        return { valid: true };
    }

    static validatePassword(password) {
        if (!password || password.length < 6) {
            return { valid: false, error: 'Le mot de passe doit contenir au moins 6 caractères' };
        }
        return { valid: true };
    }

    static validateCharacterName(name) {
        if (!name || name.length < 3) {
            return { valid: false, error: 'Le nom du personnage doit contenir au moins 3 caractères' };
        }
        if (name.length > 50) {
            return { valid: false, error: 'Le nom du personnage ne peut pas dépasser 50 caractères' };
        }
        return { valid: true };
    }

    // Validation de classe retirée - gérée par le backend
    // Le frontend vérifie juste qu'une classe est sélectionnée
    static validateCharacterClass(characterClass) {
        if (!characterClass) {
            return { valid: false, error: 'Veuillez sélectionner une classe' };
        }
        return { valid: true };
    }
}
