package com.example.gravityball

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.gravityball.databinding.ActivityMainBinding
import com.example.gravityball.model.Ball
import com.example.gravityball.sensor.AccelerometerManager
import com.example.gravityball.ui.GameOverDialog
import com.example.gravityball.views.BallView

/**
 * MainActivity: pantalla del juego.
 *
 * Responsabilidad única de esta clase (según la arquitectura del taller):
 * - Inicializar BallView.
 * - Crear el AccelerometerManager (que a su vez maneja el SensorManager).
 * - Iniciar el sensor en onResume() y detenerlo en onPause().
 * - Enviar los datos del sensor hacia BallView.
 * - Reaccionar al Game Over mostrando el diálogo correspondiente.
 * - Conectar los botones de "Color", "Tamaño +" y "Tamaño -" (definidos en
 *   el XML) con los métodos públicos setBallColor()/setBallRadius() de BallView.
 *
 * NO contiene lógica del juego, del sensor ni de colisiones: esas viven en
 * BallView, AccelerometerManager y Collision respectivamente.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ballView: BallView
    private lateinit var accelerometerManager: AccelerometerManager

    // Opciones de color disponibles para personalizar la bolita
    // (reto - nivel básico). El primer valor coincide con el color por
    // defecto que usa Ball.kt / BallView.resetGame().
    private val ballColors = listOf(
        Color.parseColor("#00E676"), // verde (por defecto)
        Color.parseColor("#2979FF"), // azul
        Color.parseColor("#FFEA00"), // amarillo
        Color.parseColor("#E040FB")  // violeta
    )
    private var colorIndex = 0

    // Límites y paso para aumentar/disminuir el tamaño de la bolita con
    // los botones "Tamaño +" / "Tamaño -".
    private val minBallRadius = 20f
    private val maxBallRadius = 90f
    private val ballRadiusStep = 10f
    private var currentBallRadius = Ball.DEFAULT_RADIUS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creamos BallView por código y la insertamos en el índice 0 del
        // FrameLayout raíz, para que quede DEBAJO de los botones definidos
        // en el XML (que se agregan encima, visibles).
        ballView = BallView(this)
        binding.root.addView(
            ballView,
            0,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // Cada vez que el acelerómetro entrega nuevos valores,
        // se los reenviamos a BallView para que mueva la bolita.
        accelerometerManager = AccelerometerManager(this) { x, y ->
            ballView.onAccelerometerMoved(x, y)
        }

        // Cuando BallView detecta una colisión, detenemos el sensor y
        // mostramos el diálogo de Game Over con el puntaje final.
        ballView.setOnGameOverListener { finalScore ->
            accelerometerManager.stop()
            GameOverDialog.show(this, finalScore) {
                colorIndex = 0
                currentBallRadius = Ball.DEFAULT_RADIUS
                ballView.resetGame()
                accelerometerManager.start()
            }
        }

        setupCustomizationButtons()
    }

    /**
     * Conecta los botones "Color", "Tamaño +" y "Tamaño -" del XML con los
     * métodos públicos de BallView.
     * - "Color" avanza cíclicamente entre una lista de colores predefinidos.
     * - "Tamaño +" / "Tamaño -" aumentan o disminuyen el radio de la bolita
     *   dentro de un rango fijo (minBallRadius..maxBallRadius).
     */
    private fun setupCustomizationButtons() {
        binding.btnChangeColor.setOnClickListener {
            colorIndex = (colorIndex + 1) % ballColors.size
            ballView.setBallColor(ballColors[colorIndex])
        }

        binding.btnIncreaseSize.setOnClickListener {
            currentBallRadius = (currentBallRadius + ballRadiusStep).coerceAtMost(maxBallRadius)
            ballView.setBallRadius(currentBallRadius)
        }

        binding.btnDecreaseSize.setOnClickListener {
            currentBallRadius = (currentBallRadius - ballRadiusStep).coerceAtLeast(minBallRadius)
            ballView.setBallRadius(currentBallRadius)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometerManager.start()
    }

    override fun onPause() {
        super.onPause()
        accelerometerManager.stop()
    }
}

