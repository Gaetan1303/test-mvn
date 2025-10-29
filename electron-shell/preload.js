const { contextBridge } = require('electron');

// Utiliser fetch natif pour éviter la dépendance axios dans le preload
// Node 18+ expose globalThis.fetch. Si absent, l'app devra inclure un polyfill.
const API_BASE_URL = 'http://localhost:8080';

async function callApi(path, { method = 'GET', token = null, body = null } = {}) {
    try {
        const headers = {
            'Accept': 'application/json'
        };
        if (body != null) {
            headers['Content-Type'] = 'application/json';
        }
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const res = await fetch(`${API_BASE_URL}${path}`, {
            method,
            headers,
            body: body != null ? JSON.stringify(body) : undefined
        });

        const text = await res.text();
        let json = null;
        try {
            json = text ? JSON.parse(text) : null;
        } catch (e) {
            // response is not json
            json = text;
        }

        if (res.ok) {
            return { success: true, data: json };
        } else {
            const message = (json && json.message) ? json.message : res.statusText || 'Erreur API';
            return { success: false, error: message };
        }
    } catch (err) {
        return { success: false, error: err.message || String(err) };
    }
}

// Exposition sécurisée des API au renderer process via contextBridge
contextBridge.exposeInMainWorld('api', {
    // Authentification
    register: async (username, email, password) => {
        return await callApi('/api/auth/register', { method: 'POST', body: { username, email, password } });
    },

    login: async (username, password) => {
        return await callApi('/api/auth/login', { method: 'POST', body: { username, password } });
    },

    // Menu
    getMenu: async (token) => {
        return await callApi('/api/menu', { method: 'GET', token });
    },

    // Personnage
    createCharacter: async (token, name, characterClass) => {
        return await callApi('/api/character/create', { method: 'POST', token, body: { name, characterClass } });
    },

    getMyCharacter: async (token) => {
        return await callApi('/api/character/me', { method: 'GET', token });
    },

    /**
     * Récupère tous les personnages de l'utilisateur connecté
     */
    getMyCharacters: async (token) => {
        return await callApi('/api/character/list', { method: 'GET', token });
    },

    /**
     * Récupère un personnage spécifique par son ID
     */
    getCharacterById: async (token, characterId) => {
        return await callApi(`/api/character/get/${characterId}`, { method: 'GET', token });
    },

    checkCharacterExists: async (token) => {
        return await callApi('/api/character/exists', { method: 'GET', token });
    },

    deleteCharacter: async (token) => {
        return await callApi('/api/character/delete', { method: 'DELETE', token });
    },

    /**
     * Supprime un personnage spécifique par son ID
     */
    deleteCharacterById: async (token, characterId) => {
        return await callApi(`/api/character/delete/${characterId}`, { method: 'DELETE', token });
    },

    // Récupérer les classes de personnages disponibles (endpoint public)
    getCharacterClasses: async () => {
        return await callApi('/api/character/classes', { method: 'GET' });
    }
});
