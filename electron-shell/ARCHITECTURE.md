#  Diagramme d'Architecture - Electron Frontend

## Vue d'ensemble

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        ELECTRON PROCESS PRINCIPAL                        │
│                              (main.js)                                   │
│                                                                          │
│  ┌────────────────────────────────────────────────────────────────┐    │
│  │                     BrowserWindow                               │    │
│  │  - Taille: 1200x800                                            │    │
│  │  - Preload: preload.js                                         │    │
│  │  - contextIsolation: true                                      │    │
│  │  - nodeIntegration: false                                      │    │
│  └────────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────────┘
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         PRELOAD SCRIPT (preload.js)                      │
│                       Pont sécurisé via contextBridge                  │
│                                                                          │
│  window.api = {                                                         │
│    ├── register(username, email, password)                              │
│    ├── login(username, password)                                        │
│    ├── getMenu(token)                                                   │
│    ├── createCharacter(token, name, class)                              │
│    ├── getMyCharacter(token)                                            │
│    └── checkCharacterExists(token)                                      │
│  }                                                                      │
│                                                                          │
│  Backend API: http://localhost:8080                                     │
└─────────────────────────────────────────────────────────────────────────┘
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    RENDERER PROCESS (index.html)                         │
│                                                                          │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐     │
│  │   styles/        │  │   scripts/       │  │   HTML           │     │
│  │                  │  │                  │  │                  │     │
│  │  main.css        │  │  app.js          │  │  5 écrans:       │     │
│  │  forms.css       │  │  utils/          │  │  - auth          │     │
│  │  tabs.css        │  │  screens/        │  │  - menu          │     │
│  │  messages.css    │  │                  │  │  - createChar    │     │
│  │  character.css   │  │                  │  │  - character     │     │
│  │  game.css        │  │                  │  │  - game          │     │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘     │
└─────────────────────────────────────────────────────────────────────────┘
```

## Architecture des Scripts

```
scripts/app.js (Point d'entrée)
        │
        ├── Initialise et expose globalement:
        │
        ├─> utils/state.js
        │   └── AppState (Singleton)
        │       ├── token: string | null
        │       ├── username: string | null
        │       ├── character: object | null
        │       ├── setAuth(token, username)
        │       ├── setCharacter(character)
        │       ├── clearAuth()
        │       ├── isAuthenticated()
        │       └── hasCharacter()
        │
        ├─> utils/screen-manager.js
        │   └── ScreenManager (Singleton)
        │       ├── screens: { auth, menu, createChar, character, game }
        │       ├── show(screenName)
        │       └── isActive(screenName)
        │
        ├─> utils/message-manager.js
        │   └── MessageManager (Singleton)
        │       ├── showAuthError(msg)
        │       ├── hideAuthError()
        │       ├── showCreateCharError(msg)
        │       ├── hideCreateCharError()
        │       ├── createSuccessMessage(txt)
        │       ├── createInfoMessage(txt)
        │       └── createWarningMessage(txt)
        │
        ├─> utils/validators.js
        │   └── FormValidator (Classe statique)
        │       ├── validateUsername(val)
        │       ├── validateEmail(val)
        │       ├── validatePassword(val)
        │       ├── validateCharacterName(val)
        │       └── validateCharacterClass(val)
        │
        ├─> screens/auth-screen.js
        │   └── AuthScreen
        │       ├── initTabs()
        │       ├── initLoginForm()
        │       ├── initRegisterForm()
        │       ├── handleLogin()
        │       ├── handleRegister()
        │       └── reset()
        │
        ├─> screens/menu-screen.js
        │   └── MenuScreen
        │       ├── load()
        │       ├── display(menuData)
        │       └── logout()
        │
        ├─> screens/create-character-screen.js
        │   └── CreateCharacterScreen
        │       ├── initClassSelector()
        │       ├── initCreateButton()
        │       ├── initLogoutButton()
        │       ├── handleCreate()
        │       └── reset()
        │
        ├─> screens/character-screen.js
        │   └── CharacterScreen
        │       ├── initButtons()
        │       ├── load()
        │       ├── display(character)
        │       └── launchGame()
        │
        └─> screens/game-screen.js
            └── GameScreen
                ├── initButtons()
                ├── launch()
                ├── initGameCanvas()
                ├── exit()
                └── [TODO] WebSocket methods
```

## Flux de navigation utilisateur

```
┌─────────────────────────────────────────────────────────────────────┐
│                         DÉMARRAGE                                    │
│                     app.js initialise tout                           │
│                 screenManager.show('auth')                           │
└─────────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    ÉCRAN: authScreen                                 │
│                                                                      │
│  ┌──────────────┐              ┌──────────────┐                    │
│  │  Connexion   │              │ Inscription  │                    │
│  └──────────────┘              └──────────────┘                    │
│         │                              │                            │
│         │ handleLogin()                │ handleRegister()           │
│         └──────────────┬───────────────┘                            │
│                        ▼                                             │
│              window.api.login() ou                                  │
│              window.api.register()                                  │
│                        │                                             │
│                        ▼                                             │
│            appState.setAuth(token, username)                        │
│                        │                                             │
│                        ▼                                             │
│              menuScreen.load()                                      │
└─────────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    ÉCRAN: menuScreen                                 │
│                                                                      │
│              window.api.getMenu(token)                              │
│                        │                                             │
│         ┌──────────────┴──────────────┐                            │
│         ▼                              ▼                            │
│  hasCharacter = true          hasCharacter = false                 │
│         │                              │                            │
│   "Voir mon personnage"          "Créer mon personnage"            │
│         │                              │                            │
│         ▼                              ▼                            │
│  characterScreen.load()    screenManager.show('createChar')        │
└─────────────────────────────────────────────────────────────────────┘
                    │                              │
                    │                              ▼
                    │         ┌─────────────────────────────────────┐
                    │         │   ÉCRAN: createCharScreen           │
                    │         │                                     │
                    │         │  handleCreate()                     │
                    │         │         │                           │
                    │         │         ▼                           │
                    │         │  window.api.createCharacter()       │
                    │         │         │                           │
                    │         │         ▼                           │
                    │         │  appState.setCharacter()            │
                    │         │         │                           │
                    │         │         ▼                           │
                    │         │  characterScreen.load()             │
                    │         └─────────────────────────────────────┘
                    │                              │
                    └──────────────┬───────────────┘
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    ÉCRAN: characterScreen                            │
│                                                                      │
│              window.api.getMyCharacter(token)                       │
│                        │                                             │
│                        ▼                                             │
│            Affichage stats complètes                                │
│                        │                                             │
│                        ▼                                             │
│              " Lancer le jeu"                                     │
│                        │                                             │
│                        ▼                                             │
│              gameScreen.launch()                                    │
└─────────────────────────────────────────────────────────────────────┘
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    ÉCRAN: gameScreen                                 │
│                                                                      │
│              screenManager.show('game')                             │
│                        │                                             │
│                        ▼                                             │
│              initGameCanvas()                                       │
│                        │                                             │
│                        ▼                                             │
│         [TODO] WebSocket + Canvas rendering                         │
│                                                                      │
│              "Quitter le jeu"                                       │
│                        │                                             │
│                        ▼                                             │
│         screenManager.show('character')                             │
└─────────────────────────────────────────────────────────────────────┘
```

## Gestion de l'état global

```
┌─────────────────────────────────────────────────────────────────────┐
│                         AppState (Singleton)                         │
│                                                                      │
│  State: {                                                           │
│    token: null,          ← JWT (24h expiration)                    │
│    username: null,       ← Username connecté                       │
│    character: null       ← Objet Character complet                 │
│  }                                                                  │
│                                                                      │
│  Lifecycle:                                                         │
│  1. setAuth(token, username)     ← Après login/register            │
│  2. setCharacter(character)      ← Après création/récupération     │
│  3. clearAuth()                  ← Lors de la déconnexion          │
│                                                                      │
│  Partagé entre tous les écrans via: window.appState                │
└─────────────────────────────────────────────────────────────────────┘
```

## Communication avec le backend

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Backend Spring Boot                               │
│                  http://localhost:8080                               │
│                                                                      │
│  Endpoints utilisés:                                                │
│  ├── POST   /auth/register                                          │
│  ├── POST   /auth/login                                             │
│  ├── GET    /api/menu              (JWT required)                   │
│  ├── POST   /api/character/create  (JWT required)                   │
│  ├── GET    /api/character/me      (JWT required)                   │
│  └── GET    /api/character/exists  (JWT required)                   │
└─────────────────────────────────────────────────────────────────────┘
                             ▲
                             │
                    Requêtes Axios
                             │
                             │
┌─────────────────────────────────────────────────────────────────────┐
│                      preload.js                                      │
│                  contextBridge.exposeInMainWorld                     │
│                                                                      │
│  window.api.{method}()                                              │
│         │                                                            │
│         └─> axios.{get|post}(url, data, headers)                   │
│                    │                                                 │
│                    └─> Authorization: Bearer {token}                │
└─────────────────────────────────────────────────────────────────────┘
```

## Principes de conception appliqués

###  Single Responsibility Principle (SRP)
- Chaque classe a une seule responsabilité
- `AppState` → gestion d'état uniquement
- `ScreenManager` → navigation uniquement
- `FormValidator` → validation uniquement

###  Separation of Concerns (SoC)
- HTML : Structure
- CSS : Présentation
- JS : Comportement

### Don't Repeat Yourself (DRY)
- Validation centralisée dans `FormValidator`
- Messages centralisés dans `MessageManager`
- État centralisé dans `AppState`

###  Open/Closed Principle
- Facile d'ajouter un nouvel écran sans modifier l'existant
- Facile d'ajouter un nouveau style sans toucher aux autres

###  Dependency Injection
- Les écrans reçoivent les dépendances via `window.*`
- Facile à tester en mockant les dépendances

---

**Architecture conçue pour** : Scalabilité, Maintenabilité, Testabilité  
**Date** : 28 octobre 2025
