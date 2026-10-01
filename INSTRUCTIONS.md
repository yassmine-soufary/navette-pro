# 📖 Instructions d'Exécution - NavettePro

## 🎬 Démarrage en 3 Étapes Simples

### 1️⃣ Préparer la Base de Données

**Ouvrir MySQL et exécuter :**
```sql
source database/schema.sql
```

**OU depuis le terminal :**
```bash
mysql -u root -p < database/schema.sql
```

### 2️⃣ Configurer le Backend

**Éditer** `src/main/resources/application.properties` :
```properties
spring.datasource.password=VOTRE_MOT_DE_PASSE_MYSQL
```

### 3️⃣ Lancer l'Application

**Terminal 1 - Backend :**
```bash
mvn clean spring-boot:run
```

**Terminal 2 - Frontend :**
```bash
cd frontend
python -m http.server 5500
```

**OU utiliser VS Code Live Server** (voir GUIDE_EXECUTION.md)

---

## 🌐 Accès

- **Frontend** : http://localhost:5500
- **Backend API** : http://localhost:8080/api

---

## 🔑 Comptes de Test

| Rôle | Email | Mot de passe |
|------|-------|-------------|
| Admin | admin@gmail.com | ad123 |
| Chauffeur | ahmed.benali@navettepro.ma | 1234 |
| Étudiant | S'inscrire avec @mundiapolis.ma | Votre choix |

---

## 📚 Documentation Complète

- **GUIDE_EXECUTION.md** - Guide détaillé avec dépannage
- **DEMARRAGE_RAPIDE.md** - Version condensée
- **README.md** - Documentation générale

---

**✅ Prêt à démarrer !**
