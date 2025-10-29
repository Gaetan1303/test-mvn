#!/bin/bash

# Script de vérification de l'architecture Electron

echo "🔍 Vérification de l'architecture Electron..."
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

errors=0
warnings=0

# Fonction de vérification
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 (manquant)"
        ((errors++))
        return 1
    fi
}

check_directory() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
        return 0
    else
        echo -e "${RED}✗${NC} $1/ (manquant)"
        ((errors++))
        return 1
    fi
}

echo " Fichiers principaux:"
check_file "index.html"
check_file "main.js"
check_file "preload.js"
check_file "package.json"
echo ""

echo " Documentation:"
check_file "README.md"
check_file "ARCHITECTURE.md"
check_file "MIGRATION.md"
echo ""

echo " Styles CSS:"
check_directory "styles"
check_file "styles/main.css"
check_file "styles/forms.css"
check_file "styles/tabs.css"
check_file "styles/messages.css"
check_file "styles/character.css"
check_file "styles/game.css"
echo ""

echo " Scripts JavaScript:"
check_directory "scripts"
check_file "scripts/app.js"
echo ""

echo "  Utilitaires:"
check_directory "scripts/utils"
check_file "scripts/utils/state.js"
check_file "scripts/utils/screen-manager.js"
check_file "scripts/utils/message-manager.js"
check_file "scripts/utils/validators.js"
echo ""

echo "  Écrans:"
check_directory "scripts/screens"
check_file "scripts/screens/auth-screen.js"
check_file "scripts/screens/menu-screen.js"
check_file "scripts/screens/create-character-screen.js"
check_file "scripts/screens/character-screen.js"
check_file "scripts/screens/game-screen.js"
echo ""

# Vérification du backend
echo " Backend Spring Boot:"
if curl -s http://localhost:8080/auth/register > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Backend accessible sur http://localhost:8080"
else
    echo -e "${YELLOW}⚠${NC} Backend non accessible (docker-compose up -d ?)"
    ((warnings++))
fi
echo ""

# Vérification des dépendances Node
echo " Dépendances Node.js:"
if [ -d "node_modules" ]; then
    echo -e "${GREEN}✓${NC} node_modules présent"
    
    if [ -d "node_modules/electron" ]; then
        echo -e "${GREEN}✓${NC} electron installé"
    else
        echo -e "${RED}✗${NC} electron non installé (npm install)"
        ((errors++))
    fi
    
    if [ -d "node_modules/axios" ]; then
        echo -e "${GREEN}✓${NC} axios installé"
    else
        echo -e "${RED}✗${NC} axios non installé (npm install)"
        ((errors++))
    fi
else
    echo -e "${RED}✗${NC} node_modules manquant (npm install)"
    ((errors++))
fi
echo ""

# Résumé
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
if [ $errors -eq 0 ] && [ $warnings -eq 0 ]; then
    echo -e "${GREEN} Architecture vérifiée avec succès !${NC}"
    echo ""
    echo " Pour lancer l'application:"
    echo "   npm run dev"
    exit 0
elif [ $errors -eq 0 ]; then
    echo -e "${YELLOW}  Architecture OK avec $warnings avertissement(s)${NC}"
    echo ""
    echo " Pour lancer l'application:"
    echo "   npm run dev"
    exit 0
else
    echo -e "${RED} $errors erreur(s) détectée(s)${NC}"
    if [ $warnings -gt 0 ]; then
        echo -e "${YELLOW}  $warnings avertissement(s)${NC}"
    fi
    exit 1
fi
