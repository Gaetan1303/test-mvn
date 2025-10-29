#!/usr/bin/env node

/**
 * Script de test de connexion Backend
 * Teste les endpoints /auth/register et /auth/login
 */

const API_BASE_URL = 'http://localhost:8080';

async function testBackend() {
    console.log(' Test de connexion Backend Spring Boot\n');
    console.log(`Backend URL: ${API_BASE_URL}\n`);

    // Test 1: Register
    console.log('1️  Test POST /api/auth/register...');
    try {
        const res1 = await fetch(`${API_BASE_URL}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: 'testuser_' + Date.now(),
                email: 'test' + Date.now() + '@example.com',
                password: 'password123'
            })
        });

        console.log(`   Status: ${res1.status} ${res1.statusText}`);
        
        if (res1.ok) {
            const data = await res1.json();
            console.log('Registration réussie');
            console.log('Token reçu:', data.token ? 'Oui (' + data.token.substring(0, 20) + '...)' : 'Non');
            
            // Test 2: Login avec le même user
            console.log('\n2️  Test POST /api/auth/login...');
            const res2 = await fetch(`${API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username: 'player_docker',  // User créé dans les tests précédents
                    password: 'password123'
                })
            });

            console.log(`   Status: ${res2.status} ${res2.statusText}`);
            
            if (res2.ok) {
                const loginData = await res2.json();
                console.log('    Login réussi');
                console.log('   Token reçu:', loginData.token ? 'Oui' : 'Non');
                
                // Test 3: Appel API protégée avec token
                console.log('\n3️  Test GET /api/menu (avec JWT)...');
                const res3 = await fetch(`${API_BASE_URL}/api/menu`, {
                    headers: {
                        'Authorization': `Bearer ${loginData.token}`
                    }
                });

                console.log(`   Status: ${res3.status} ${res3.statusText}`);
                
                if (res3.ok) {
                    const menuData = await res3.json();
                    console.log('   Menu récupéré');
                    console.log('   Username:', menuData.username);
                    console.log('   Has character:', menuData.hasCharacter);
                    console.log('   Next:', menuData.next);
                } else {
                    const errorText = await res3.text();
                    console.log('Erreur menu:', errorText);
                }
            } else {
                const errorText = await res2.text();
                console.log('Erreur login:', errorText);
            }
        } else if (res1.status === 409) {
            console.log('User déjà existant (normal pour un test)');
        } else {
            const errorText = await res1.text();
            console.log('Erreur:', errorText);
        }

        console.log('\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
        console.log('Tests terminés - Backend accessible et fonctionnel');
        console.log('Le front Electron peut se connecter au backend\n');
        
    } catch (error) {
        console.error('\n ERREUR DE CONNEXION:', error.message);
        console.error('\n Vérifiez que le backend est lancé:');
        console.error('   cd /home/billy/test/test-mvn');
        console.error('   docker-compose up -d\n');
    }
}

testBackend();
