# 🚀 Guide d'Exécution - NavettePro

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

1. **Java 17 ou supérieur**
   ```bash
   java -version
   ```
   Si Java n'est pas installé : [Télécharger Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```
   Si Maven n'est pas installé : [Télécharger Maven](https://maven.apache.org/download.cgi)

3. **MySQL 8.0+**
   ```bash
   mysql --version
   ```
   Si MySQL n'est pas installé : [Télécharger MySQL](https://dev.mysql.com/downloads/mysql/)

4. **Un serveur web** (VS Code Live Server recommandé) ou Python

---

## 🔧 Étape 1 : Configuration de la Base de Données

### 1.1 Démarrer MySQL

Assurez-vous que MySQL est en cours d'exécution :
- **Windows** : Vérifiez dans les Services Windows
- **Linux/Mac** : `sudo systemctl start mysql` ou `brew services start mysql`

### 1.2 Créer la Base de Données

Ouvrez un terminal et connectez-vous à MySQL :

```bash
mysql -u root -p
```

Entrez votre mot de passe MySQL.

### 1.3 Exécuter le Script SQL

**Option A : Depuis MySQL**
```sql
source C:/Users/user/Desktop/NavettePro/database/schema.sql
```

**Option B : Depuis le Terminal**
```bash
mysql -u root -p < database/schema.sql
```

Ou si vous êtes dans le dossier NavettePro :
```bash
mysql -u root -p < database/schema.sql
```

**Option C : Copier-coller le contenu**
1. Ouvrez le fichier `database/schema.sql`
2. Copiez tout le contenu
3. Collez dans MySQL et exécutez

### 1.4 Vérifier la Création

```sql
USE navettepro;
SHOW TABLES;
```

Vous devriez voir toutes les tables créées.

---

## ⚙️ Étape 2 : Configuration du Backend

### 2.1 Modifier les Paramètres de Connexion

Ouvrez le fichier : `src/main/resources/application.properties`

Modifiez ces lignes selon votre configuration MySQL :

```properties
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE_MYSQL
```

**Exemple :**
```properties
spring.datasource.username=root
spring.datasource.password=monMotDePasse123
```

Si votre MySQL n'a pas de mot de passe, laissez vide :
```properties
spring.datasource.password=
```

---

## 🚀 Étape 3 : Démarrer le Backend (Spring Boot)

### Option A : Avec Maven (Recommandé)

Ouvrez un terminal dans le dossier du projet :

```bash
cd C:\Users\user\Desktop\NavettePro
```

**Compiler et lancer :**
```bash
mvn clean install
mvn spring-boot:run
```

**Ou en une seule commande :**
```bash
mvn clean spring-boot:run
```

### Option B : Avec VS Code

1. Ouvrez VS Code dans le dossier `NavettePro`
2. Appuyez sur `F5` ou allez dans **Run > Start Debugging**
3. Sélectionnez "Spring Boot - NavettePro"

### Option C : Avec un IDE (IntelliJ IDEA / Eclipse)

1. Ouvrez le projet dans votre IDE
2. Localisez `NavetteProApplication.java`
3. Clic droit > Run 'NavetteProApplication'

### ✅ Vérification

Vous devriez voir dans la console :
```
Started NavetteProApplication in X.XXX seconds
```

Le backend est accessible sur : **http://localhost:8080**

---

## 🌐 Étape 4 : Démarrer le Frontend

### Option A : VS Code Live Server (Recommandé)

1. **Installer l'extension Live Server** dans VS Code
   - Ouvrez VS Code
   - Allez dans Extensions (Ctrl+Shift+X)
   - Cherchez "Live Server" et installez

2. **Ouvrir le projet**
   - Dans VS Code : File > Open Folder
   - Sélectionnez le dossier `frontend`

3. **Lancer Live Server**
   - Clic droit sur `index.html`
   - Sélectionnez "Open with Live Server"
   - Ou appuyez sur `Alt+L` puis `Alt+O`

Le frontend s'ouvrira automatiquement dans votre navigateur sur : **http://localhost:5500**

### Option B : Python SimpleHTTPServer

Ouvrez un terminal dans le dossier `frontend` :

```bash
cd frontend
python -m http.server 5500
```

Puis ouvrez votre navigateur : **http://localhost:5500**

### Option C : Node.js http-server

Si vous avez Node.js installé :

```bash
npm install -g http-server
cd frontend
http-server -p 5500
```

---

## 🧪 Étape 5 : Tester l'Application

### 5.1 Page d'Accueil

1. Ouvrez votre navigateur : **http://localhost:5500**
2. Vous devriez voir la page d'accueil avec le logo NavettePro
3. Cliquez sur "Commencer"

### 5.2 Test de Connexion Admin

1. Sélectionnez "Administrateur"
2. Email : `admin@gmail.com`
3. Mot de passe : `ad123`
4. Cliquez sur "Se connecter"

Vous devriez accéder au tableau de bord administrateur.

