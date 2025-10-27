# Fiche de synthèse — Architecture & Principes SOLID

## 1. Architecture globale

L’application repose sur une **architecture hybride** combinant :
- **Backend Spring Boot (Java, Maven)**  
  → Fournit l’API REST, la logique métier et la persistance des données.  
- **Frontend Electron (JavaScript, Node.js)**  
  → Fournit une interface utilisateur légère et multiplateforme, communiquant avec le backend local.  

### Schéma simplifié

[Electron UI] ⇄ [API REST - Spring Boot] ⇄ [Base de données / Services internes]



### Organisation des répertoires

| Dossier / Fichier | Rôle principal |
|--------------------|----------------|
| `src/main/java/...` | Code source Spring Boot (contrôleurs, services, modèles) |
| `src/main/resources/` | Configuration, templates, fichiers statiques |
| `pom.xml` | Configuration Maven du backend |
| `electron-shell/` | Application Electron (interface utilisateur) |
| `electron-shell/start.sh` | Script de lancement combiné backend + UI |
| `doc/` | Documentation technique (cahier des charges, diagrammes UML, etc.) |

---

## 2. Couche Backend (Spring Boot)

Le backend suit une structure **en couches logiques** :

| Couche | Contenu | Rôle |
|--------|----------|------|
| **Controller** | Classes REST exposant les endpoints (`@RestController`) | Gère les requêtes HTTP et les réponses JSON |
| **Service** | Logique métier et orchestration des données | Centralise les traitements applicatifs |
| **Repository** | Interfaces Spring Data JPA (`extends JpaRepository`) | Communication avec la base de données |
| **Model / Entity** | Classes métiers annotées `@Entity` | Représentation des objets persistés |

Cette séparation permet :
- Une meilleure testabilité (mock des dépendances)
- Une maintenance facilitée
- Une évolution modulaire du code

---

## 3. Couche Frontend (Electron)

L’interface Electron s’appuie sur :
- **Node.js** pour exécuter du JavaScript côté client.
- **Electron** pour encapsuler une interface Web dans une application de bureau.  
- Un **fichier `index.html`** minimal qui sert de point d’entrée graphique.

Electron communique avec le backend via des appels HTTP (`fetch`, `axios`, etc.) sur `http://localhost:8080`.

---

## 4. Principes SOLID

Les **principes SOLID** guident la conception orientée objet du backend (Spring Boot).

| Principe | Nom complet | Application dans le projet |
|-----------|-------------|-----------------------------|
| **S** | **Single Responsibility Principle**<br>(Responsabilité unique) | Chaque classe a un rôle clair (ex : un `UserService` ne gère que la logique des utilisateurs). |
| **O** | **Open/Closed Principle**<br>(Ouvert à l’extension, fermé à la modification) | Les services peuvent être étendus via de nouvelles implémentations ou stratégies, sans modifier le code existant. |
| **L** | **Liskov Substitution Principle**<br>(Substitution de Liskov) | Les implémentations peuvent remplacer leurs abstractions sans casser le comportement du code (ex : `Repository` génériques). |
| **I** | **Interface Segregation Principle**<br>(Ségrégation des interfaces) | Les interfaces définissent des contrats précis, adaptés aux besoins (évite les interfaces “fourre-tout”). |
| **D** | **Dependency Inversion Principle**<br>(Inversion des dépendances) | Le code dépend d’abstractions (interfaces, services injectés via `@Autowired`) et non de classes concrètes. |

---

## 5. Avantages de l’architecture adoptée

-  **Séparation claire des responsabilités** (backend / frontend / documentation)
-  **Extensibilité** : ajout de modules, services ou endpoints sans couplage excessif
-  **Réutilisabilité** : composants modulaires et testables
-  **Cohérence technique** entre Maven, Java 17+, Node 18+
-  **Portabilité** : Electron permet un déploiement multiplateforme (Windows, macOS, Linux)

---

## 6. Bonnes pratiques associées

- Utiliser l’**encodage UTF-8** (configuré dans `pom.xml`)
- Respecter les conventions de nommage Java et Node
- Centraliser la **configuration** (`application.properties`, variables d’environnement)
- Éviter les dépendances circulaires entre couches
- Versionner uniquement le **code source et les fichiers de configuration**
- Documenter les classes clés et exposer un **diagramme UML** à jour dans `/doc`

---

**📘 Référence interne :**
Ce document fait partie du dossier d’architecture technique.  
Il complète les sections *Cahier des charges* et *Diagrammes UML* dans le répertoire [`doc/`](./doc).

---


