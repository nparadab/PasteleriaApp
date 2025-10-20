package com.proyecto1.milsabores.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.database.ProductoRepository

class ListaProductosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var productoRepository: ProductoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_productos)

        productoRepository = ProductoRepository(this)

        recycler = findViewById(R.id.recyclerProductos)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter()
        recycler.adapter = adapter

        cargarProductos()
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val productos = productoRepository.obtenerTodos()
        adapter.actualizarLista(productos)
    }
}
