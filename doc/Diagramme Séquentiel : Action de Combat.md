sequenceDiagram
    participant P1 as Attaquant (Client)
    participant WS_Handler as GameWebSocketHandler
    participant CombatService as CombatService
    participant DungeonService as DungeonService
    participant P_Autres as Autres Joueurs (Clients)

    P1->>WS_Handler: Envoi message WS: /app/combat/attack (cibleId=MonstreX)

    WS_Handler->>CombatService: initialiserAttaque(PersonnageA, cibleId)

    CombatService->>DungeonService: getMonstre(cibleId)
    DungeonService-->>CombatService: MonstreX (HP actuel)

    Note over CombatService: 1. Calcul des dégâts (Attaque - Défense)<br>2. Mise à jour des HP de MonstreX

    CombatService->>DungeonService: majMonstre(MonstreX, newHP)

    alt MonstreX est vaincu
        DungeonService->>DungeonService: Supprimer MonstreX
        CombatService->>CombatService: Calculer XP et Loot
        CombatService->>P1: Diffuser notification de Gain (XP, Loot)
    end

    CombatService->>WS_Handler: préparerDiffusionResultat(MonstreX, newHP)

    WS_Handler->>P_Autres: Diffuser message WS: /topic/game/combat_sync (cibleId, newHP, statut)
    P_Autres->>P_Autres: Mettre à jour le statut du combat sur l'écran