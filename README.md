# OpenHands Android App

![Android CI](https://github.com/jereidk/openhands-android/workflows/Android%20CI/CD/badge.svg)
![Release](https://img.shields.io/github/v/release/jereidk/openhands-android?include_prereleases)
![License](https://img.shields.io/github/license/jereidk/openhands-android)

> ⚠️ **Wrapper no oficial** para la plataforma [OpenHands](https://www.all-hands.dev) creada por [All-Hands-AI](https://github.com/All-Hands-AI/OpenHands).

---

## 🏢 Sobre OpenHands

**OpenHands** es un Agente de Software con IA que te ayuda a construir, probar y desplegar código. Este wrapper permite acceder a OpenHands desde dispositivos Android.

- 🌐 [Sitio web](https://www.all-hands.dev)
- 📖 [Documentación](https://docs.openhands.dev)
- 💬 [Discord](https://discord.gg/YS茄子MST8)
- 🐙 [Repositorio principal](https://github.com/All-Hands-AI/OpenHands)

## 👨‍💻 Créditos

| Rol | Descripción |
|-----|-------------|
| **OpenHands AI** | Agente IA utilizado para generar este wrapper |
| **All-Hands-AI** | Creadores de la plataforma OpenHands original |

---

## 🛡️ Legal y Licencia

- Este es un proyecto **no oficial** y **no afiliado** con All-Hands-AI
- **OpenHands®** es marca registrada de All-Hands-AI
- Ver [LICENSE](LICENSE) para términos completos
- El uso está sujeto a los [Términos de Servicio de OpenHands](https://www.all-hands.dev/terms)

---

## 📱 Features

- Full WebView integration with app.all-hands.dev
- Native Android experience with Material Design
- Deep linking support (openhands://)
- Hardware accelerated rendering
- Offline capability (partial)
- GitHub, GitLab, and Bitbucket OAuth support

## 🏗️ Building the APK

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 34
- Java 11 or later

### Build with Gradle

```bash
cd android
./gradlew assembleDebug
```

The APK will be generated at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

### Build Release APK

```bash
./gradlew assembleRelease
```

## 📁 Project Structure

```
openhands-android/
├── android/
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/openhands/app/
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── mipmap-*/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── build.gradle
│   ├── settings.gradle
│   └── gradlew
└── README.md
```

## 🔧 Configuration

### Web URL

The default URL is `https://app.all-hands.dev/`. To change it, edit:

`android/app/src/main/java/com/openhands/app/MainActivity.java`

```java
private String baseUrl = "https://app.all-hands.dev/";
```

### Deep Links

Deep links are configured to handle `https://app.all-hands.dev/*` URLs.

## 📦 Installing

1. Enable "Install from unknown sources" in Android settings
2. Transfer `app-debug.apk` to your device
3. Open the APK file and install

## 🛠️ Development

### Setup Android Studio

1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the `android/` folder
4. Wait for Gradle sync to complete

### Run on Device

1. Connect your Android device via USB
2. Enable USB debugging on your device
3. Click "Run" in Android Studio or:
   ```bash
   ./gradlew installDebug
   ```

## 📄 License

MIT License - See LICENSE file for details

## 🌐 Links

- [OpenHands Website](https://www.all-hands.dev)
- [Documentation](https://docs.openhands.dev)
- [GitHub Repository](https://github.com/All-Hands-AI/OpenHands)