package com.proyecto1.milsabores.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.model.LoginRequest
import com.proyecto1.milsabores.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvOlvidaste: TextView

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

        // ✅ Inicializar ApiService para que pueda leer SharedPreferences
        ApiService.init(this)

        // ✅ AUTO-LOGIN (usamos "auth" para coincidir con ApiService)
        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val tokenGuardado = prefs.getString("token", null)
        val rolGuardado = prefs.getString("role", null)

        if (tokenGuardado != null && rolGuardado != null) {
            when (rolGuardado.lowercase()) {
                "admin" -> {
                    startActivity(Intent(this, InicioActivity::class.java))
                    finish()
                }
                "vendedor" -> {
                    startActivity(Intent(this, InicioVendedorActivity::class.java))
                    finish()
                }
                "cliente" -> {
                    startActivity(Intent(this, ListaProductosClienteActivity::class.java))
                    finish()
                }
                "pastelero" -> {
                    startActivity(Intent(this, MealActivity::class.java))
                    finish()
                }
            }
        }

        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvOlvidaste = findViewById(R.id.tvOlvidaste)

        tvOlvidaste.setOnClickListener {
            vibrar()
            startActivity(Intent(this, RecuperarPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
            vibrar()

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            iniciarSesion(email, password)
        }
    }

    private fun iniciarSesion(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.userApi.login(LoginRequest(email, password))

                runOnUiThread {
                    if (response.isSuccessful && response.body() != null) {

                        val token = response.body()!!.token

                        Log.d("LOGIN", "Token recibido: $token")

                        val payload = token.split(".")[1]
                        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
                        val json = JSONObject(String(decodedBytes))

                        val nombre = json.optString("nombre", "Usuario")
                        val rol = json.optString("rol", "invitado").lowercase()
                        val correo = json.optString("sub", email)

                        // ✅ Guardar sesión en "auth" (coincide con ApiService)
                        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putString("token", token)
                            .putString("role", rol)
                            .putString("email", correo)
                            .putString("nombre", nombre)
                            .apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido $nombre",
                            Toast.LENGTH_SHORT
                        ).show()

                        when (rol) {
                            "admin" -> redirigir(InicioActivity::class.java)
                            "vendedor" -> redirigir(InicioVendedorActivity::class.java)
                            "cliente" -> redirigir(ListaProductosClienteActivity::class.java)
                            "pastelero" -> redirigir(MealActivity::class.java)
                            else -> Toast.makeText(this@LoginActivity, "Rol desconocido: $rol", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun redirigir(destino: Class<*>) {
        val intent = Intent(this, destino)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
