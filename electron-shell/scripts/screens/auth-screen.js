/**
 * Gestion de l'écran d'authentification (connexion/inscription)
 */
class AuthScreen {
    constructor() {
        this.initTabs();
        this.initLoginForm();
        this.initRegisterForm();
    }

    initTabs() {
        document.querySelectorAll('.tab').forEach(tab => {
            tab.addEventListener('click', () => {
                const tabName = tab.dataset.tab;
                
                // Mettre à jour les onglets actifs
                document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                
                // Afficher le bon formulaire
                if (tabName === 'login') {
                    document.getElementById('loginForm').style.display = 'block';
                    document.getElementById('registerForm').style.display = 'none';
                } else {
                    document.getElementById('loginForm').style.display = 'none';
                    document.getElementById('registerForm').style.display = 'block';
                }
                
                // Effacer les erreurs
                messageManager.hideAuthError();
            });
        });
    }

    initLoginForm() {
        const loginBtn = document.getElementById('loginBtn');
        const usernameInput = document.getElementById('loginUsername');
        const passwordInput = document.getElementById('loginPassword');

        loginBtn.addEventListener('click', () => this.handleLogin());
        
        // Support de la touche Entrée
        passwordInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.handleLogin();
        });
    }

    initRegisterForm() {
        const registerBtn = document.getElementById('registerBtn');
        const passwordInput = document.getElementById('registerPassword');

        registerBtn.addEventListener('click', () => this.handleRegister());
        
        // Support de la touche Entrée
        passwordInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.handleRegister();
        });
    }

    async handleLogin() {
        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value;
        
        // Validation
        const usernameValidation = FormValidator.validateUsername(username);
        if (!usernameValidation.valid) {
            messageManager.showAuthError(usernameValidation.error);
            return;
        }

        const passwordValidation = FormValidator.validatePassword(password);
        if (!passwordValidation.valid) {
            messageManager.showAuthError(passwordValidation.error);
            return;
        }
        
        // Appel API
        const result = await window.api.login(username, password);
        
        if (result.success) {
            appState.setAuth(result.data.token, username);
            messageManager.hideAuthError();
            
            // Charger le menu
            if (window.menuScreen) {
                await window.menuScreen.load();
            }
        } else {
            messageManager.showAuthError(result.error);
        }
    }

    async handleRegister() {
        const username = document.getElementById('registerUsername').value.trim();
        const email = document.getElementById('registerEmail').value.trim();
        const password = document.getElementById('registerPassword').value;
        
        // Validation
        const usernameValidation = FormValidator.validateUsername(username);
        if (!usernameValidation.valid) {
            messageManager.showAuthError(usernameValidation.error);
            return;
        }

        const emailValidation = FormValidator.validateEmail(email);
        if (!emailValidation.valid) {
            messageManager.showAuthError(emailValidation.error);
            return;
        }

        const passwordValidation = FormValidator.validatePassword(password);
        if (!passwordValidation.valid) {
            messageManager.showAuthError(passwordValidation.error);
            return;
        }
        
        // Appel API
        const result = await window.api.register(username, email, password);
        
        if (result.success) {
            appState.setAuth(result.data.token, username);
            messageManager.hideAuthError();
            
            // Charger le menu
            if (window.menuScreen) {
                await window.menuScreen.load();
            }
        } else {
            messageManager.showAuthError(result.error);
        }
    }

    reset() {
        document.getElementById('loginUsername').value = '';
        document.getElementById('loginPassword').value = '';
        document.getElementById('registerUsername').value = '';
        document.getElementById('registerEmail').value = '';
        document.getElementById('registerPassword').value = '';
        messageManager.hideAuthError();
    }
}

// Export de l'instance
const authScreen = new AuthScreen();
