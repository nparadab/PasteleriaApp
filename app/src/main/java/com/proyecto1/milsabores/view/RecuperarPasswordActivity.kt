package com.proyecto1.milsabores.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RecuperarPasswordActivity : AppCompatActivity() {

    private lateinit var etEmailRecuperar: EditText
    private lateinit var btnRecuperar: Button
    private lateinit var btnVolverLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password)
        supportActionBar?.hide()

        etEmailRecuperar = findViewById(R.id.etEmailRecuperar)
        btnRecuperar = findViewById(R.id.btnRecuperar)
        btnVolverLogin = findViewById(R.id.btnVolverLogin)

        btnRecuperar.setOnClickListener {
            val email = etEmailRecuperar.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            recuperarPassword(email)
        }

        btnVolverLogin.setOnClickListener {
            finish()
        }
    }

    private fun recuperarPassword(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.userApi.recuperarPassword(mapOf("email" to email))

                runOnUiThread {

                    if (response.isSuccessful && response.body() != null) {

                        val json = JSONObject(response.body()!!.string())
                        val tempPass = json.optString("passwordTemporal", "N/A")

                        Toast.makeText(
                            this@RecuperarPasswordActivity,
                            "Tu contraseña temporal es: $tempPass",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        val errorJson = response.errorBody()?.string()
                        val mensaje = try {
                            JSONObject(errorJson ?: "").optString("mensaje", "Correo no encontrado")
                        } catch (e: Exception) {
                            "Correo no encontrado"
                        }

                        Toast.makeText(
                            this@RecuperarPasswordActivity,
                            mensaje,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@RecuperarPasswordActivity,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
