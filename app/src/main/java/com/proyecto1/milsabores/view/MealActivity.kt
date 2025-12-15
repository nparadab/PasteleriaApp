package com.proyecto1.milsabores.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.viewmodel.MealViewModel

class MealActivity : AppCompatActivity() {

    private val viewModel: MealViewModel by viewModels()
    private lateinit var adapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_meal)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMeals)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnLogout = findViewById<Button>(R.id.btnLogoutPastelero)

        adapter = MealAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)

        searchButton.startAnimation(bounce)
        btnVolver.startAnimation(bounce)
        btnLogout.startAnimation(bounce)

        viewModel.recetas.observe(this, Observer { meals ->
            adapter.updateData(meals)
        })

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                viewModel.buscar(query)
            }
        }

        // ✅ Botón Volver
        btnVolver.setOnClickListener {
            finish()
        }

        // ✅ Botón Cerrar Sesión (CORREGIDO)
        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE) // ✅ CORREGIDO
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
