package com.example.gravityball.game

/**
 * GameManager: responsable único de la LÓGICA DE NEGOCIO del juego.
 *
 * No sabe nada de Canvas, View, Paint ni dibujado: eso sigue viviendo en
 * BallView. GameManager solo se encarga de:
 * - Llevar el puntaje.
 * - Saber si el juego está "jugando" o en "game over".
 * - Reiniciar su propio estado cuando el juego vuelve a empezar.
 *
 * Separar esto de BallView cumple con el principio de responsabilidad única:
 * BallView dibuja, GameManager decide reglas de negocio (puntaje/estado).
 */
class GameManager {

    /** Puntaje actual. Solo se puede modificar desde dentro de la clase. */
    var score: Int = 0
        private set

    /** true mientras el juego está activo; false luego de un Game Over. */
    var isPlaying: Boolean = true
        private set

    /**
     * Suma un punto al puntaje. Se usa junto con un temporizador de 1
     * segundo en BallView, de forma que el puntaje crece según el
     * tiempo de supervivencia del jugador.
     *
     * No hace nada si el juego ya terminó, para evitar que el puntaje
     * siga subiendo después de un Game Over.
     */
    fun addSurvivalPoint() {
        if (isPlaying) {
            score++
        }
    }

    /**
     * Marca el juego como terminado. Se llama cuando BallView detecta
     * una colisión entre la bolita y un obstáculo.
     */
    fun endGame() {
        isPlaying = false
    }

    /**
     * Reinicia el estado del juego para una nueva partida:
     * puntaje a 0 y estado nuevamente "jugando".
     */
    fun reset() {
        score = 0
        isPlaying = true
    }
}
