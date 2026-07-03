package com.example.gravityball.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gravityball.MainActivity
import com.example.gravityball.databinding.ActivityHomeBinding

/**
 * HomeActivity: pantalla de inicio del juego.
 * Responsabilidad única: mostrar el título y el botón "Jugar",
 * y navegar hacia MainActivity (la pantalla del juego) al pulsarlo.
 * No contiene ninguna lógica de juego.
 */
class HomeActivity : AppCompatActivity() {

    // ViewBinding: nos da acceso seguro a las vistas del XML sin usar findViewById
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflamos el layout usando ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el listener del botón Jugar
        binding.btnPlay.setOnClickListener {
            navigateToGame()
        }
    }

    /**
     * Navega desde la pantalla de inicio hacia la pantalla del juego (MainActivity).
     */
    private fun navigateToGame() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
