-- Migration pour ajouter les stats complètes de Final Fantasy Tactics Advance
-- Ajout de : PDef, MDef, Hit, MagicHit, Evade, MagicEvade, CritRate, Destiny

-- D'abord, mettre à jour les personnages existants qui pourraient avoir des NULL dans les anciennes colonnes
UPDATE characters SET speed = 100 WHERE speed IS NULL;
UPDATE characters SET move = 3 WHERE move IS NULL;
UPDATE characters SET pa = 50 WHERE pa IS NULL;
UPDATE characters SET ma = 50 WHERE ma IS NULL;

-- Ajout des colonnes de défense
ALTER TABLE characters ADD COLUMN IF NOT EXISTS physical_defense INTEGER NOT NULL DEFAULT 20;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS magic_defense INTEGER NOT NULL DEFAULT 15;

-- Ajout des colonnes de précision
ALTER TABLE characters ADD COLUMN IF NOT EXISTS hit INTEGER NOT NULL DEFAULT 80;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS magic_hit INTEGER NOT NULL DEFAULT 80;

-- Ajout des colonnes d'esquive
ALTER TABLE characters ADD COLUMN IF NOT EXISTS evade INTEGER NOT NULL DEFAULT 10;
ALTER TABLE characters ADD COLUMN IF NOT EXISTS magic_evade INTEGER NOT NULL DEFAULT 10;

-- Ajout de la colonne de taux de critique
ALTER TABLE characters ADD COLUMN IF NOT EXISTS crit_rate INTEGER NOT NULL DEFAULT 5;

-- Ajout de la colonne Destin/Alignement (peut être négatif)
-- Positif = bon, Négatif = mauvais, 0 = neutre
ALTER TABLE characters ADD COLUMN IF NOT EXISTS destiny INTEGER NOT NULL DEFAULT 0;

-- Commentaires pour documenter les nouvelles colonnes
COMMENT ON COLUMN characters.physical_defense IS 'Défense physique (PDef) - réduit les dégâts physiques reçus';
COMMENT ON COLUMN characters.magic_defense IS 'Défense magique (MDef) - réduit les dégâts magiques reçus';
COMMENT ON COLUMN characters.hit IS 'Précision physique - chance de toucher avec des attaques physiques';
COMMENT ON COLUMN characters.magic_hit IS 'Précision magique - chance de toucher avec des sorts';
COMMENT ON COLUMN characters.evade IS 'Esquive physique - chance d''éviter les attaques physiques';
COMMENT ON COLUMN characters.magic_evade IS 'Esquive magique - chance d''éviter les sorts';
COMMENT ON COLUMN characters.crit_rate IS 'Taux de critique - chance d''infliger un coup critique';
COMMENT ON COLUMN characters.destiny IS 'Destin/Alignement du personnage (positif = bon, négatif = mauvais, 0 = neutre)';
