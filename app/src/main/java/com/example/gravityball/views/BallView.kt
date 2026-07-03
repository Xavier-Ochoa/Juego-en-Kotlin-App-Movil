package com.example.gravityball.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.example.gravityball.R
import com.example.gravityball.game.GameManager
import com.example.gravityball.model.Ball
import com.example.gravityball.model.Obstacle
import com.example.gravityball.util.Collision

/**
 * BallView: View personalizada donde vive TODA la lógica gráfica del juego,
 * según la arquitectura del taller (MainActivity -> SensorManager -> BallView -> Canvas).
 *
 * Fase 6: el puntaje y el estado jugando/game-over ya no se guardan como
 * variables sueltas: se delegan por completo a GameManager (responsabilidad
 * única: lógica de negocio del juego). BallView se limita a dibujar y a
 * avisarle a GameManager cuándo sumar puntos o terminar la partida.
 */
class BallView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Estado de la bolita. Posición inicial en (0,0); se recalcula cuando
    // conocemos el tamaño real de la View en onSizeChanged().
    private var ball = Ball(x = 0f, y = 0f)

    // Lista de obstáculos, se generan en setupObstacles() cuando ya conocemos
    // el tamaño de la pantalla.
    private val obstacles = mutableListOf<Obstacle>()

    // Factor de sensibilidad: controla qué tan rápido se mueve la bolita
    // por cada unidad de aceleración detectada.
    private val movementSensitivity = 12f

    // GameManager concentra la lógica de negocio del juego: puntaje y
    // estado jugando/game-over. BallView ya no guarda esas variables sueltas.
    private val gameManager = GameManager()

    // Callback que MainActivity registra para enterarse cuando termina el juego.
    private var onGameOverListener: ((Int) -> Unit)? = null

    // Paint reutilizable para la bolita.
    private val ballPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Paint reutilizable para los obstáculos.
    private val obstaclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Paint reutilizable para el fondo.
    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#121212")
        style = Paint.Style.FILL
    }

    // Paint reutilizable para el texto de coordenadas (X:, Y:).
    private val coordinatesPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 42f
    }

    // Paint reutilizable para el texto de instrucciones.
    private val instructionsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }

    // Paint reutilizable para el texto del puntaje.
    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 48f
        textAlign = Paint.Align.RIGHT
        isFakeBoldText = true
    }

    // Texto de instrucciones cargado una sola vez desde strings.xml.
    private val instructionsText: String by lazy {
        context.getString(R.string.instructions)
    }

    // Handler para el "reloj" del puntaje: cada 1 segundo, mientras el
    // juego esté activo, le pide a GameManager que sume un punto por
    // tiempo de supervivencia.
    private val scoreHandler = Handler(Looper.getMainLooper())
    private val scoreTickIntervalMs = 1000L
    private val scoreTickRunnable = object : Runnable {
        override fun run() {
            if (gameManager.isPlaying) {
                gameManager.addSurvivalPoint()
                invalidate()
            }
            scoreHandler.postDelayed(this, scoreTickIntervalMs)
        }
    }

    /**
     * Se llama automáticamente cuando la View obtiene su tamaño real en pantalla.
     * Aprovechamos este momento para centrar la bolita y generar los obstáculos,
     * ya que en el constructor el ancho/alto de la View todavía es 0.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ball.x = w / 2f
        ball.y = h / 2f
        setupObstacles(w, h)
    }

    /**
     * Crea los obstáculos del juego en posiciones relativas al tamaño de
     * pantalla, para que se vean bien en cualquier dispositivo.
     */
    private fun setupObstacles(w: Int, h: Int) {
        obstacles.clear()
        obstacles.add(Obstacle(left = w * 0.10f, top = h * 0.25f, width = w * 0.35f, height = 40f))
        obstacles.add(Obstacle(left = w * 0.55f, top = h * 0.45f, width = w * 0.35f, height = 40f))
        obstacles.add(Obstacle(left = w * 0.10f, top = h * 0.70f, width = w * 0.40f, height = 40f))
    }

    /**
     * Se llama cuando la View queda anclada a la ventana (pantalla visible).
     * Aprovechamos este momento para arrancar el "reloj" del puntaje.
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        scoreHandler.post(scoreTickRunnable)
    }

    /**
     * Se llama cuando la View se desancla de la ventana (Activity destruida
     * o en background). Detenemos el reloj del puntaje para no dejar
     * callbacks pendientes y evitar fugas de memoria.
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scoreHandler.removeCallbacks(scoreTickRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawObstacles(canvas)
        drawBall(canvas)
        drawCoordinates(canvas)
        drawScore(canvas)
        drawInstructions(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
    }

    private fun drawBall(canvas: Canvas) {
        ballPaint.color = ball.color
        canvas.drawCircle(ball.x, ball.y, ball.radius, ballPaint)
    }

    private fun drawObstacles(canvas: Canvas) {
        for (obstacle in obstacles) {
            obstaclePaint.color = obstacle.color
            canvas.drawRect(obstacle.left, obstacle.top, obstacle.right, obstacle.bottom, obstaclePaint)
        }
    }

    /**
     * Muestra las coordenadas actuales de la bolita en la esquina superior
     * izquierda de la pantalla, como pide el reto del taller.
     */
    private fun drawCoordinates(canvas: Canvas) {
        canvas.drawText("X: ${ball.x.toInt()}", 40f, 80f, coordinatesPaint)
        canvas.drawText("Y: ${ball.y.toInt()}", 40f, 130f, coordinatesPaint)
    }

    /**
     * Muestra el texto de instrucciones en la parte inferior de la pantalla.
     */
    private fun drawInstructions(canvas: Canvas) {
        canvas.drawText(instructionsText, width / 2f, height - 60f, instructionsPaint)
    }

    /**
     * Muestra el puntaje actual (proveniente de GameManager) en la esquina
     * superior derecha de la pantalla.
     */
    private fun drawScore(canvas: Canvas) {
        canvas.drawText("${gameManager.score}", width - 40f, 80f, scorePaint)
    }

    /**
     * Recibe los valores crudos (x, y) del acelerómetro y mueve la bolita.
     * Si el juego ya terminó (Game Over), ignora el movimiento.
     *
     * Mapeo (igual al taller):
     * - Inclinar a la derecha  -> X aumenta.
     * - Inclinar a la izquierda -> X disminuye.
     * - Inclinar hacia abajo   -> Y aumenta.
     * - Inclinar hacia arriba  -> Y disminuye.
     */
    fun onAccelerometerMoved(sensorX: Float, sensorY: Float) {
        if (!gameManager.isPlaying) return

        ball.x -= sensorX * movementSensitivity
        ball.y += sensorY * movementSensitivity
        clampBallToScreenBounds()
        checkCollisions()
        invalidate()
    }

    /**
     * Corrige la posición de la bolita si se sale de cualquiera de los
     * 4 bordes de la pantalla: izquierdo, derecho, superior e inferior.
     */
    private fun clampBallToScreenBounds() {
        if (width == 0 || height == 0) return

        val minX = ball.radius
        val maxX = width - ball.radius
        val minY = ball.radius
        val maxY = height - ball.radius

        when {
            ball.x < minX -> ball.x = minX
            ball.x > maxX -> ball.x = maxX
        }
        when {
            ball.y < minY -> ball.y = minY
            ball.y > maxY -> ball.y = maxY
        }
    }

    /**
     * Revisa si la bolita chocó con algún obstáculo. Si hay colisión,
     * termina el juego y notifica el puntaje final.
     */
    private fun checkCollisions() {
        for (obstacle in obstacles) {
            if (Collision.circleIntersectsRect(ball.x, ball.y, ball.radius, obstacle)) {
                triggerGameOver()
                return
            }
        }
    }

    /**
     * Marca el juego como terminado (delegando en GameManager) y notifica
     * a quien esté escuchando (normalmente MainActivity) para que muestre
     * el diálogo de Game Over con el puntaje final.
     */
    private fun triggerGameOver() {
        gameManager.endGame()
        onGameOverListener?.invoke(gameManager.score)
    }

    /**
     * Registra el callback que se ejecuta cuando el juego termina.
     * Debe llamarse desde MainActivity.
     */
    fun setOnGameOverListener(listener: (Int) -> Unit) {
        onGameOverListener = listener
    }

    /**
     * Reinicia el juego por completo para una nueva partida:
     * - Posición de la bolita: vuelve al centro de la pantalla.
     * - Color y tamaño de la bolita: vuelven a sus valores por defecto.
     * - Obstáculos: se recrean desde cero.
     * - Puntaje y estado jugando/game-over: se reinician a través de GameManager.
     */
    fun resetGame() {
        gameManager.reset()
        ball.x = width / 2f
        ball.y = height / 2f
        ball.color = Ball.DEFAULT_COLOR
        ball.radius = Ball.DEFAULT_RADIUS
        setupObstacles(width, height)
        invalidate()
    }

    /**
     * Cambia el color de la bolita y redibuja.
     * Requisito del reto — nivel básico.
     */
    fun setBallColor(color: Int) {
        ball.color = color
        invalidate()
    }

    /**
     * Cambia el tamaño (radio) de la bolita y redibuja.
     * Requisito del reto — nivel básico.
     */
    fun setBallRadius(radius: Float) {
        ball.radius = radius
        invalidate()
    }
}
