# NavettePro - Application de Gestion du Transport Universitaire

Application mobile intelligente de gestion du transport universitaire avec 3 rôles distincts : Étudiant, Chauffeur et Administrateur.

## Technologies utilisées

- **Frontend**: HTML + CSS + JavaScript
- **Backend**: Java (Spring Boot)
- **Base de données**: MySQL

## Structure du projet

```
NavettePro/
├── src/
│   └── main/
│       ├── java/
│       │   └── ma/mundiapolis/navettepro/
│       │       ├── config/          # Configuration (Security, CORS)
│       │       ├── controller/      # Contrôleurs REST
│       │       ├── dto/             # Data Transfer Objects
│       │       ├── model/           # Entités JPA
│       │       ├── repository/      # Repositories JPA
│       │       ├── service/         # Services métier
│       │       └── util/            # Utilitaires (JWT)
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── css/
│   │   └── style.css
│   ├── js/
│   │   ├── main.js
│   │   ├── auth.js
│   │   ├── student.js
│   │   ├── driver.js
│   │   └── admin.js
│   ├── images/
│   │   └── logo.png
│   ├── index.html
│   ├── login.html
│   ├── student-dashboard.html
│   ├── driver-dashboard.html
│   └── admin-dashboard.html
├── database/
│   └── schema.sql
└── pom.xml
```

## Installation et configuration

### 1. Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- Un serveur web local (pour le frontend) ou Live Server dans VS Code

### 2. Configuration de la base de données

1. Créer une base de données MySQL :
```sql
CREATE DATABASE navettepro;
```

2. Exécuter le script SQL :
```bash
mysql -u root -p navettepro < database/schema.sql
```

3. Configurer les paramètres de connexion dans `src/main/resources/application.properties` :
```properties
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

### 3. Compilation et exécution du backend

```bash
# Compiler le projet
mvn clean install

# Exécuter l'application
mvn spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080`

### 4. Configuration du frontend

1. Ouvrir le dossier `frontend` dans un serveur web local
2. Si vous utilisez VS Code, installer l'extension "Live Server"
3. Ouvrir `index.html` avec Live Server

Le frontend sera accessible sur `http://localhost:5500` (ou le port configuré)

## Comptes par défaut

### Administrateur
- **Email**: admin@gmail.com
- **Mot de passe**: ad123

### Chauffeurs
Les chauffeurs sont créés automatiquement lors de l'initialisation de la base de données. Le mot de passe par défaut est `1234`.

### Étudiants
Les étudiants doivent s'inscrire avec un email universitaire (@mundiapolis.ma).

## Fonctionnalités

### 👨‍🎓 Interface Étudiant
- Localisation GPS du bus en temps réel
- Estimation de l'heure d'arrivée (ETA)
- Notifications automatiques (bus proche, retard)
- Signalement de surcharge
- Réclamations structurées
- Historique des trajets

### 🚌 Interface Chauffeur
- Itinéraire optimisé
- Liste des étudiants inscrits dans son trajet
- Alerte retard
- Communication interne avec administration
- Mise à jour statut trajet (En cours / Terminé)
- Mise à jour position GPS

### 👨‍💼 Interface Administration
- Tableau de bord avec statistiques
- Suivi des bus en temps réel
- Statistiques des retards
- Analyse des réclamations
- Évaluation des chauffeurs
- Gestion complète des chauffeurs et trajets
- Génération de rapports mensuels PDF (à implémenter)

## Trajets disponibles

L'application inclut 9 trajets (A à I) avec leurs points de ramassage respectifs, comme spécifié dans les exigences.

## API Endpoints

### Authentification
- `POST /api/auth/login` - Connexion
- `POST /api/auth/register` - Inscription étudiant

### Routes
- `GET /api/routes` - Liste des trajets
- `GET /api/routes/{code}` - Détails d'un trajet
- `GET /api/routes/{routeId}/pickup-points` - Points de ramassage

### Étudiant
- `GET /api/student/{studentId}/route` - Trajet de l'étudiant
- `POST /api/student/complaint` - Créer une réclamation
- `GET /api/student/{studentId}/complaints` - Historique des réclamations
- `GET /api/student/{studentId}/notifications` - Notifications

### Chauffeur
- `GET /api/driver/{routeId}/students` - Liste des étudiants
- `POST /api/driver/trip-status` - Mettre à jour le statut
- `GET /api/driver/{routeId}/trip-status` - Statut actuel

### Administration
- `GET /api/admin/drivers` - Liste des chauffeurs
- `POST /api/admin/drivers` - Créer un chauffeur
- `DELETE /api/admin/drivers/{driverId}` - Supprimer un chauffeur
- `PUT /api/admin/drivers/{driverId}/email` - Modifier l'email
- `PUT /api/admin/drivers/{driverId}/password` - Modifier le mot de passe
- `GET /api/admin/complaints` - Toutes les réclamations
- `GET /api/admin/statistics` - Statistiques

### Localisation GPS
- `POST /api/bus-location/update` - Mettre à jour la position
- `GET /api/bus-location/route/{routeId}` - Position par trajet
- `GET /api/bus-location/driver/{driverId}` - Position par chauffeur

## Notes importantes

1. **Logo**: Placez le logo de l'application dans `frontend/images/logo.png`
2. **CORS**: Le backend est configuré pour accepter les requêtes depuis `http://localhost:5500`
3. **JWT**: Les tokens JWT sont valides pendant 24 heures
4. **Sécurité**: Les mots de passe sont hashés avec BCrypt

## Développement futur

- Intégration d'une vraie carte (Google Maps ou Leaflet)
- Calcul ETA basé sur la distance réelle
- Notifications push
- Génération de rapports PDF
- Application mobile native (React Native ou Flutter)

## Auteur

NavettePro - Application de gestion du transport universitaire Mundiapolis
