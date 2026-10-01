# 🚀 Démarrer l'Application NavettePro

## ⚡ Démarrage Rapide (3 Étapes)

### ✅ Étape 1 : Préparer la Base de Données

**Ouvrir MySQL et exécuter :**
```bash
mysql -u root -p < database\schema.sql
```

**OU dans MySQL :**
```sql
source database/schema.sql
```

---

### ✅ Étape 2 : Démarrer le Backend

**Option A : Script Automatique (Recommandé)**
```bash
# Double-cliquer sur :
start-backend.bat
```

**Option B : Commande Maven**
```bash
cd C:\Users\user\Desktop\NavettePro
mvn clean spring-boot:run
```

**✅ Attendre** : Vous verrez `Started NavetteProApplication`

Le backend tourne sur : **http://localhost:8080**

---

### ✅ Étape 3 : Démarrer le Frontend

**Option A : VS Code Live Server (Recommandé)**

1. Ouvrir VS Code
2. File → Open Folder → Sélectionner le dossier `frontend`
3. Installer l'extension "Live Server" si nécessaire
4. Clic droit sur `index.html` → "Open with Live Server"

**Option B : Python**
```bash
cd frontend
python -m http.server 5500
```

**✅ Le frontend s'ouvre** : **http://localhost:5500**

---

## 🎯 Vérification

1. ✅ Backend démarré → Voir "Started NavetteProApplication"
2. ✅ Frontend ouvert → http://localhost:5500
3. ✅ Cliquer "Commencer"
4. ✅ Se connecter :
   - **Admin** : `admin@gmail.com` / `ad123`
   - **Chauffeur** : `ahmed.benali@navettepro.ma` / `1234`

---

## ⚠️ Important

- **MySQL doit être démarré** avant de lancer le backend
- **Les deux serveurs doivent tourner en même temps** :
  - Backend : Port 8080
  - Frontend : Port 5500

---

## 🛑 Arrêter l'Application

- **Backend** : Appuyer sur `Ctrl+C` dans le terminal
- **Frontend** : Fermer le navigateur ou arrêter Live Server

---

**🎉 C'est parti !**
