package com.proyecto1.milsabores.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
import android.view.View
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
    private lateinit var btnVerUsuarios: Button
    private lateinit var btnVerProductos: Button
    private lateinit var btnClima: Button
    private lateinit var btnRecetas: Button
    private lateinit var btnLogout: Button

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
            imgAnimada.setImageResource(imagenes[indiceActual])
            imgAnimada.startAnimation(fade)
            indiceActual = (indiceActual + 1) % imagenes.size
            handler.postDelayed(this, intervalo)
        }
    }

    private fun vibrar() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(80)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.inicio)

        titulo = findViewById(R.id.tituloPasteleria)
        eslogan = findViewById(R.id.tvDescripcion)
        imgAnimada = findViewById(R.id.imgAnimadaInicio)
        btnIngresar = findViewById(R.id.btnIngresarProducto)
        btnVerUsuarios = findViewById(R.id.btnVerUsuarios)
        btnVerProductos = findViewById(R.id.btnVerProductos)
        btnClima = findViewById(R.id.btnClima)
        btnRecetas = findViewById(R.id.btnRecetas)
        btnLogout = findViewById(R.id.btnLogout)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)

        titulo.startAnimation(fadeIn)
        eslogan.startAnimation(slideIn)
        btnIngresar.startAnimation(bounce)
        btnVerUsuarios.startAnimation(bounce)
        btnVerProductos.startAnimation(bounce)
        btnClima.startAnimation(bounce)
        btnRecetas.startAnimation(bounce)
        btnLogout.startAnimation(bounce)

        handler.post(cambiarImagen)

        // ✅ Control de roles (CORREGIDO: ahora usa "auth")
        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val rol = prefs.getString("role", "")?.lowercase()

        // ✅ ADMIN ve usuarios
        if (rol == "admin") {
            btnVerUsuarios.visibility = View.VISIBLE
            btnVerUsuarios.setOnClickListener {
                vibrar()
                startActivity(Intent(this, ListaUsuariosActivity::class.java))
            }
        } else {
            btnVerUsuarios.visibility = View.GONE
        }

        // ✅ Todos ven productos
        btnVerProductos.setOnClickListener {
            vibrar()
            startActivity(Intent(this, ListaProductosActivity::class.java))
        }

        btnIngresar.setOnClickListener {
            vibrar()
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }

        btnClima.setOnClickListener {
            vibrar()
            startActivity(Intent(this, ClimaActivity::class.java))
        }

        btnRecetas.setOnClickListener {
            vibrar()
            startActivity(Intent(this, MealActivity::class.java))
        }

        // ✅ Cerrar sesión (CORREGIDO)
        btnLogout.setOnClickListener {
            vibrar()
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(cambiarImagen)
    }
}
