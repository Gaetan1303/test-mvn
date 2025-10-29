-- Migration V4: Suppression des anciennes stats (strength, agility, intelligence)
-- Stats Final Fantasy Tactics uniquement : HP, MP, PA, MA, Speed, Move

-- Supprimer les anciennes colonnes deprecated
ALTER TABLE characters DROP COLUMN IF EXISTS strength;
ALTER TABLE characters DROP COLUMN IF EXISTS agility;
ALTER TABLE characters DROP COLUMN IF EXISTS intelligence;

-- Vérification que toutes les stats FFT sont présentes et NOT NULL
-- Les colonnes suivantes doivent exister : current_hp, max_hp, current_mp, max_mp, physical_attack, magic_attack, speed, move
