# 📄 Briefing Lore & Worldbuilding
**Destinataire :** Rédacteur/Rédactrice du Lore et du Monde
**Objet :** Cadre narratif et technique du RPG Multijoueur

Ce document sert à établir les fondations narratives du projet en garantissant une cohérence avec l'architecture et les fonctionnalités du jeu, telles que définies dans la documentation technique (diagrammes UML, cahier des charges).

---

## 1. Contexte du Projet et Public Cible

| Critère | Détails clés | Implication pour le Lore |
| :--- | :--- | :--- |
| **Genre du Projet** | RPG Multijoueur (MMORPG ou Coopératif) de type **Fantastique**. | Le monde doit être riche en magie, races, créatures et civilisations. |
| **Objectif de Gameplay** | Exploration, Quêtes, Donjons en Coopération. | L'histoire doit motiver les joueurs à voyager, interagir en groupe et pénétrer des zones dangereuses (`Donjon`s). |
| **Contrainte Technique** | Le Serveur de Jeu est **Autoritaire**. | L'univers doit justifier la prépondérance des statistiques, des niveaux et des règles de combat strictes (un système de jeu "mécanique" géré par le serveur). |
| **Public Cible** | Joueurs occasionnels à réguliers. | Le Lore doit être engageant et suffisamment accessible sans être trop dense au départ. |

---

## 2. Entités de Jeu à Intégrer au Lore

L'univers et l'histoire doivent fournir un contexte crédible aux classes et entités suivantes du modèle de jeu :

### A. Personnages et Progression

| Entité de Jeu | Définition Technique | Question de Lore à Adresser |
| :--- | :--- | :--- |
| **Classes de Personnages** | Guerrier, Mage, Prêtre, etc. (cf. `ClassePersonnage` dans `Diagramme de Classe.md`). | Quelles sont les origines de ces classes ? Sont-elles issues d'écoles, de cultes, de lignées ? Comment le monde perçoit-il la Magie et la Foi ? |
| **HP / Mana (MP)** | Points de Vie et Points de Magie/Ressource (cf. `Personnage`). | Quelle est la nature de cette force vitale (`HP`) et de la ressource magique (`MP`) ? Comment la régénération est-elle justifiée (Repos, Prière, Alchimie) ? |
| **Compétences** | Capacités spécifiques avec un coût en `Mana` (cf. `Competence`). | Comment les compétences sont-elles apprises ? Sont-ce des sorts, des techniques secrètes, ou des dons innés ? |
| **Niveaux (XP)** | Progression du `Personnage` par accumulation de points d'expérience (XP). | Quel est le concept narratif derrière le gain de `Niveau` ? (Ex : L'éveil de pouvoirs, la maîtrise d'une discipline, le développement spirituel). |

### B. Conflits et Environnement

| Entité de Jeu | Définition Technique | Question de Lore à Adresser |
| :--- | :--- | :--- |
| **Monstres** | Entités avec HP, Dégâts, et un `LootTable` (cf. `Monstre`). | Quelle est l'écologie des monstres ? Pourquoi sont-ils hostiles ? Quel lien y a-t-il entre leur nature et le `Loot` (Objets/XP) qu'ils laissent derrière eux ? |
| **Donjons Instanciés** | Zones privées créées pour un groupe (`Donjon` - cf. `Diagramme de Classe.md`). | Pourquoi ces zones existent-elles ? Comment justifier le fait qu'elles soient privées/isolées (Ex : Dimensions parallèles, Failles, Sanctuaires scellés) ? |
| **Objets / Loot** | Équipement (`Arme`, `Armure`) ou consommable (`Potion`) (cf. `Objet`). | Quel est le rôle de l'artisanat/commerce ? Donner un contexte historique ou mythologique aux équipements et aux matériaux rares. |

---

## 3. Directives de Worldbuilding

La structure narrative doit soutenir le gameplay asynchrone et synchrone du jeu.

1.  **Le Monde Avant l'Histoire :**
    * **Géographie :** Définir une carte, les grandes régions, les capitales.
    * **Sociétés :** Décrire les principales Factions, Alliances et Rivalités (politiques, religieuses ou ancestrales).
    * **Événement Moteur :** Quel événement majeur (guerre, cataclysme, prophétie) justifie l'état actuel du monde et pousse les joueurs à l'aventure ?

2.  **Raison d'Être du Joueur :**
    * Le `Personnage` joueur est un aventurier, un héros potentiel, mais initialement un inconnu.
    * Le scénario doit justifier la répétition d'actions de base (tuer X monstres, livrer un objet) comme faisant partie d'une quête plus vaste.

3.  **Narration Discrète (Lore par l'environnement) :**
    * Éviter les murs de texte ou les cinématiques trop longues.
    * Le Lore doit être découvert via :
        * **Narration Environnementale :** Architecture, ruines, marques de bataille, etc.
        * **Dialogues de PNJ :** Courts, engageants, fournissant des indices.
        * **Descriptions d'Objets :** Les objets rares ou légendaires ont des "fiches d'histoire" courtes.