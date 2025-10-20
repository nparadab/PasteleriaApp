package com.proyecto1.milsabores.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R

class InicioActivity : AppCompatActivity() {

    private lateinit var imgAnimada: ImageView
    private lateinit var titulo: TextView
    private lateinit var eslogan: TextView
    private lateinit var btnIngresar: Button
    private lateinit var btnVer: Button

    private val imagenes = listOf(
        R.drawable.inicio_1,
        R.drawable.inicio_2,
        R.drawable.inicio_3
    )
    private var indiceActual = 0
    private val handler = Handler(Looper.getMainLooper())
    private val intervalo = 4000L

    private val cambiarImagen = object : Runnable {
        override fun run() {
            val fade = AnimationUtils.loadAnimation(this@InicioActivity, R.anim.fade_in)
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

        // Referencias visuales
        titulo = findViewById(R.id.tituloPasteleria)
        eslogan = findViewById(R.id.tvDescripcion)
        imgAnimada = findViewById(R.id.imgAnimadaInicio)
        btnIngresar = findViewById(R.id.btnIngresarProducto)
        btnVer = findViewById(R.id.btnVerProductos)

        // Animaciones
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)

        titulo.startAnimation(fadeIn)
        eslogan.startAnimation(slideIn)
        btnIngresar.startAnimation(bounce)
        btnVer.startAnimation(bounce)

        // Iniciar rotación de imágenes
        handler.post(cambiarImagen)

        // Navegación
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
