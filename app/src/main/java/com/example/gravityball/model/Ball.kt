package com.example.gravityball.model

import android.graphics.Color

/**
 * Representa la bolita del juego.
 * Contiene únicamente el ESTADO de la bolita: posición, radio y color.
 * No contiene lógica de dibujado (eso vive en BallView) ni lógica de
 * movimiento avanzada (eso se agrega en la Fase 3 con el acelerómetro).
 */
data class Ball(
    var x: Float,
    var y: Float,
    var radius: Float = DEFAULT_RADIUS,
    var color: Int = DEFAULT_COLOR
) {
    companion object {
        const val DEFAULT_RADIUS = 40f
        val DEFAULT_COLOR = Color.parseColor("#00E676")
    }
}
