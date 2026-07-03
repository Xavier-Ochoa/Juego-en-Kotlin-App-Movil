# Gravity Ball — Fase 1 a Fase 5

Este ZIP contiene todo lo desarrollado hasta la **Fase 5** del proyecto:
- Fase 1: Gradle, estructura, HomeActivity, MainActivity, navegación.
- Fase 2: Ball.kt, Obstacle.kt, BallView.kt — bolita dibujada con Canvas.
- Fase 3: AccelerometerManager.kt — movimiento por inclinación.
- Fase 4: límites de pantalla + coordenadas X/Y e instrucciones en pantalla.
- Fase 5: Collision.kt, GameOverDialog.kt — obstáculos, detección de
  colisiones y diálogo de Game Over con reinicio.

## ⚠️ Antes de copiar estos archivos a tu proyecto

Este ZIP **NO incluye**:
- `gradlew`
- `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `.gradle/`, `.idea/`, `.kotlin/` (carpetas generadas por Android Studio)

Esto es intencional: esos archivos ya existen en tu proyecto
(`C:\Users\APP MOVILES\Documents\kotlin`) creado con Android Studio y no deben
reemplazarse por texto plano (el `.jar` es un binario).

## Cómo instalar este ZIP en tu proyecto existente

1. Cierra Android Studio.
2. Extrae este ZIP.
3. Copia **todo el contenido** de la carpeta extraída dentro de tu carpeta
   `C:\Users\APP MOVILES\Documents\kotlin`, reemplazando cuando se te pida:
   - `build.gradle.kts` (raíz)
   - `settings.gradle.kts`
   - `gradle.properties`
   - `gradle/wrapper/gradle-wrapper.properties`
   - `app/build.gradle.kts`
   - `app/src/main/AndroidManifest.xml`
   - todo lo demás dentro de `app/src/main/...` es nuevo, se agrega sin conflicto.
4. Abre PowerShell en la raíz del proyecto y ejecuta:
   ```powershell
   .\gradlew wrapper --gradle-version 8.9
   .\gradlew clean assembleDebug
   ```
5. Abre el proyecto en Android Studio y espera el **Gradle Sync**.
6. Ejecuta la app en tu celular físico (con Depuración USB activada).

## Qué deberías ver

- Pantalla de inicio con el título **Gravity Ball**, un subtítulo y el botón **Jugar**.
- Al presionar Jugar, la bolita se mueve al inclinar el celular, nunca sale de
  la pantalla, y hay 3 obstáculos rojos en pantalla.
- Al chocar con un obstáculo, el juego se detiene y aparece un diálogo
  **GAME OVER** con el puntaje (0 por ahora, el puntaje real llega en la
  Fase 6) y un botón "Jugar de nuevo" que reinicia la bolita y los obstáculos.

## Estructura incluida

```
GravityBall/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/gravityball/
│       │   ├── MainActivity.kt
│       │   ├── ui/HomeActivity.kt
│       │   ├── views/        (vacío, se llena en Fase 2)
│       │   ├── sensor/       (vacío, se llena en Fase 3)
│       │   ├── model/        (vacío, se llena en Fase 2)
│       │   ├── util/         (vacío, se llena en Fase 5)
│       │   └── game/         (vacío, se llena en Fase 6)
│       └── res/
│           ├── layout/activity_home.xml
│           ├── layout/activity_main.xml
│           ├── values/strings.xml
│           ├── values/colors.xml
│           ├── values/themes.xml
│           ├── drawable/ic_launcher_background.xml
│           ├── drawable/ic_launcher_foreground.xml
│           ├── mipmap-anydpi-v26/ic_launcher.xml
│           ├── mipmap-anydpi-v26/ic_launcher_round.xml
│           ├── mipmap/ic_launcher.xml
│           └── mipmap/ic_launcher_round.xml
└── README.md
```

## Próxima fase

Fase 6: puntaje real (GameManager), cambio de color/tamaño y reinicio completo.
