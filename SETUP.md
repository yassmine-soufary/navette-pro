# Guide de Démarrage Rapide - NavettePro

## Prérequis

1. **Java 17+** installé
2. **Maven 3.6+** installé
3. **MySQL 8.0+** installé et en cours d'exécution
4. **Un serveur web** pour le frontend (VS Code Live Server recommandé)

## Installation

### 1. Base de données

```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script SQL
source database/schema.sql
# OU
mysql -u root -p navettepro < database/schema.sql
```

### 2. Configuration Backend

Éditer `src/main/resources/application.properties` :
```properties
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe_mysql
```

### 3. Compiler et lancer le backend

```bash
# Dans le répertoire racine du projet
mvn clean install
mvn spring-boot:run
```

Le backend sera accessible sur : `http://localhost:8080`

### 4. Lancer le frontend

**Option 1 : VS Code Live Server**
1. Ouvrir VS Code dans le dossier `frontend`
2. Installer l'extension "Live Server"
3. Clic droit sur `index.html` → "Open with Live Server"

**Option 2 : Python SimpleHTTPServer**
```bash
cd frontend
python -m http.server 5500
```

Le frontend sera accessible sur : `http://localhost:5500`

## Comptes de test

### Administrateur
- Email: `admin@gmail.com`
- Mot de passe: `ad123`

### Chauffeur (exemple)
- Email: `ahmed.benali@navettepro.ma`
- Mot de passe: `1234`

### Étudiant
- S'inscrire avec un email `@mundiapolis.ma`
- Sélectionner un trajet et un point de ramassage lors de l'inscription

## Structure des URLs

- **Frontend**: http://localhost:5500
- **Backend API**: http://localhost:8080/api

## Vérification

1. Ouvrir `http://localhost:5500` dans votre navigateur
2. Cliquer sur "Commencer"
3. Se connecter avec le compte admin ou créer un compte étudiant

## Dépannage

### Erreur de connexion à la base de données
- Vérifier que MySQL est en cours d'exécution
- Vérifier les identifiants dans `application.properties`

### Erreur CORS
- Vérifier que le frontend tourne sur le port 5500
- Vérifier la configuration CORS dans `SecurityConfig.java`

### Le logo ne s'affiche pas
- Placer le logo dans `frontend/images/logo.png`
- Format recommandé: PNG, 200x200px

## Prochaines étapes

1. Ajouter le logo de l'université dans `frontend/images/logo.png`
2. Configurer les coordonnées GPS des points de ramassage
3. Intégrer une vraie carte (Google Maps API ou Leaflet)
4. Implémenter la génération de rapports PDF
