@echo off
echo ========================================
echo   NavettePro - Demarrage Application
echo ========================================
echo.

echo.
echo [INFO] Ouverture du frontend dans le navigateur...
echo [INFO] Frontend: http://localhost:8080
echo.
echo Appuyez sur Ctrl+C pour arreter le serveur
echo.

REM Lancer le serveur PHP intégré servant le dossier frontend
REM Le routeur PHP gère les requêtes API
cd /d "%~dp0"
python -m http.server 8080 --directory frontend 2>nul || node -e "const http = require('http'); const fs = require('fs'); const path = require('path'); http.createServer((req, res) => { let filePath = path.join('./frontend', req.url === '/' ? 'index.html' : req.url); fs.readFile(filePath, (err, data) => { res.writeHead(err ? 404 : 200, {'Content-Type': err ? 'text/plain' : 'text/html'}); res.end(err ? '404' : data); }); }).listen(8080, () => console.log('Server running on port 8080')); " || (echo [ERREUR] Python ou Node.js requis && pause && exit /b 1)

pause
