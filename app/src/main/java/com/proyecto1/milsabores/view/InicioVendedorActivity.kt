package com.proyecto1.milsabores.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R

class InicioVendedorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_inicio_vendedor)

        val btnProductos = findViewById<Button>(R.id.btnProductosV)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // ✅ Ver productos
        btnProductos.setOnClickListener {
            startActivity(Intent(this, ListaProductosActivity::class.java))
        }

        // ✅ Cerrar sesión
        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


}
