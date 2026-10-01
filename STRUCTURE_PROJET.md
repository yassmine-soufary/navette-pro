# 📁 Structure du Projet NavettePro

## 📂 Organisation des Fichiers

```
NavettePro/
│
├── 📁 src/                          ← BACKEND (Code Java Spring Boot)
│   └── main/
│       ├── java/
│       │   └── ma/mundiapolis/navettepro/
│       │       ├── 🎯 NavetteProApplication.java  ← POINT D'ENTRÉE PRINCIPAL
│       │       ├── 📁 config/                     ← Configuration (Sécurité, CORS)
│       │       │   ├── SecurityConfig.java
│       │       │   └── JwtAuthenticationFilter.java
│       │       ├── 📁 controller/                 ← Contrôleurs REST API
│       │       │   ├── AuthController.java
│       │       │   ├── AdminController.java
│       │       │   ├── StudentController.java
│       │       │   ├── DriverController.java
│       │       │   ├── RouteController.java
│       │       │   └── BusLocationController.java
│       │       ├── 📁 service/                    ← Logique métier
│       │       │   ├── AuthService.java
│       │       │   ├── AdminService.java
│       │       │   ├── StudentService.java
│       │       │   ├── RouteService.java
│       │       │   └── ...
│       │       ├── 📁 repository/                 ← Accès base de données
│       │       │   ├── UserRepository.java
│       │       │   ├── RouteRepository.java
│       │       │   └── ...
│       │       ├── 📁 model/                      ← Entités (Tables)
│       │       │   ├── User.java
│       │       │   ├── Route.java
│       │       │   ├── Complaint.java
│       │       │   └── ...
│       │       ├── 📁 dto/                        ← Objets de transfert
│       │       │   ├── AuthRequest.java
│       │       │   └── AuthResponse.java
│       │       └── 📁 util/                      ← Utilitaires
│       │           └── JwtUtil.java
│       └── resources/
│           └── application.properties            ← Configuration Backend
│
├── 📁 frontend/                     ← FRONTEND (HTML/CSS/JavaScript)
│   ├── index.html                   ← Page d'accueil
│   ├── login.html                   ← Page de connexion
│   ├── student-dashboard.html       ← Interface étudiant
│   ├── driver-dashboard.html        ← Interface chauffeur
│   ├── admin-dashboard.html          ← Interface admin
│   ├── 📁 css/
│   │   └── style.css
│   ├── 📁 js/
│   │   ├── main.js
│   │   ├── auth.js
│   │   ├── student.js
│   │   ├── driver.js
│   │   └── admin.js
│   └── 📁 images/
│       └── logo.png
│
├── 📁 database/                      ← Scripts SQL
│   └── schema.sql                   ← Schéma de base de données
│
├── 📄 pom.xml                        ← Configuration Maven (Backend)
├── 📄 start-backend.bat              ← Script de démarrage Windows
│
└── 📄 README.md                      ← Documentation

```

## 🎯 Fichiers Backend Principaux

### 1. **NavetteProApplication.java** ⭐
**Emplacement** : `src/main/java/ma/mundiapolis/navettepro/NavetteProApplication.java`

C'est le **point d'entrée principal** de l'application Spring Boot.
```java
@SpringBootApplication
public class NavetteProApplication {
    public static void main(String[] args) {
        SpringApplication.run(NavetteProApplication.class, args);
    }
}
```

**Pour démarrer** : Exécuter cette classe ou utiliser `mvn spring-boot:run`

---

### 2. **application.properties** ⚙️
**Emplacement** : `src/main/resources/application.properties`

Configuration du backend :
- Port du serveur (8080)
- Connexion MySQL
- Configuration JWT
- CORS

---

### 3. **Contrôleurs** 🌐
**Emplacement** : `src/main/java/.../controller/`

Ces fichiers gèrent les **endpoints API REST** :

- `AuthController.java` → `/api/auth/login`, `/api/auth/register`
- `AdminController.java` → `/api/admin/*`
- `StudentController.java` → `/api/student/*`
- `DriverController.java` → `/api/driver/*`
- `RouteController.java` → `/api/routes/*`
- `BusLocationController.java` → `/api/bus-location/*`

---

### 4. **Services** 💼
**Emplacement** : `src/main/java/.../service/`

Logique métier de l'application :
- `AuthService.java` → Authentification
- `AdminService.java` → Gestion admin
- `RouteService.java` → Gestion des trajets
- etc.

---

### 5. **Modèles** 📊
**Emplacement** : `src/main/java/.../model/`

Entités JPA (correspondent aux tables MySQL) :
- `User.java` → Table `users`
- `Route.java` → Table `routes`
- `Complaint.java` → Table `complaints`
- etc.

---

## 🔍 Où Trouver le Backend ?

### Dans VS Code / IntelliJ IDEA :

1. **Ouvrir le dossier** : `src/main/java/ma/mundiapolis/navettepro/`
2. **Fichier principal** : `NavetteProApplication.java`
3. **Structure complète** :
   ```
   src/
   └── main/
       └── java/
           └── ma/
               └── mundiapolis/
                   └── navettepro/
                       ├── NavetteProApplication.java  ← DÉMARRER ICI
                       ├── config/
                       ├── controller/
                       ├── service/
                       ├── repository/
                       ├── model/
                       ├── dto/
                       └── util/
   ```

---

## 🚀 Comment Démarrer le Backend

### Méthode 1 : Maven (Terminal)
```bash
cd C:\Users\user\Desktop\NavettePro
mvn clean spring-boot:run
```

### Méthode 2 : Script Windows
```bash
start-backend.bat
```

### Méthode 3 : VS Code
1. Ouvrir `NavetteProApplication.java`
2. Clic droit → "Run Java"
3. Ou appuyer sur `F5` (avec configuration launch.json)

### Méthode 4 : IntelliJ IDEA
1. Ouvrir le projet
2. Localiser `NavetteProApplication.java`
3. Clic droit → "Run 'NavetteProApplication'"

---

## ✅ Vérification

Quand le backend démarre, vous verrez :
```
Started NavetteProApplication in X.XXX seconds
```

Le backend sera accessible sur : **http://localhost:8080**

Testez l'API : **http://localhost:8080/api/routes**

---

## 📝 Notes

- **Backend** = Code Java dans `src/main/java/`
- **Frontend** = Code HTML/JS dans `frontend/`
- Les deux doivent tourner **simultanément** :
  - Backend sur le port **8080**
  - Frontend sur le port **5500**

---

**Le backend est bien présent dans le projet ! Il se trouve dans le dossier `src/`** 🎯
