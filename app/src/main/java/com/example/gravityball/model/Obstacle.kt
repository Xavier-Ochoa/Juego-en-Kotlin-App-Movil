package com.example.gravityball.model

import android.graphics.Color

/**
 * Representa un obstáculo rectangular del juego.
 * Se dibuja con Canvas.drawRect(). La colisión con la bolita se
 * calculará en la Fase 5 usando left/top/right/bottom.
 */
data class Obstacle(
    var left: Float,
    var top: Float,
    var width: Float,
    var height: Float,
    var color: Int = Color.parseColor("#FF5252")
) {
    val right: Float get() = left + width
    val bottom: Float get() = top + height
}