### 5.3 Test de Connexion Chauffeur

1. Sélectionnez "Chauffeur"
2. Email : `ahmed.benali@navettepro.ma`
3. Mot de passe : `1234`
4. Cliquez sur "Se connecter"

### 5.4 Test d'Inscription Étudiant

1. Sélectionnez "Étudiant"
2. Cliquez sur "Nouveau compte étudiant ?"
3. Remplissez le formulaire :
   - Prénom : Votre prénom
   - Nom : Votre nom
   - Email : `votre.nom@mundiapolis.ma` (doit se terminer par @mundiapolis.ma)
   - Téléphone : Votre numéro
   - Mot de passe : Votre mot de passe
   - **Sélectionnez un trajet** (A, B, C, etc.)
   - **Sélectionnez un point de ramassage**
4. Cliquez sur "S'inscrire"

---

## 🔍 Dépannage

### ❌ Erreur : "Cannot connect to MySQL"

**Solution :**
1. Vérifiez que MySQL est démarré
2. Vérifiez le nom d'utilisateur et le mot de passe dans `application.properties`
3. Testez la connexion :
   ```bash
   mysql -u root -p
   ```

### ❌ Erreur : "Port 8080 already in use"

**Solution :**
1. Trouvez le processus utilisant le port :
   ```bash
   netstat -ano | findstr :8080
   ```
2. Tuez le processus ou changez le port dans `application.properties` :
   ```properties
   server.port=8081
   ```

### ❌ Erreur : "CORS policy"

**Solution :**
1. Vérifiez que le frontend tourne sur le port 5500
2. Vérifiez la configuration CORS dans `SecurityConfig.java`

### ❌ Erreur : "Table doesn't exist"

**Solution :**
1. Vérifiez que le script SQL a été exécuté
2. Vérifiez que vous êtes dans la bonne base de données :
   ```sql
   USE navettepro;
   SHOW TABLES;
   ```

### ❌ Le logo ne s'affiche pas

**Solution :**
1. Placez votre logo dans `frontend/images/logo.png`
2. Vérifiez que le fichier existe et est au format PNG

### ❌ Erreur : "JWT token validation failed"

**Solution :**
1. Déconnectez-vous et reconnectez-vous
2. Vérifiez que le backend est bien démarré
3. Vérifiez la configuration JWT dans `application.properties`

---

## 📱 Utilisation de l'Application

### Pour l'Administrateur

1. **Gestion des chauffeurs** :
   - Ajouter/Supprimer des chauffeurs
   - Modifier email et mot de passe des chauffeurs

2. **Statistiques** :
   - Voir le nombre d'étudiants, chauffeurs, trajets
   - Voir les réclamations en attente

3. **Suivi des bus** :
   - Voir la position de tous les bus en temps réel

### Pour le Chauffeur

1. **Voir son itinéraire** :
   - Liste des points de ramassage
   - Liste des étudiants inscrits

2. **Mettre à jour le statut** :
   - Démarrer/Terminer un trajet

3. **Signaler un retard** :
   - Indiquer le nombre de minutes de retard
   - Ajouter une raison

4. **Activer le GPS** :
   - Cliquer sur "Démarrer le suivi GPS"
   - Autoriser l'accès à la localisation dans le navigateur

### Pour l'Étudiant

1. **Voir la position du bus** :
   - Localisation en temps réel
   - Heure d'arrivée estimée

2. **Notifications** :
   - Recevoir des alertes (bus proche, retard, etc.)

3. **Réclamations** :
   - Signaler un problème (retard, surcharge, comportement, autre)

4. **Historique** :
   - Voir toutes ses réclamations précédentes

---

## 🛑 Arrêter l'Application

### Backend
- Dans le terminal : Appuyez sur `Ctrl+C`

### Frontend (Live Server)
- Dans VS Code : Clic droit sur le port dans la barre d'état > "Stop Port"

---

## 📝 Notes Importantes

1. **Le backend doit être démarré avant le frontend**
2. **MySQL doit être en cours d'exécution**
3. **Les deux serveurs doivent tourner simultanément** :
   - Backend : http://localhost:8080
   - Frontend : http://localhost:5500

4. **Pour un usage en production**, vous devrez :
   - Configurer un vrai serveur web (Apache/Nginx)
   - Utiliser HTTPS
   - Configurer un vrai serveur de base de données
   - Ajouter des logs et monitoring

---

## ✅ Checklist de Démarrage

- [ ] MySQL installé et démarré
- [ ] Base de données créée (script SQL exécuté)
- [ ] `application.properties` configuré avec les bons identifiants
- [ ] Backend compilé et démarré (port 8080)
- [ ] Frontend lancé (port 5500)
- [ ] Test de connexion admin réussi
- [ ] Test d'inscription étudiant réussi

---

**🎉 Félicitations ! Votre application NavettePro est maintenant opérationnelle !**

Pour toute question ou problème, consultez le fichier `README.md` ou `SETUP.md`.
