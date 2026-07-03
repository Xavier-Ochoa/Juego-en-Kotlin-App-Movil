package com.example.gravityball.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * AccelerometerManager: encapsula TODO el manejo del sensor acelerómetro.
 * Responsabilidad única: registrar/desregistrar el sensor y notificar los
 * valores X, Y crudos a través de un callback simple.
 *
 * MainActivity solo crea esta clase y le dice cuándo iniciar/detener;
 * no conoce los detalles de SensorManager ni de SensorEventListener.
 */
class AccelerometerManager(
    context: Context,
    private val onAccelerometerChanged: (x: Float, y: Float) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    /**
     * Registra el listener del acelerómetro. Debe llamarse en onResume()
     * de la Activity para no consumir batería cuando la app está en segundo plano.
     */
    fun start() {
        accelerometer?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    /**
     * Libera el listener del acelerómetro. Debe llamarse en onPause()
     * para evitar fugas de recursos.
     */
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            onAccelerometerChanged(x, y)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se requiere manejar cambios de precisión para este juego.
    }
}
