package com.proyecto1.milsabores.view

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.viewmodel.ClimaViewModel

class ClimaActivity : AppCompatActivity() {

    private val viewModel: ClimaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_clima)

        val climaText = findViewById<TextView>(R.id.climaText)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)

        climaText.startAnimation(fadeIn)
        btnVolver.startAnimation(bounce)

        viewModel.clima.observe(this) { weather ->
            weather?.let {
                climaText.text = "Temp: ${it.temperature}°C\n" +
                        "Viento: ${it.windspeed} km/h\n" +
                        "Código clima: ${it.weathercode}"
            }
        }

        viewModel.cargarClima(-33.45, -70.66)

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
