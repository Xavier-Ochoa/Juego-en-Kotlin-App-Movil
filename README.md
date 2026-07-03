# 🎱 Gravity Ball

**Gravity Ball** es un juego para Android en el que controlas una bolita
inclinando el dispositivo. El objetivo es sobrevivir el mayor tiempo posible
esquivando los obstáculos que aparecen en pantalla: el puntaje sube por cada
segundo que sigues con vida.

El juego está construido con **Android nativo (Kotlin + Android SDK
tradicional)**, sin frameworks de UI declarativa: toda la pantalla del juego
se dibuja a mano con `Canvas`, y el movimiento se controla en tiempo real con
el sensor acelerómetro del dispositivo.

---

## 📱 Capturas

## Capturas de pantalla

<table align="center">
  <tr>
    <th>Inicio</th>
    <th>Juego</th>
    <th>Game Over</th>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/9d8a86e0-43ec-4c41-96cb-0e26864fbe11" width="220"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/2effd2ce-9ff8-44a8-969d-40ce48aef044" width="220"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/1bbf8d32-1857-408a-beb5-fada42bcf85e" width="220"/>
    </td>
  </tr>
</table>

*(Agrega aquí tus propias capturas de pantalla ejecutando la app en un
emulador o dispositivo físico.)*

---

## ✨ Características

- **Control por acelerómetro**: inclina el celular para mover la bolita en
  cualquier dirección, con un movimiento suavizado (no salta de golpe con el
  valor crudo del sensor).
- **Límites de pantalla**: la bolita nunca se sale de los bordes.
- **Coordenadas en tiempo real**: se muestran los valores `X` e `Y` de la
  posición actual de la bolita.
- **Obstáculos y colisiones**: varios obstáculos rectangulares con detección
  de colisión círculo–rectángulo.
- **Puntaje por supervivencia**: aumenta automáticamente un punto por cada
  segundo que el jugador sigue con vida.
- **Game Over y reinicio**: al chocar, se muestra un diálogo con el puntaje
  final y un botón "Jugar de nuevo" que reinicia la partida por completo
  (posición, puntaje, color, tamaño y obstáculos).
- **Personalización de la bolita**: botones en pantalla para cambiar su
  color (cíclico entre varias opciones) y su tamaño (aumentar / disminuir
  dentro de un rango controlado).

---

## 🕹️ Cómo se juega

1. Desde la pantalla de inicio, presiona **Jugar**.
2. Inclina el dispositivo para mover la bolita:
   - Inclinar a la **derecha** → la bolita se mueve a la derecha.
   - Inclinar a la **izquierda** → la bolita se mueve a la izquierda.
   - Inclinar hacia **abajo** → la bolita se mueve hacia abajo.
   - Inclinar hacia **arriba** → la bolita se mueve hacia arriba.
3. Esquiva los obstáculos rojos. El puntaje (esquina superior derecha) sube
   automáticamente mientras sobrevives.
4. Opcional: usa los botones **Color**, **Tamaño +** y **Tamaño -** (esquina
   inferior izquierda) para personalizar la bolita mientras juegas.
5. Si chocas con un obstáculo, aparece el diálogo **GAME OVER** con tu
   puntaje final. Presiona **Jugar de nuevo** para reiniciar.

---

## 🛠️ Tecnologías y versiones

| Componente | Versión |
|---|---|
| Lenguaje | Kotlin 2.0.21 |
| Android Gradle Plugin (AGP) | 8.7.3 |
| Gradle wrapper | 8.9 |
| compileSdk / targetSdk | 35 |
| minSdk | 24 (Android 7.0) |
| AndroidX Core KTX | 1.15.0 |
| AppCompat | 1.7.0 |
| Material Components | 1.12.0 |
| ConstraintLayout | 2.1.4 |
| ViewBinding | activado |

No se usa Jetpack Compose ni ninguna librería de terceros para el juego: el
renderizado corre 100% sobre `View` + `Canvas` + `Paint`, y el sensor se
maneja con `SensorManager` / `SensorEventListener` estándar del SDK.

---

## 🏗️ Arquitectura del proyecto

El proyecto sigue una arquitectura modular donde **cada clase tiene una
única responsabilidad**:

```
com.example.gravityball
├── MainActivity.kt          → Pantalla del juego: conecta sensor, BallView,
│                               botones de personalización y diálogo de Game Over.
├── ui/
│   ├── HomeActivity.kt       → Pantalla de inicio (LAUNCHER), navega al juego.
│   └── GameOverDialog.kt     → Diálogo de Game Over con puntaje y botón de reinicio.
├── views/
│   └── BallView.kt           → View personalizada: dibuja TODO con Canvas
│                                (fondo, obstáculos, bolita, texto) y coordina
│                                movimiento, colisiones y reinicio visual.
├── sensor/
│   └── AccelerometerManager.kt → Encapsula el registro/desregistro del
│                                  acelerómetro y expone los valores X, Y
│                                  mediante un callback simple.
├── model/
│   ├── Ball.kt                → Estado de la bolita (posición, radio, color).
│   └── Obstacle.kt            → Estado de un obstáculo rectangular.
├── util/
│   └── Collision.kt           → Función pura para detectar colisión
│                                 círculo–rectángulo (sin dependencias de Android UI).
└── game/
    └── GameManager.kt         → Lógica de negocio del juego: puntaje y
                                  estado jugando/game-over, independiente del dibujado.
```

**Flujo de datos:**
`AccelerometerManager` (sensor) → `MainActivity` (orquesta) → `BallView`
(mueve, dibuja, detecta colisiones) → `GameManager` (puntaje/estado) →
`GameOverDialog` (UI de fin de juego) → `MainActivity.resetGame()` (reinicia
todo el ciclo).

---

## 📂 Estructura de carpetas

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
│       ├── java/com/example/gravityball/   (ver árbol de arquitectura arriba)
│       └── res/
│           ├── layout/       → activity_home.xml, activity_main.xml
│           ├── values/       → strings.xml, colors.xml, themes.xml
│           ├── drawable/     → iconos vectoriales
│           └── mipmap*/      → íconos de la app
└── README.md
```

---

## ▶️ Cómo compilar y ejecutar

### Requisitos previos
- [Android Studio](https://developer.android.com/studio) (versión reciente,
  compatible con AGP 8.7.3 / Kotlin 2.0.21).
- JDK 17 o superior (Android Studio lo incluye por defecto).
- Un dispositivo físico con **acelerómetro** (recomendado, ya que el
  emulador simula el sensor de forma limitada) o un emulador con sensores
  virtuales configurados.

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/<tu-usuario>/GravityBall.git
   ```
2. Abre la carpeta `GravityBall` con Android Studio y espera a que termine
   el **Gradle Sync**.
3. Conecta un dispositivo Android físico (con la **Depuración USB**
   activada) o inicia un emulador.
4. Presiona **Run ▶** en Android Studio, o desde la terminal:
   ```bash
   ./gradlew clean assembleDebug
   ./gradlew installDebug
   ```
5. Abre la app en el dispositivo e inclina el celular para jugar.

---

## 🧩 Posibles mejoras futuras

- Dificultad progresiva (obstáculos más rápidos o numerosos con el tiempo).
- Tabla de mejores puntajes (persistencia local).
- Sonido y efectos al chocar o sumar puntos.
- Selector visual de colores (paleta) en vez de un botón cíclico.
- Modo de obstáculos en movimiento.

---

## 📄 Licencia

Este proyecto es de uso educativo, desarrollado como parte del taller
**"De Java a Kotlin + Android Sensores"**. Puedes usarlo, modificarlo y
compartirlo libremente citando la fuente.
