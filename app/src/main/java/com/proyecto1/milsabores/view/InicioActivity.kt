package com.proyecto1.milsabores.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R

class InicioActivity : AppCompatActivity() {

    private lateinit var imgAnimada: ImageView
    private val imagenes = listOf(
        R.drawable.inicio_1,
        R.drawable.inicio_2,
        R.drawable.inicio_3
    )
    private var indiceActual = 0
    private val handler = Handler(Looper.getMainLooper())
    private val intervalo = 4000L // 4 segundos

    private val cambiarImagen = object : Runnable {
        override fun run() {
            val fade = AnimationUtils.loadAnimation(this@InicioActivity, android.R.anim.fade_in)
            imgAnimada.startAnimation(fade)
            imgAnimada.setImageResource(imagenes[indiceActual])
            indiceActual = (indiceActual + 1) % imagenes.size
            handler.postDelayed(this, intervalo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.inicio)

        val logo = findViewById<ImageView>(R.id.logoPasteleria)
        val btnIngresar = findViewById<Button>(R.id.btnIngresarProducto)
        val btnVer = findViewById<Button>(R.id.btnVerProductos)
        imgAnimada = findViewById(R.id.imgAnimadaInicio)

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        logo.startAnimation(bounce)

        handler.post(cambiarImagen)

        btnIngresar.setOnClickListener {
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }

        btnVer.setOnClickListener {
            startActivity(Intent(this, ListaProductosActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(cambiarImagen)
    }
}
