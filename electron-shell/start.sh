#!/bin/bash

echo "🚀 Démarrage de l'application..."

# Démarrer Spring Boot en arrière-plan
echo "�� Démarrage du backend Spring Boot..."
cd ~/test/test
./mvnw spring-boot:run > spring-boot.log 2>&1 &
SPRING_PID=$!

# Attendre que Spring Boot démarre
echo "⏳ Attente du démarrage du serveur..."
sleep 10

# Démarrer Electron
echo "🖥️  Démarrage de l'interface Electron..."
cd ~/test/test/electron-shell
npm start

# Quand Electron se ferme, arrêter Spring Boot
kill $SPRING_PID
echo "✅ Application arrêtée"
