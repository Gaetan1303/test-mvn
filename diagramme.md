# Documentation du Projet RPG Multijoueur

Ce document présente le diagramme de classes des entités principales du jeu, suivi d'un plan d'action pour le développement du serveur multijoueur sous Spring Boot.

---

## 1. Diagramme de Classes (Orienté Jeu/Domaine)

Ce modèle se concentre sur les entités de jeu qui seront gérées et persistées par le serveur Spring Boot (couche "Modèle").

### Aperçu des Entités

| Nom de la classe | Rôle | Type (dans Spring Boot) |
| :--- | :--- | :--- |
| **Utilisateur** | Compte d'authentification du joueur. | `@Entity` |
| **Personnage** | L'avatar jouable (stats, position, inventaire). | `@Entity` |
| **ClassePersonnage** | Définit les archétypes (Guerrier, Mage, etc.). | `@Entity` ou Enum |
| **Competence** | Les capacités des personnages. | `@Entity` |
| **Objet** | Équipement ou consommable. | `@Entity` |
| **Donjon** | Instance de zone multijoueur. | `@Component` (Logique/Session) |
| **Monstre** | Les ennemis à combattre. | `@Entity` |

### Diagramme UML (Mermaid)

```mermaid
classDiagram
    direction LR

    class Utilisateur {
        - id: Long
        - username: String
        - password: String
        + getPersonnages()
    }

    class Personnage {
        - id: Long
        - nom: String
        - niveau: int
        - hp: int
        - mp: int
        - positionX: int
        - positionY: int
        - classe: ClassePersonnage
        - inventaire: List<Objet>
        + attaquer(cible)
        + utiliserCompetence(comp)
    }

    class ClassePersonnage {
        - id: Long
        - nom: String (Guerrier, Mage, Prêtre, etc.)
        - statsBase: Map<String, Integer>
        - competencesBase: List<Competence>
    }

    class Competence {
        - id: Long
        - nom: String
        - coutMana: int
        - type: String (Dégât, Soin, Buff)
        + executer(lanceur, cible)
    }

    class Inventaire {
        - id: Long
        - capacite: int
        - objets: Map<Objet, Integer>
        + ajouterObjet(objet, quantite)
    }

    class Objet {
        - id: Long
        - nom: String
        - type: String (Arme, Armure, Potion)
        - valeur: Map<String, Object> (dégâts, défense, soin)
    }

    class Monstre {
        - id: Long
        - nom: String
        - hp: int
        - degats: int
        + agir(personnages)
    }

    class Donjon {
        - id: Long
        - nom: String
        - instanceID: String
        - joueurs: List<Personnage>
        - monstres: List<Monstre>
        + demarrerCombat()
        + terminerDonjon()
    }

    Utilisateur "1" -- "0..*" Personnage : possède >
    Personnage "1" -- "1" ClassePersonnage : est de >
    Personnage "1" -- "1" Inventaire : a >
    Personnage "1" -- "0..*" Competence : peut utiliser >
    Inventaire "1" -- "0..*" Objet : contient >
    Donjon "1" -- "0..*" Personnage : contient >
    Donjon "1" -- "0..*" Monstre : contient >