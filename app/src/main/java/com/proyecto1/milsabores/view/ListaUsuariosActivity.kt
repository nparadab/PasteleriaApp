package com.proyecto1.milsabores.view

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.adapter.UsuarioAdapter
import com.proyecto1.milsabores.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var rvUsuarios: RecyclerView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)
        supportActionBar?.hide()

        rvUsuarios = findViewById(R.id.rvUsuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(this)

        btnVolver = findViewById(R.id.btnVolver)

        btnVolver.setOnClickListener {
            vibrar()
            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        cargarUsuarios()
    }

    private fun vibrar() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(80)
            }
        }
    }

    private fun cargarUsuarios() {

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""

        Log.d("TOKEN", "Token leído: $token")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.usuarioApi.getUsuarios()

                runOnUiThread {

                    Log.d("USUARIOS", "Código HTTP: ${response.code()}")
                    Log.d("USUARIOS", "Body: ${response.body()}")
                    Log.d("USUARIOS", "ErrorBody: ${response.errorBody()?.string()}")

                    if (response.isSuccessful && response.body() != null) {
                        rvUsuarios.adapter = UsuarioAdapter(response.body()!!)
                    } else {
                        Toast.makeText(
                            this@ListaUsuariosActivity,
                            "Error al cargar usuarios: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ListaUsuariosActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
