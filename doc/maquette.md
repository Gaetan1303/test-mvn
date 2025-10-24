rpg-multijoueur/
│
├── pom.xml                          # Configuration Maven
├── README.md                        # Documentation projet
├── src/
│   ├── main/
│   │   ├── java/com/example/rpg/
│   │   │   ├── RpgApplication.java  # Classe principale Spring Boot
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java        # REST : /login, /register
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── UserService.java           # Gestion utilisateur (authentification)
│   │   │   │   └── GameService.java           # (à venir : logique du jeu)
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── Utilisateur.java           # Entité JPA
│   │   │   │   └── Personnage.java            # Entité JPA
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   └── UtilisateurRepository.java # Interface JPA
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtService.java            # Génération & validation de JWT
│   │   │   │   └── SecurityConfig.java        # Configuration Spring Security
│   │   │   │
│   │   │   └── websocket/
│   │   │       ├── WebSocketConfig.java       # Configuration STOMP
│   │   │       ├── GameSessionManager.java    # Gestion des sessions connectées
│   │   │       └── GameController.java        # Endpoint STOMP (/topic/game)
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Config BD, ports, JWT, etc.
│   │       └── schema.sql / data.sql          # (optionnel) initialisation BD
│   │
│   └── test/
│       └── java/com/example/rpg/
│           └── RpgApplicationTests.java
│
└── logs/
    └── spring-boot.log
