-- Migration : Ajout des stats Final Fantasy Tactics
-- Date : 28 octobre 2025

-- Ajouter les nouvelles colonnes
ALTER TABLE characters ADD COLUMN IF NOT EXISTS current_mp INTEGER;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS max_mp INTEGER;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS physical_attack INTEGER;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS magic_attack INTEGER;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS speed INTEGER;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS move INTEGER;

-- Mettre à jour les anciennes colonnes pour permettre NULL (compatibilité)
ALTER TABLE characters ALTER COLUMN strength DROP NOT NULL;
ALTER TABLE characters ALTER COLUMN agility DROP NOT NULL;
ALTER TABLE characters ALTER COLUMN intelligence DROP NOT NULL;

-- Initialiser les nouvelles stats pour les personnages existants basés sur leur classe
-- WARRIOR -> KNIGHT (HP: 120, MP: 20, PA: 70, MA: 40, Speed: 90, Move: 3)
UPDATE characters 
SET current_mp = 20, max_mp = 20, physical_attack = 70, magic_attack = 40, speed = 90, move = 3
WHERE character_class = 'WARRIOR' AND current_mp IS NULL;

-- MAGE -> BLACK_MAGE (HP: 80, MP: 45, PA: 35, MA: 80, Speed: 85, Move: 3)
UPDATE characters 
SET current_mp = 45, max_mp = 45, physical_attack = 35, magic_attack = 80, speed = 85, move = 3
WHERE character_class = 'MAGE' AND current_mp IS NULL;

-- ARCHER -> THIEF (HP: 90, MP: 35, PA: 55, MA: 45, Speed: 120, Move: 4)
UPDATE characters 
SET current_mp = 35, max_mp = 35, physical_attack = 55, magic_attack = 45, speed = 120, move = 4
WHERE character_class = 'ARCHER' AND current_mp IS NULL;

-- Rendre les nouvelles colonnes obligatoires (NOT NULL)
ALTER TABLE characters ALTER COLUMN current_mp SET NOT NULL;
ALTER TABLE characters ALTER COLUMN max_mp SET NOT NULL;
ALTER TABLE characters ALTER COLUMN physical_attack SET NOT NULL;
ALTER TABLE characters ALTER COLUMN magic_attack SET NOT NULL;
ALTER TABLE characters ALTER COLUMN speed SET NOT NULL;
ALTER TABLE characters ALTER COLUMN move SET NOT NULL;

-- Vérification
-- SELECT id, name, character_class, max_hp, max_mp, physical_attack, magic_attack, speed, move FROM characters;
