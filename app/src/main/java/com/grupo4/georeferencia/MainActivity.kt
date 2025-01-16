package com.grupo4.georeferencia

import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null

    private lateinit var tvX: TextView
    private lateinit var tvY: TextView
    private lateinit var tvZ: TextView

    private lateinit var pbX: ProgressBar
    private lateinit var pbY: ProgressBar
    private lateinit var pbZ: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia a los elementos de la tabla
        tvX = findViewById(R.id.tv_x)
        tvY = findViewById(R.id.tv_y)
        tvZ = findViewById(R.id.tv_z)

        // Referencia a las barras de progreso
        pbX = findViewById(R.id.pb_x)
        pbY = findViewById(R.id.pb_y)
        pbZ = findViewById(R.id.pb_z)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        if (gravitySensor == null) {
            Toast.makeText(this, "Sensor de gravedad no disponible", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        gravitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GRAVITY) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Actualiza los textos de la tabla
            tvX.text = "X: %.2f".format(x)
            tvY.text = "Y: %.2f".format(y)
            tvZ.text = "Z: %.2f".format(z)

            // Actualiza las barras de progreso
            pbX.progress = (x + 10).toInt() * 5 // Ajusta valores al rango [0, 100]
            pbY.progress = (y + 10).toInt() * 5
            pbZ.progress = (z + 10).toInt() * 5
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No implementado
    }
}
