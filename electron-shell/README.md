# 🎮 Architecture Electron - RPG Multijoueur

## 📁 Structure des fichiers

```
electron-shell/
├── index.html              # Point d'entrée HTML (structure uniquement)
├── main.js                 # Process principal Electron
├── preload.js              # Pont sécurisé entre main et renderer
├── package.json            # Dépendances et scripts
│
├── styles/                 # 🎨 Styles CSS modulaires
│   ├── main.css           # Styles principaux et layout
│   ├── forms.css          # Formulaires et inputs
│   ├── tabs.css           # Système d'onglets
│   ├── messages.css       # Messages (erreur, succès, warning, info)
│   ├── character.css      # Interface personnage et sélection classe
│   └── game.css           # Interface de jeu
│
└── scripts/                # 📜 JavaScript modulaire
    ├── app.js             # Point d'entrée principal
    │
    ├── utils/             # Utilitaires réutilisables
    │   ├── state.js               # Gestion d'état global (AppState)
    │   ├── screen-manager.js      # Navigation entre écrans
    │   ├── message-manager.js     # Gestion des messages
    │   └── validators.js          # Validation de formulaires
    │
    └── screens/           # Logique par écran
        ├── auth-screen.js             # Connexion/Inscription
        ├── menu-screen.js             # Menu principal
        ├── create-character-screen.js # Création de personnage
        ├── character-screen.js        # Affichage du personnage
        └── game-screen.js             # Écran de jeu
```

## 🏗️ Architecture

### 1. Séparation des responsabilités

#### **HTML** (`index.html`)
- Structure pure des 5 écrans
- Aucun style inline
- Aucun script inline
- Imports modulaires des CSS et JS

#### **CSS** (dossier `styles/`)
- **Modulaire** : Un fichier par domaine fonctionnel
- **Réutilisable** : Classes génériques (`.error`, `.success`, `.button`)
- **Maintenable** : Facile d'ajouter/modifier un composant

#### **JavaScript** (dossier `scripts/`)
- **Utilitaires** : Classes réutilisables (State, ScreenManager, etc.)
- **Écrans** : Chaque écran a sa propre classe
- **Point d'entrée** : `app.js` initialise tout

### 2. Flux de données

```
┌─────────────────────────────────────────────────────────┐
│                     preload.js                          │
│  (Pont sécurisé vers API backend via contextBridge)    │
└─────────────────────────────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────┐
│                   AppState (state.js)                   │
│  (État global : token, username, character)             │
└─────────────────────────────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────┐
│              ScreenManager (screen-manager.js)          │
│  (Navigation entre écrans)                              │
└─────────────────────────────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────┐
│           Écrans (auth, menu, character, game)          │
│  (Logique métier par écran)                             │
└─────────────────────────────────────────────────────────┘
```

### 3. Communication inter-écrans

Tous les gestionnaires sont exposés globalement dans `app.js` :

```javascript
window.appState           // État partagé
window.screenManager      // Navigation
window.messageManager     // Messages
window.authScreen         // Écran auth
window.menuScreen         // Écran menu
// etc.
```

Exemple d'utilisation :
```javascript
// Dans auth-screen.js, après login réussi :
await window.menuScreen.load();

// Dans menu-screen.js, pour créer un personnage :
window.screenManager.show('createChar');
```

## 🔐 Sécurité

### preload.js (contextBridge)
- ✅ `contextIsolation: true` activé
- ✅ `nodeIntegration: false` désactivé
- ✅ API exposée via `contextBridge.exposeInMainWorld('api', {...})`
- ✅ Aucun accès direct à Node.js depuis le renderer

### Gestion des tokens JWT
- Token stocké en mémoire uniquement (`AppState`)
- Pas de `localStorage` (évite les XSS)
- Token passé dans chaque requête API via headers

## 🎯 Écrans disponibles

| Écran | ID | Responsabilité |
|-------|-----|----------------|
| Authentification | `authScreen` | Login/Register avec validation |
| Menu principal | `menuScreen` | Navigation post-login |
| Création personnage | `createCharScreen` | Sélection nom + classe |
| Personnage | `characterScreen` | Affichage stats complètes |
| Jeu | `gameScreen` | Canvas de jeu (à implémenter) |

