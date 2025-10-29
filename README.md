# RPG Multijoueur FFT - Application Spring Boot + Electron

Application de jeu de rôle multijoueur inspirée de Final Fantasy Tactics, avec backend Spring Boot et interface Electron.

## Architecture

- **Backend** : Spring Boot 3.5.6 + PostgreSQL + WebSocket STOMP + Mercure SSE
- **Frontend** : Electron 28.0.0 avec architecture modulaire
- **Infrastructure** : Docker Compose (app, db, caddy, mercure)
- **Système de jeu** : 18 classes FFT, 14 statistiques FFTA, mouvement temps réel

## Structure du projet

```
test-mvn/
├── src/main/java/          # Backend Spring Boot
│   ├── controller/         # REST + WebSocket controllers
│   ├── service/            # Logique métier (Game, Auth, Mercure)
│   ├── model/              # Entités JPA (Character, Utilisateur)
│   ├── security/           # JWT + Spring Security
│   └── websocket/          # Config STOMP + GameSessionManager
├── electron-shell/         # Frontend Electron
│   ├── scripts/            # Logique UI (screens, utils)
│   ├── styles/             # CSS modulaire
│   └── index.html          # Point d'entrée
├── doc technique/          # Documentation complète
│   ├── ARCHITECTURE.md
│   ├── PHASE1_WEBSOCKET_IMPLEMENTATION.md
│   └── Diagrammes UML
├── compose.yaml            # Docker Compose
├── dockerfile              # Build multi-stage
└── pom.xml                 # Maven
```

## Prérequis

- **Java 21** (OpenJDK ou Eclipse Temurin)
- **Maven 3.9+** (ou utilisez `./mvnw`)
- **Docker** et **Docker Compose**
- **Node.js 18+** et **npm** (pour Electron)
- **Git**

Vérification :
```bash
java -version          # doit afficher Java 21
docker --version
docker-compose --version
node -v                # >= 18.x
npm -v
```

## Installation initiale

### 1. Cloner le projet

```bash
git clone https://github.com/Gaetan1303/test-mvn.git
cd test-mvn
```

### 2. Créer le fichier .env (OBLIGATOIRE)

Le fichier `.env` contient les variables d'environnement pour Docker :

```bash
cat > .env << 'EOF'
# Base de données PostgreSQL
DB_NAME=rpgdb
DB_USER=rpguser
DB_PASS=rpgpassword


# Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=update
EOF
```

**IMPORTANT** : En production, changez ces secrets !

### 3. Installer les dépendances Electron

```bash
cd electron-shell
npm install
cd ..
```

## Lancement du projet

### Option 1 : Lancement complet avec Docker (RECOMMANDÉ)

```bash
# Depuis la racine du projet
export $(cat .env | grep -v '^#' | xargs)
docker-compose up --build -d
```

Services lancés :
- **Backend Spring Boot** : http://localhost:8080
- **PostgreSQL** : localhost:5433
- **Mercure Hub** : http://localhost:8081
- **Caddy (Reverse Proxy)** : http://localhost:3000

Vérification :
```bash
docker ps                                    # Tous les conteneurs UP
curl http://localhost:8080/health            # {"status":"UP"}
curl http://localhost:8080/api/character/classes | jq length  # 18
```

### Option 2 : Lancement manuel (développement)

**Terminal 1 - Backend** :
```bash
export $(cat .env | grep -v '^#' | xargs)
./mvnw spring-boot:run
```

**Terminal 2 - Electron** :
```bash
cd electron-shell
npm start              # Mode normal
# ou
npm run dev            # Avec DevTools
```

## Arrêt propre

```bash
docker-compose down                # Arrête les conteneurs
docker-compose down -v             # + supprime les volumes (DB)
```

## Endpoints principaux

### REST API

| Endpoint | Méthode | Auth | Description |
|----------|---------|------|-------------|
| `/` | GET | Public | Page d'accueil API |
| `/health` | GET | Public | Health check |
| `/api/auth/register` | POST | Public | Inscription |
| `/api/auth/login` | POST | Public | Connexion (retourne JWT) |
| `/api/character/classes` | GET | Public | Liste des 18 classes FFT |
| `/api/character/create` | POST | JWT | Créer un personnage |
| `/api/character/list` | GET | JWT | Liste des personnages |
| `/api/character/me` | GET | JWT | Personnage actif |

