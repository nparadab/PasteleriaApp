package com.proyecto1.milsabores.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.database.ProductoRepository
import android.widget.Button
import android.content.Intent
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.view.View
import android.widget.CheckBox

class ListaProductosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var productoRepository: ProductoRepository

    private fun filtrarProductos(categoria: String, soloConStock: Boolean) {
        val productos = productoRepository.obtenerTodos()

        val filtrados = productos.filter { producto ->
            val coincideCategoria = categoria == "Todos" || producto.categoria == categoria
            val tieneStock = !soloConStock || producto.stock > 0
            coincideCategoria && tieneStock
        }.sortedByDescending { it.stock } // 🔽 También ordena los filtrados

        adapter.actualizarLista(filtrados)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_productos)

        productoRepository = ProductoRepository(this)

        recycler = findViewById(R.id.recyclerProductos)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter()
        recycler.adapter = adapter

        cargarProductos()

        supportActionBar?.hide()

        val btnVolver = findViewById<Button>(R.id.btnVolverInicio)
        btnVolver.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val spinnerFiltro = findViewById<Spinner>(R.id.spinnerFiltro)
        val checkStock = findViewById<CheckBox>(R.id.checkStock)

        checkStock.setOnCheckedChangeListener { _, isChecked ->
            val categoria = spinnerFiltro.selectedItem.toString()
            filtrarProductos(categoria, isChecked)
        }

        val adapterSpinner = ArrayAdapter.createFromResource(
            this,
            R.array.categorias,
            android.R.layout.simple_spinner_item
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltro.adapter = adapterSpinner

        spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val categoria = parent.getItemAtPosition(position).toString()
                val soloConStock = checkStock.isChecked
                filtrarProductos(categoria, soloConStock)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    // ✅ Función actualizada para ordenar por stock
    private fun cargarProductos() {
        val productos = productoRepository.obtenerTodos()
            .sortedByDescending { it.stock } // 🔽 Ordena de mayor a menor stock
        adapter.actualizarLista(productos)
    }
}
