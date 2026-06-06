# 📤 Cómo subir el proyecto a GitHub

El token de GitHub disponible es de una GitHub App que no tiene permisos para crear repositorios nuevos.

## Opciones para subir:

### Opción 1: Usar GitHub CLI con token personal
Si tienes un token personal con permisos de `repo`:
```bash
gh auth login --token <tu-token-personal>
gh repo create openhands-android --public
git remote add origin https://github.com/jereidk/openhands-android.git
git push -u origin master
```

### Opción 2: Subir manualmente
1. Ve a https://github.com/new
2. Nombre: `openhands-android`
3. Crea el repositorio vacío
4. Luego ejecuta:
```bash
git remote add origin https://github.com/jereidk/openhands-android.git
git push -u origin master
```

### Opción 3: Crear en una organización
Si tienes una organización de OpenHands:
```bash
gh repo create openhands-android --org <org-name> --public
```

---

## Archivos listos para subir:
- ✅ android/ (proyecto Android Studio)
- ✅ PWA files (index.html, manifest.json, sw.js)
- ✅ icons/ (iconos PNG)
- ✅ README.md
- ✅ .gitignore

## Archivos en el proyecto:
- android/app/src/main/java/com/openhands/app/MainActivity.java
- android/app/src/main/res/layout/activity_main.xml
- android/app/build.gradle
- android/build.gradle
- android/settings.gradle
- android/gradlew
- manifest.json, index.html, sw.js