### WebSocket STOMP

**Connexion** : `ws://localhost:8080/ws` (ou `/ws-sockjs` avec SockJS)

**Headers requis** :
```javascript
{
  "Authorization": "Bearer <JWT_TOKEN>"
}
```

**Destinations client → serveur** :
- `/app/game/connect` - Connecter un personnage
- `/app/game/move` - Déplacer (validé côté serveur)
- `/app/chat/send` - Envoyer un message chat

**Souscriptions serveur → client** :
- `/topic/game/position` - Positions de tous les joueurs (broadcast)
- `/topic/game/disconnect` - Notifications de déconnexion
- `/user/queue/errors` - Erreurs privées

### Mercure SSE

**Endpoint** : `http://localhost:8081/.well-known/mercure?topic=chat/global`

Écoute des messages chat en temps réel via Server-Sent Events.

## Tests

Le projet a été testé avec succès en charge :
- **75 joueurs simultanés** (50 en mouvement + 25 en chat)
- **100% taux de succès**, 0 erreurs
- **75,196 messages** broadcast
- **Latence moyenne : 0.16ms**
- Infrastructure validée comme TRÈS STABLE

Voir `doc technique/PHASE1_WEBSOCKET_IMPLEMENTATION.md` pour les détails.

## Variables d'environnement

| Variable | Valeur par défaut | Description |
|----------|-------------------|-------------|
| `DB_NAME` | rpgdb | Nom base PostgreSQL |
| `DB_USER` | rpguser | Utilisateur PostgreSQL |
| `DB_PASS` | rpgpassword | Mot de passe PostgreSQL |
| `JWT_SECRET` | (voir .env) | Secret JWT (256 bits min) |
| `MERCURE_JWT_SECRET` | (voir .env) | Secret Mercure |

## Dépannage

### Backend ne démarre pas

1. Vérifier que le fichier `.env` existe et est correctement chargé :
```bash
export $(cat .env | grep -v '^#' | xargs)
docker-compose config  # Affiche la config avec variables
```

2. Logs Docker :
```bash
docker logs test-mvn_app_1 --tail 100
```

3. Erreur PostgreSQL "no password provided" :
```bash
# Re-créer les conteneurs
docker-compose down
export $(cat .env | grep -v '^#' | xargs)
docker-compose up --build -d
```

### Electron ne se lance pas

```bash
cd electron-shell
rm -rf node_modules package-lock.json
npm install
npm start
```

### Endpoint /api/character/classes retourne vide

Le backend n'a pas démarré correctement. Vérifier :
```bash
curl http://localhost:8080/health
docker logs test-mvn_app_1 | grep ERROR
```

### WebSocket refuse la connexion

1. Vérifier que le JWT est valide :
```bash
# Obtenir un token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}' | jq -r '.token')

# Tester un endpoint protégé
curl http://localhost:8080/api/character/list \
  -H "Authorization: Bearer $TOKEN"
```

2. Le endpoint `/ws` doit être accessible :
```bash
curl -i http://localhost:8080/ws
# Devrait retourner un Upgrade WebSocket ou 400 (normal en HTTP)
```

## Documentation technique

Consultez le dossier `doc technique/` :
- **ARCHITECTURE.md** : Diagrammes complets (Electron, Backend, Flow)
- **PHASE1_WEBSOCKET_IMPLEMENTATION.md** : Implémentation WebSocket détaillée
- **Diagrammes UML** : Classes, séquences, états

## Technologies utilisées

### Backend
- Spring Boot 3.5.6
- Spring Security + JWT
- Spring WebSocket (STOMP)
- PostgreSQL 15
- Hibernate/JPA
- Mercure Hub (SSE)

### Frontend
- Electron 28.0.0
- Architecture MVC modulaire
- Axios (HTTP)
- Pattern Singleton pour l'état

### Infrastructure
- Docker + Docker Compose
- Caddy (reverse proxy)
- Multi-stage Dockerfile

## Licence

Projet éducatif - RPG FFT Multiplayer 