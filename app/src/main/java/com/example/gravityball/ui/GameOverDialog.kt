package com.example.gravityball.ui

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.gravityball.R

/**
 * GameOverDialog: encapsula la creación y muestra del diálogo de Game Over.
 * Responsabilidad única: mostrar el puntaje final y el botón para reiniciar.
 * No conoce nada de BallView ni de la lógica del juego.
 */
object GameOverDialog {

    /**
     * Muestra el diálogo de Game Over.
     *
     * @param context contexto de la Activity que lo invoca.
     * @param score puntaje final a mostrar.
     * @param onRestart callback que se ejecuta al presionar "Jugar de nuevo".
     */
    fun show(context: Context, score: Int, onRestart: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.game_over_title))
            .setMessage("Puntaje: $score")
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.btn_restart)) { dialog, _ ->
                dialog.dismiss()
                onRestart()
            }
            .show()
    }
}
