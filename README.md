# test — Application Spring Boot + Electron

Ce dépôt contient une application backend Spring Boot (Maven) et une interface cliente légère Electron située dans `electron-shell/`.

Ce README explique comment initialiser le projet pour GitHub, les prérequis, et comment lancer localement le backend et l'interface Electron.

## Structure principale

- `src/` : code source Java (Spring Boot)
- `pom.xml` / `mvnw` : Maven wrapper pour construire et lancer l'application
- `electron-shell/` : application Electron (UI)

## Prérequis

- Java JDK (17+ recommandé, vérifier la compatibilité de vos outils)
- Maven (ou utilisez le wrapper `./mvnw` fourni)
- Node.js (>=18) et npm (pour la partie Electron)
- Git (pour initialiser le dépôt GitHub)

Vérifiez vos versions :

```bash
java -version
./mvnw -v
node -v
npm -v
```

## Initialiser le dépôt Git et le publier sur GitHub

1. Initialiser le dépôt local (si ce n'est pas déjà le cas) :

```bash
git init
git add .
git commit -m "Initial commit: Spring Boot + Electron shell"
```

2. Ajouter le remote GitHub et pousser :

```bash
# Remplacez <user> et <repo> par vos valeurs
git remote add origin git@github.com:<user>/<repo>.git
git branch -M main
git push -u origin main
```

3. (Optionnel) Créez un `README.md` (déjà présent) et activez GitHub Actions/CI selon vos besoins.

## Démarrer le backend Spring Boot

Utilisez le wrapper Maven fourni (recommandé) :

```bash
# depuis la racine du projet
./mvnw spring-boot:run
```

Les logs apparaîtront dans le terminal; l'API devrait être disponible sur `http://localhost:8080` (par défaut).

## Démarrer l'interface Electron

1. Installer les dépendances :

```bash
cd electron-shell
npm install
```

2. Lancer Electron :

```bash
npm start    # lancement normal
npm run dev  # ouvre les devtools
```

Remarque : `electron-shell/start.sh` est un helper qui tente de démarrer Maven puis Electron. Il utilise des chemins `~/test/test` — vous pouvez l'adapter pour des chemins relatifs si nécessaire.

## Suggestions et bonnes pratiques

- Ajoutez un fichier `.gitignore` à la racine pour exclure les dossiers générés (exemples ci-dessous).
- Versionnez uniquement le code source et les fichiers de configuration, pas `node_modules/`, `target/`, `build/`, ni `.m2/`.
- Harmonisez la version Java `pom.xml`

Exemple minimal de `.gitignore` :

```
# Java
/target/
/build/
.classpath
.project
.settings/

# Maven
/.mvn/
/spring-boot.log

# Node
/electron-shell/node_modules/

# OS
.DS_Store
thumbs.db

# IDE
.idea/
/.vscode/
```

## Dépannage rapide

- Si Electron ne s'ouvre pas, vérifiez que `npm install` a bien installé `electron` et que votre Node.js est compatible.
- Si Spring Boot lève des erreurs liées à la version Java, exécutez `java -version` et alignez la propriété `java.version` dans `pom.xml`.
- Pour exécuter le projet en mode développement, ouvrez deux terminaux — un pour le backend (`./mvnw spring-boot:run`) et un pour Electron (`cd electron-shell && npm start`).

---

Si vous voulez, je peux :
- ajouter un `index.html` minimal dans `electron-shell/`;
- corriger `start.sh` pour utiliser des chemins relatifs et un encodage UTF-8 propre;
- créer un `.gitignore` réel dans le repo.

Dites-moi ce que vous souhaitez automatiser maintenant et je l'ajoute/commite.

# Documentation : 

Il y a un dossier "doc" dans le projet qui retrace la documentation technique du projet, avec : cahier des charges, diagrammes de classes et UML (Unified Modeling Language) 