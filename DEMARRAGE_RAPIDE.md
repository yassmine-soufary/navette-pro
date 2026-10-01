# ⚡ Démarrage Rapide - NavettePro

## 🎯 En 5 Minutes

### Étape 1 : Base de Données (2 min)

```bash
# Ouvrir MySQL
mysql -u root -p

# Exécuter le script
source database/schema.sql
```

**OU** depuis le terminal Windows :
```bash
mysql -u root -p < database\schema.sql
```

### Étape 2 : Configurer MySQL (30 sec)

Ouvrir `src/main/resources/application.properties` et modifier :
```properties
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

Si pas de mot de passe, laissez vide :
```properties
spring.datasource.password=
```

### Étape 3 : Démarrer le Backend (1 min)

**Option A : Script automatique (Windows)**
```bash
start-backend.bat
```

**Option B : Commande Maven**
```bash
mvn clean spring-boot:run
```

✅ Attendre : `Started NavetteProApplication`

### Étape 4 : Démarrer le Frontend (1 min)

**Option A : VS Code Live Server**
1. Ouvrir VS Code dans le dossier `frontend`
2. Clic droit sur `index.html` → "Open with Live Server"

**Option B : Python**
```bash
cd frontend
python -m http.server 5500
```

### Étape 5 : Tester (30 sec)

1. Ouvrir : **http://localhost:5500**
2. Cliquer "Commencer"
3. Se connecter :
   - **Admin** : `admin@gmail.com` / `ad123`
   - **Chauffeur** : `ahmed.benali@navettepro.ma` / `1234`

---

## 📋 Checklist

- [ ] MySQL démarré
- [ ] Base de données créée
- [ ] `application.properties` configuré
- [ ] Backend démarré (port 8080)
- [ ] Frontend démarré (port 5500)
- [ ] Test de connexion réussi

---

## 🆘 Problèmes Fréquents

| Problème | Solution |
|----------|----------|
| Port 8080 occupé | Changer le port dans `application.properties` |
| Erreur MySQL | Vérifier que MySQL est démarré |
| CORS error | Vérifier que le frontend est sur le port 5500 |
| Logo manquant | Placer `logo.png` dans `frontend/images/` |

---

## 📚 Documentation Complète

Pour plus de détails, consultez : **GUIDE_EXECUTION.md**

---

**🚀 C'est parti !**
