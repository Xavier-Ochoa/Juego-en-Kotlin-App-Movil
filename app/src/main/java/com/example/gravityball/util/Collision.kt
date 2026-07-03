package com.example.gravityball.util

import com.example.gravityball.model.Obstacle

/**
 * Collision: funciones puras para detectar colisiones en el juego.
 * Se mantienen aisladas del resto de la lógica (BallView) para que sean
 * fáciles de leer, probar y reutilizar. No dependen de Canvas ni de Android UI.
 */
object Collision {

    /**
     * Determina si un círculo (la bolita) intersecta con un rectángulo (un obstáculo).
     *
     * Algoritmo: se calcula el punto del rectángulo más cercano al centro del
     * círculo (recortando las coordenadas del círculo a los límites del
     * rectángulo), y se compara la distancia entre ese punto y el centro del
     * círculo contra el radio del círculo.
     */
    fun circleIntersectsRect(
        circleX: Float,
        circleY: Float,
        radius: Float,
        obstacle: Obstacle
    ): Boolean {
        val closestX = circleX.coerceIn(obstacle.left, obstacle.right)
        val closestY = circleY.coerceIn(obstacle.top, obstacle.bottom)

        val distanceX = circleX - closestX
        val distanceY = circleY - closestY
        val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)

        return distanceSquared <= radius * radius
    }
}
