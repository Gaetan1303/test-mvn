# Cahier des Charges - Projet RPG Multijoueur

## A. Identification du Projet

| Critère | Description |
| :--- | :--- |
| **Nom du Projet** | RPG Multijoueur Fantastique (Nom à définir) |
| **Objectif Principal** | Développer un jeu de rôle multijoueur avec un serveur robuste capable de synchroniser l'état du jeu et de gérer la persistance des données. |
| **Cible** | Joueurs occasionnels à réguliers aimant les RPG avec exploration, quêtes et donjons en coopération. |

---

## B. Périmètre Technique et Contraintes

| Domaine | Exigence / Choix Technologique |
| :--- | :--- |
| **Langage de Programmation** | **Java** (JDK 17 ou supérieur recommandé). |
| **Architecture Serveur** | **Spring Boot** (v3+), utilisation de l'architecture par couches (Services, Repositories). |
| **Gestion de Projet** | Maven (`pom.xml`) ou Gradle (`build.gradle`), suivi par GitHub Issues/Milestones. |
| **Base de Données** | PostgreSQL ou MySQL. Utilisation de **Spring Data JPA**. |
| **Communication Réseau** | **WebSockets** (via Spring-Websocket) pour la synchronisation en temps réel (mouvement, combat). |
| **Serveur de Jeu** | **Autoritaire** (la logique de combat s'exécute uniquement sur le serveur Spring Boot). |
| **Moteur Client (Front-end)** | À déterminer (Ex: LibGDX, Godot ou Unity). |

---

## C. Fonctionnalités (Modules)

Les fonctionnalités sont réparties selon les jalons du plan d'action.

### C.1. Module Authentification & Persistance (Phase 0)

| ID | Exigence Fonctionnelle | Description |
| :--- | :--- | :--- |
| **F-AUTH-01** | **Gestion des Comptes** | Permettre aux utilisateurs de s'inscrire et de se connecter (**Spring Security**). |
| **F-PERS-01** | **Persistance des Données** | Sauvegarder les données des `Utilisateur` et `Personnage` dans la base de données. |
| **F-CHAR-01** | **Création de Personnage** | Permettre de choisir un nom et une `ClassePersonnage` au moment de la création. |

### C.2. Module Multijoueur & Mouvement (Phase 1)

| ID | Exigence Fonctionnelle | Description |
| :--- | :--- | :--- |
| **F-MULTI-01** | **Connexion Temps Réel** | Établir et maintenir une connexion WebSocket bidirectionnelle avec le client. |
| **F-MULTI-02** | **Synchronisation du Mouvement**| Le mouvement d'un joueur doit être diffusé aux autres joueurs de la même zone en temps réel. |
| **F-MULTI-03** | **Gestion de la Zone** | Le serveur gère la liste des joueurs présents dans une zone de jeu donnée. |

### C.3. Module Logique RPG (Phase 2)

| ID | Exigence Fonctionnelle | Description |
| :--- | :--- | :--- |
| **F-RPG-01** | **Système de Classes** | **8 classes** de personnages doivent être implémentées (stats de base et progression uniques). |
| **F-RPG-02** | **Inventaire & Équipement**| Les joueurs peuvent stocker des `Objet`, les équiper et les déséquiper (affectant les statistiques). |
| **F-RPG-03** | **Combat Simplifié** | Les joueurs et les monstres peuvent se cibler et s'attaquer. Le serveur calcule les dégâts et met à jour les HP (Serveur Autoritaire). |
| **F-RPG-04** | **Compétences** | Le système doit permettre l'utilisation des compétences avec gestion du coût en mana/ressource. |

### C.4. Module Contenu (Phase 3)

| ID | Exigence Fonctionnelle | Description |
| :--- | :--- | :--- |
| **F-CONT-01** | **Système de Quête** | Les joueurs peuvent accepter et compléter des quêtes simples (ex: Tué X monstres). |
| **F-CONT-02** | **Donjons Instanciés** | Création d'instances de `Donjon` pour les groupes de joueurs (zones privées avec des monstres dédiés). |
| **F-CONT-03** | **Monstres** | Les monstres possèdent des statistiques et ont un comportement d'attaque de base (IA simple). |

---

## D. Critères de Validation

Le projet sera considéré comme réussi lorsque :

1.  Le serveur Spring Boot est démarré et capable d'accepter des connexions WebSocket sécurisées.
2.  Deux clients de test peuvent se connecter, s'authentifier et voir la position de l'autre se mettre à jour en temps réel.
3.  La logique de combat (calcul de dégâts et mise à jour des HP) est entièrement gérée côté serveur et fonctionne sans erreur critique.
4.  Un personnage est persisté (sauvegardé et rechargé) avec son inventaire et sa progression.