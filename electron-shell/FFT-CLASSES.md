# 🎮 Classes Final Fantasy Tactics - Guide Complet

## 📊 18 Classes Implémentées

### 🗡️ Jobs de Combat et de Mêlée (7 classes)

| Emoji | Classe | Nom français | Rôle | HP | MP | PA | MA | Speed | Move |
|-------|--------|--------------|------|----|----|----|----|-------|------|
| 🎓 | SQUIRE | Écolier | DPS/Support de base | 100 | 30 | 50 | 40 | 100 | 3 |
| 🛡️ | KNIGHT | Chevalier | Tank/Contrôle Mêlée | 120 | 20 | 70 | 40 | 90 | 3 |
| 🥋 | MONK | Moine | DPS Mêlée/Soin | 115 | 25 | 75 | 45 | 95 | 3 |
| 🗡️ | THIEF | Voleur | Vitesse/Utility | 90 | 35 | 55 | 45 | 120 | 4 |
| 🐉 | DRAGOON | Chevalier Dragon | DPS Mêlée/Saut | 110 | 25 | 65 | 40 | 85 | 3 |
| ⚔️ | SAMURAI | Samouraï | DPS Mêlée Épique | 105 | 30 | 80 | 50 | 95 | 3 |
| 🥷 | NINJA | Ninja | DPS/Speed Ultime | 95 | 35 | 70 | 45 | 130 | 4 |

**Points forts** :
- **Tank** : Knight (HP max, PA élevé)
- **DPS physique** : Samurai (PA le plus élevé : 80)
- **Speed** : Ninja (Speed max : 130), Thief (120)
- **Mouvement** : Ninja et Thief (Move 4)

---

### ✨ Jobs de Magie et de Support (7 classes)

| Emoji | Classe | Nom français | Rôle | HP | MP | PA | MA | Speed | Move |
|-------|--------|--------------|------|----|----|----|----|-------|------|
| ⚗️ | CHEMIST | Chimiste | Soin/Support | 95 | 40 | 45 | 50 | 100 | 3 |
| ✨ | WHITE_MAGE | Mage Blanc | Soigneur/Buffer | 85 | 50 | 40 | 70 | 90 | 3 |
| 🔮 | BLACK_MAGE | Mage Noir | DPS Magique | 80 | 45 | 35 | 80 | 85 | 3 |
| ⏰ | TIME_MAGE | Mage Temporel | Contrôle du Temps | 85 | 45 | 40 | 65 | 95 | 3 |
| 🌟 | SUMMONER | Invocateur | DPS Magique de Zone | 80 | 60 | 35 | 75 | 80 | 3 |
| 🔯 | MYSTIC | Mystique | Débuff/Contrôle | 90 | 50 | 40 | 60 | 90 | 3 |
| 🌍 | GEOMANCER | Géomancien | DPS Polyvalent | 100 | 40 | 55 | 55 | 100 | 4 |

**Points forts** :
- **DPS magique** : Black Mage (MA max : 80)
- **Soin** : White Mage (MA 70, MP 50)
- **Invocations** : Summoner (MP max : 60)
- **Polyvalent** : Geomancer (équilibré PA/MA, Move 4)

---

### 🌟 Jobs Spéciaux (4 classes)

| Emoji | Classe | Nom français | Rôle | HP | MP | PA | MA | Speed | Move |
|-------|--------|--------------|------|----|----|----|----|-------|------|
| 🎵 | BARD | Barde | Support (Buffs) | 85 | 30 | 45 | 50 | 75 | 3 |
| 💃 | DANCER | Danseuse | Support (Débuffs) | 85 | 30 | 50 | 55 | 75 | 3 |
| 🎭 | MIME | Mime | Duplicateur | 100 | 40 | 60 | 60 | 110 | 3 |
| 🗡️ | DARK_KNIGHT | Chevalier Noir | DPS/Soin Sombre | 115 | 30 | 75 | 50 | 90 | 3 |

**Points forts** :
- **Support unique** : Bard (buffs), Dancer (débuffs)
- **Polyvalent** : Mime (stats équilibrées, Speed 110)
- **DPS/Tank hybride** : Dark Knight (HP 115, PA 75)

---

## 📈 Statistiques Expliquées

| Stat | Nom complet | Description | Utilisation |
|------|-------------|-------------|-------------|
| **HP** | Hit Points | Points de vie | Survie, résistance aux dégâts physiques |
| **MP** | Magic Points | Points de magie | Utilisation de sorts et compétences |
| **PA** | Physical Attack | Attaque physique | Dégâts des attaques de mêlée et armes |
| **MA** | Magic Attack | Attaque magique | Puissance des sorts et soins |
| **Speed** | Vitesse | Rapidité d'action | Ordre des tours, initiative |
| **Move** | Mouvement | Déplacement | Cases parcourues par tour sur la grille |

