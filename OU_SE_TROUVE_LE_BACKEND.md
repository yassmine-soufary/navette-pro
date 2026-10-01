# 🔍 Où Se Trouve le Backend ?

## 📍 Emplacement du Backend

Le backend se trouve dans le dossier : **`src/`**

```
NavettePro/
└── src/                    ← 🎯 C'EST ICI LE BACKEND
    └── main/
        ├── java/           ← Code Java (Backend)
        └── resources/      ← Configuration
```

---

## 🎯 Fichier Principal du Backend

**Fichier** : `src/main/java/ma/mundiapolis/navettepro/NavetteProApplication.java`

C'est le **point d'entrée** de l'application Spring Boot.

---

## 📂 Structure Complète du Backend

```
src/
└── main/
    ├── java/
    │   └── ma/mundiapolis/navettepro/
    │       ├── NavetteProApplication.java  ⭐ POINT D'ENTRÉE
    │       ├── config/                     (Configuration)
    │       ├── controller/                 (API REST)
    │       ├── service/                    (Logique métier)
    │       ├── repository/                 (Base de données)
    │       ├── model/                       (Entités)
    │       ├── dto/                         (Transfert de données)
    │       └── util/                       (Utilitaires)
    │
    └── resources/
        └── application.properties          (Configuration)
```

---

## 🚀 Comment Voir le Backend dans VS Code

1. **Ouvrir VS Code** dans le dossier `NavettePro`
2. **Explorateur de fichiers** (Ctrl+Shift+E)
3. **Développer** le dossier `src`
4. **Développer** `main` → `java` → `ma` → `mundiapolis` → `navettepro`
5. **Vous verrez** :
   - ✅ NavetteProApplication.java
   - ✅ config/
   - ✅ controller/
   - ✅ service/
   - ✅ repository/
   - ✅ model/
   - ✅ dto/
   - ✅ util/

---

## 🔎 Fichiers Backend Importants

| Fichier | Emplacement | Description |
|---------|-------------|-------------|
| **NavetteProApplication.java** | `src/main/java/.../` | Point d'entrée principal |
| **application.properties** | `src/main/resources/` | Configuration (MySQL, port, etc.) |
| **AuthController.java** | `src/main/java/.../controller/` | API d'authentification |
| **AdminController.java** | `src/main/java/.../controller/` | API admin |
| **SecurityConfig.java** | `src/main/java/.../config/` | Configuration sécurité |

---

## ✅ Le Backend Existe Bien !

**Tous les fichiers backend sont présents** dans le dossier `src/`.

Si vous ne les voyez pas :
1. Vérifiez que vous êtes dans le bon dossier
2. Développez le dossier `src` dans l'explorateur
3. Les fichiers Java ont l'extension `.java`

---

## 🎬 Pour Démarrer le Backend

```bash
cd C:\Users\user\Desktop\NavettePro
mvn clean spring-boot:run
```

OU double-cliquer sur : **`start-backend.bat`**

---

**Le backend est là ! Cherchez dans le dossier `src/`** 📁
