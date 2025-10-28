-- Migration V3: Permettre plusieurs personnages par utilisateur
-- Change la relation OneToOne en ManyToOne entre Character et Utilisateur
-- Supprime la contrainte UNIQUE sur utilisateur_id

-- Supprimer la contrainte d'unicité sur utilisateur_id
ALTER TABLE characters DROP CONSTRAINT IF EXISTS uk_character_utilisateur;

-- Note: Si la contrainte a un nom différent, vous pouvez la trouver avec:
-- SELECT constraint_name FROM information_schema.table_constraints 
-- WHERE table_name = 'characters' AND constraint_type = 'UNIQUE';