---

## 🎨 Interface Electron

### Catégorisation visuelle

Les classes sont colorées par catégorie :

- 🔴 **Rouge (Physical)** : Squire, Knight, Monk, Thief, Dragoon, Samurai, Ninja
- 🔵 **Bleu (Magic)** : White Mage, Black Mage, Time Mage, Summoner, Mystic
- 🟢 **Vert (Support)** : Chemist, Geomancer, Bard, Dancer, Mime
- 🟣 **Violet (Special)** : Dark Knight

### Affichage des stats

Chaque carte de classe affiche :
```
🎓 Écolier
DPS/Support de base
HP: 100 | MP: 30
PA: 50 | MA: 40
Speed: 100 | Move: 3
```

### Grille responsive

- **4 colonnes** pour afficher toutes les classes
- **Scroll automatique** si plus de 5 lignes
- **Hover effects** avec élévation de la carte
- **Sélection** avec changement de couleur

---

## 🔧 Utilisation

### Créer un personnage

1. Lancer Electron : `cd electron-shell && npm start`
2. Se connecter ou s'inscrire
3. Cliquer sur "Créer mon personnage"
4. **Choisir parmi 18 classes**
5. Entrer un nom
6. Valider

### API Backend

```bash
# Récupérer toutes les classes
curl http://localhost:8080/api/character/classes | jq '.'

# Créer un personnage Ninja
curl -X POST http://localhost:8080/api/character/create \
  -H "Authorization: Bearer <JWT>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Hattori","characterClass":"NINJA"}'
```

---

## 🎯 Recommandations par style de jeu

### 🗡️ Vous aimez le combat rapproché ?
- **Débutant** : Knight (tank solide)
- **Intermédiaire** : Monk (DPS + auto-soin)
- **Expert** : Ninja (vitesse ultime)

### ✨ Vous préférez la magie ?
- **Débutant** : White Mage (soin)
- **Intermédiaire** : Black Mage (DPS magique)
- **Expert** : Summoner (invocations de zone)

### 🎵 Vous voulez supporter l'équipe ?
- **Débutant** : Chemist (objets)
- **Intermédiaire** : Bard/Dancer (buffs/débuffs)
- **Expert** : Time Mage (contrôle du temps)

### ⚔️ Vous cherchez l'équilibre ?
- **Polyvalent** : Geomancer (PA/MA équilibrés)
- **Hybride** : Dark Knight (combat + magie)
- **Unique** : Mime (copie d'actions)

---

## 📊 Classements

### 🏆 Top 5 HP (Survie)
1. Knight : 120
2. Monk : 115
3. Dark Knight : 115
4. Dragoon : 110
5. Samurai : 105

### ⚡ Top 5 Speed (Rapidité)
1. Ninja : 130
2. Thief : 120
3. Mime : 110
4. Squire : 100
5. Geomancer/Chemist : 100

### 💪 Top 5 PA (DPS physique)
1. Samurai : 80
2. Monk : 75
3. Dark Knight : 75
4. Ninja : 70
5. Knight : 70

### 🔮 Top 5 MA (DPS magique)
1. Black Mage : 80
2. Summoner : 75
3. White Mage : 70
4. Time Mage : 65
5. Mystic : 60

### 🌊 Top 3 MP (Endurance magique)
1. Summoner : 60
2. White Mage/Mystic : 50
3. Black Mage/Time Mage : 45

### 🏃 Classes avec Move 4
- Ninja
- Thief
- Geomancer

---

## 🚀 Évolutions futures possibles

### Classes non implémentées (FFT)
- **Orator/Mediator** : Manipulation et recrutement
- **Calculator/Arithméticien** : Magie calculée
- **Onion Knight** : Classe ultime end-game

### Système de progression
- Gain d'XP et de JP (Job Points)
- Unlock de compétences par niveau
- Changement de classe (Job System)
- Compétences secondaires équipables

### Combat
- Calcul des dégâts basé sur PA/MA
- Système de tours basé sur Speed
- Mouvement sur grille tactique (Move)
- Compétences spécifiques par classe

---

**Créé le** : 28 octobre 2025  
**Version** : 1.0  
**Backend** : Spring Boot 3.5.6 + PostgreSQL  
**Frontend** : Electron 28 + JavaScript modulaire
