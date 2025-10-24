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

Temps TT : 8 semaines (prévision) - 12 semaines (sureté)

### C.1. Module Authentification & Persistance (Phase 0)

Temps : 1 semaine

| ID | Exigence Fonctionnelle | Description | Temps |
| :--- | :--- | :--- |-|
| **F-AUTH-01** | **Gestion des Comptes** | Permettre aux utilisateurs de s'inscrire et de se connecter (**Spring Security**). |2 jours|
| **F-PERS-01** | **Création des classes de Personnage** | Sauvegarder les données des `Utilisateur` et `Personnage` dans la base de données. |1 jour|
| **F-CHAR-01** | **Création de Personnage** | Permettre de choisir un nom et une `ClassePersonnage` au moment de la création. | 1 jour |

### C.2. Module Multijoueur & Mouvement (Phase 1)

Temps : 2 semaines

| ID | Exigence Fonctionnelle | Description | Temps |
| :--- | :--- | :--- |-|
| **F-MULTI-01** | **Connexion Temps Réel** | Établir et maintenir une connexion WebSocket bidirectionnelle avec le client. | 1 jour |
| **F-MULTI-02** | **Déplacement du personnage** | Le joueur peut effectuer des mouvements en haut, en bas à droite et à gauche sur une grille de déplacement | 5 jours |
| **F-MULTI-02** | **Gestion de la Zone** | Le serveur gère la liste des joueurs présents dans une zone de jeu donnée. | 1 jour |
| **F-MULTI-03** | **Broadcasting du Mouvement**| Le mouvement d'un joueur doit être diffusé aux autres joueurs de la même zone en temps réel. | 3 jours |

### C.3. Module Logique RPG (Phase 2)

Temps : 10 jours

| ID | Exigence Fonctionnelle | Description | Temps |
| :--- | :--- | :--- | - |
| **F-RPG-01** | **Système de Classes** | **8 classes** de personnages doivent être implémentées (stats de base et progression uniques). | 1 jour |
| **F-RPG-02** | **Inventaire & Équipement**| Les joueurs peuvent stocker des `Objet`, les équiper et les déséquiper (affectant les statistiques). | 2 jours |
| **F-RPG-03** | **Combat Simplifié** | Les joueurs et les monstres peuvent se cibler et s'attaquer. Le serveur calcule les dégâts et met à jour les HP (Serveur Autoritaire). | 5 jours |
| **F-RPG-04** | **Compétences** | Le système doit permettre l'utilisation des compétences avec gestion du coût en mana/ressource. | 2 jours |

### C.4. Module Contenu (Phase 3)


Temps : A définir (3 semaines)

| ID | Exigence Fonctionnelle | Description | Temps |
| :--- | :--- | :--- | -|
| **F-CONT-01** | **Système de Quête** | Les joueurs peuvent accepter et compléter des quêtes simples (ex: Tué X monstres). |   |
| **F-CONT-02** | **Donjons Instanciés** | Création d'instances de `Donjon` pour les groupes de joueurs (zones privées avec des monstres dédiés). |
| **F-CONT-03** | **Monstres** | Les monstres possèdent des statistiques et ont un comportement d'attaque de base (IA simple). |

---

## D. Critères de Validation

Le projet sera considéré comme réussi lorsque :

1.  Le serveur Spring Boot est démarré et capable d'accepter des connexions WebSocket sécurisées.
2.  Deux clients de test peuvent se connecter, s'authentifier et voir la position de l'autre se mettre à jour en temps réel.
3.  La logique de combat (calcul de dégâts et mise à jour des HP) est entièrement gérée côté serveur et fonctionne sans erreur critique.
4.  Un personnage est persisté (sauvegardé et rechargé) avec son inventaire et sa progression.