## 🧩 Classes utilitaires

### AppState
```javascript
appState.setAuth(token, username)    // Après login/register
appState.setCharacter(character)      // Après création/récupération
appState.clearAuth()                  // Déconnexion
appState.isAuthenticated()            // Vérifie si JWT présent
appState.hasCharacter()               // Vérifie si personnage créé
```

### ScreenManager
```javascript
screenManager.show('auth')           // Afficher écran auth
screenManager.show('menu')           // Afficher menu
screenManager.isActive('game')       // Vérifier écran actif
```

### MessageManager
```javascript
messageManager.showAuthError(msg)          // Erreur sur écran auth
messageManager.hideAuthError()             // Cacher erreur
messageManager.createSuccessMessage(txt)   // Créer message succès
```

### FormValidator
```javascript
FormValidator.validateUsername(value)      // Valide username (3-50 chars)
FormValidator.validateEmail(value)         // Valide email
FormValidator.validatePassword(value)      // Valide password (min 6)
FormValidator.validateCharacterName(value) // Valide nom perso (3-50)
FormValidator.validateCharacterClass(val)  // Valide classe (WARRIOR/MAGE/ARCHER)
```

## 🚀 Utilisation

### Démarrage
```bash
cd electron-shell
npm run dev      # Mode développement (avec DevTools)
npm start        # Mode normal
```

### Ajouter un nouvel écran

1. **HTML** : Ajouter une `<div id="newScreen" class="screen">` dans `index.html`
2. **CSS** : Créer `styles/new-screen.css` (optionnel)
3. **JS** : Créer `scripts/screens/new-screen.js`
4. **Référencer** : Ajouter dans `index.html` et `app.js`

Exemple :
```javascript
// scripts/screens/new-screen.js
class NewScreen {
    constructor() {
        this.init();
    }
    
    init() {
        // Initialisation
    }
    
    show() {
        screenManager.show('newScreen');
    }
}

const newScreen = new NewScreen();
```

## 🔄 Workflow utilisateur

```
1. Inscription/Connexion
   ↓
2. Menu (vérifie si personnage existe)
   ↓
3a. Création personnage (si nouveau)
   ↓
3b. Affichage personnage (si existant)
   ↓
4. Lancement du jeu
   ↓
5. Canvas + WebSocket (Phase suivante)
```

## 📦 Dépendances

- **electron** ^28.0.0 : Framework Electron
- **axios** ^1.6.2 : Requêtes HTTP vers backend

## 🎨 Conventions CSS

- **Classes utilitaires** : `.error`, `.success`, `.warning`, `.info`
- **Classes composants** : `.character-info`, `.stat-grid`, `.class-option`
- **Classes état** : `.active`, `.selected`, `.show`
- **Préfixe ID** : `#authScreen`, `#menuScreen`, etc.

## 🧪 Tests

### Tester la navigation
```javascript
// Console DevTools
screenManager.show('menu')
screenManager.show('character')
```

### Tester l'état
```javascript
// Console DevTools
appState.setAuth('fake-token', 'testuser')
appState.isAuthenticated()  // true
```

## 📝 TODO (Phase WebSocket)

- [ ] Implémenter connexion STOMP dans `game-screen.js`
- [ ] Ajouter canvas HTML5 pour rendu du jeu
- [ ] Gérer mouvement clavier (ZQSD/Flèches)
- [ ] Broadcast position via `/app/game/move`
- [ ] Subscribe à `/topic/game/updates`
- [ ] Afficher les autres joueurs

## 🤝 Contribution

Toute modification doit respecter :
1. **Modularité** : Un fichier = Une responsabilité
2. **Séparation** : HTML/CSS/JS séparés
3. **Documentation** : Commenter les classes/méthodes
4. **Conventions** : Suivre le style existant

---

**Auteur** : Équipe RPG Multijoueur  
**Version** : 1.0.0  
**Dernière mise à jour** : 28 octobre 2025
