# Fiche de synthèse — Architecture & Principes SOLID

## 1. Architecture globale

L’application repose sur une **architecture hybride** combinant :
- **Backend Spring Boot (Java, Maven)**  
  → Fournit l’API REST, la logique métier et la persistance des données.  
- **Frontend Electron (JavaScript, Node.js)**  
  → Fournit une interface utilisateur légère et multiplateforme, communiquant avec le backend local.  

### Schéma simplifié

```
[Electron UI] ⇄ [API REST - Spring Boot] ⇄ [Base de données / Services internes]
```

---

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
- **Node.js** pour exécuter du JavaScript côté client  
- **Electron** pour encapsuler une interface Web dans une application de bureau  
- Un **fichier `index.html`** minimal qui sert de point d’entrée graphique

Electron communique avec le backend via des appels HTTP (`fetch`, `axios`, etc.) sur `http://localhost:8080`.

---

## 4. Diagramme d’état — Gestion des transitions applicatives

Le diagramme suivant illustre la logique d’état principale de l’application (connexion, interaction sociale et combat) :

```mermaid
stateDiagram-v2
direction LR

[*] --> HORS_LIGNE : Démarrage App

state CONNECTÉ {
    direction LR
    
    [*] --> HUB
    
    state SOCIAL {
        direction TB
        [*] --> DIALOGUE
        DIALOGUE --> ECHANGE : INIT_TRADE
        ECHANGE --> DIALOGUE : END_TRADE / ABORT
    }

    state COMBAT {
        direction TB
        [*] --> ATTENTE_TOUR
        ATTENTE_TOUR --> ACTION : TOUR_COMMENCÉ
        ACTION --> ATTENTE_TOUR : ACTION_FINIE
        
        ACTION --> MORT : TAKE_DAMAGE[si HP <= 0]
    }
    
    HUB --> COMBAT : START_COMBAT
    COMBAT --> HUB : END_COMBAT
    
    HUB --> SOCIAL : START_DIALOGUE
    SOCIAL --> HUB : END_SOCIAL
    
    MORT --> HUB : RESURRECT
}

HORS_LIGNE --> CONNECTÉ : LOGIN
CONNECTÉ --> HORS_LIGNE : LOGOUT

HORS_LIGNE --> MORT : LOGIN[si compte banni]
MORT --> HORS_LIGNE : LOGOUT
```

### Objectif du diagramme
Ce **state machine** décrit la navigation logique entre les états :
- Gestion du cycle de vie d’un joueur (connexion / déconnexion / bannissement)
- Navigation entre les sous-états **HUB**, **SOCIAL**, et **COMBAT**
- Gestion des transitions internes et événements (trade, combat, mort, résurrection)

Cette modélisation soutient la **séparation des contextes métier** et facilite la **gestion des transitions asynchrones** côté serveur.

---

## 5. Principes SOLID

Les **principes SOLID** guident la conception orientée objet du backend (Spring Boot).

| Principe | Nom complet | Application dans le projet |
|-----------|-------------|-----------------------------|
| **S** | **Single Responsibility Principle**<br>(Responsabilité unique) | Chaque classe a un rôle clair (ex : un `UserService` ne gère que la logique des utilisateurs). |
| **O** | **Open/Closed Principle**<br>(Ouvert à l’extension, fermé à la modification) | Les services peuvent être étendus via de nouvelles implémentations ou stratégies, sans modifier le code existant. |
| **L** | **Liskov Substitution Principle**<br>(Substitution de Liskov) | Les implémentations peuvent remplacer leurs abstractions sans casser le comportement du code (ex : `Repository` génériques). |
| **I** | **Interface Segregation Principle**<br>(Ségrégation des interfaces) | Les interfaces définissent des contrats précis, adaptés aux besoins (évite les interfaces “fourre-tout”). |
| **D** | **Dependency Inversion Principle**<br>(Inversion des dépendances) | Le code dépend d’abstractions (interfaces, services injectés via `@Autowired`) et non de classes concrètes. |

---

## 6. Avantages de l’architecture adoptée

- **Séparation claire des responsabilités** (backend / frontend / documentation)
- **Extensibilité** : ajout de modules, services ou endpoints sans couplage excessif
- **Réutilisabilité** : composants modulaires et testables
- **Cohérence technique** entre Maven, Java 17+, Node 18+
- **Portabilité** : Electron permet un déploiement multiplateforme (Windows, macOS, Linux)

---

## 7. Bonnes pratiques associées

- Utiliser l’**encodage UTF-8** (configuré dans `pom.xml`)
- Respecter les **conventions de nommage** Java et Node
- Centraliser la **configuration** (`application.properties`, variables d’environnement)
- Éviter les **dépendances circulaires** entre couches
- Versionner uniquement le **code source et les fichiers de configuration**
- Documenter les classes clés et exposer un **diagramme UML** à jour dans `/doc`

---

**📘 Référence interne :**  
Ce document fait partie du *dossier d’architecture technique*.  
Il complète les sections *Cahier des charges* et *Diagrammes UML* dans le répertoire [`doc/`](./doc).

