package com.proyecto1.milsabores.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // Oculta la barra superior con el nombre de la app

        setContentView(R.layout.inicio)

        val logo = findViewById<ImageView>(R.id.logoPasteleria)
        val btnIngresar = findViewById<Button>(R.id.btnIngresarProducto)
        val btnVer = findViewById<Button>(R.id.btnVerProductos)

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        logo.startAnimation(bounce)

        btnIngresar.setOnClickListener {
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }

        btnVer.setOnClickListener {
            startActivity(Intent(this, ListaProductosActivity::class.java))
        }
    }
}
