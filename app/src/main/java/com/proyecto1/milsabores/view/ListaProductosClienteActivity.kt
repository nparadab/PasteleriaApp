package com.proyecto1.milsabores.view

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.viewmodel.ProductoViewModel

class ListaProductosClienteActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapterCliente
    private val productoViewModel: ProductoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_productos_cliente)
        supportActionBar?.hide()

        // Configurar RecyclerView
        recycler = findViewById(R.id.recyclerProductosCliente)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapterCliente(emptyList())
        recycler.adapter = adapter

        // Observa productos
        productoViewModel.productos.observe(this) { lista ->
            adapter.actualizarLista(lista.sortedByDescending { it.stock })
        }
        productoViewModel.cargarProductos()

        // ✅ Botón VOLVER
        val btnVolver = findViewById<Button>(R.id.btnVolverCliente)
        btnVolver.setOnClickListener {

            val prefs = getSharedPreferences("auth", MODE_PRIVATE) // ✅ CORREGIDO
            val rol = prefs.getString("role", "")?.lowercase()

            when (rol) {
                "admin" -> startActivity(Intent(this, InicioActivity::class.java))
                "vendedor" -> startActivity(Intent(this, InicioVendedorActivity::class.java))
                "pastelero" -> startActivity(Intent(this, MealActivity::class.java))
                "cliente" -> startActivity(Intent(this, ListaProductosClienteActivity::class.java))
                else -> startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()
        }

        // ✅ Botón CERRAR SESIÓN (CORREGIDO)
        val btnLogout = findViewById<Button>(R.id.btnLogoutCliente)
        btnLogout.setOnClickListener {
            val prefs = getSharedPreferences("auth", MODE_PRIVATE) // ✅ CORREGIDO
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Spinner dinámico de categorías desde backend
        val spinnerFiltro = findViewById<Spinner>(R.id.spinnerFiltroCliente)
        val checkStock = findViewById<CheckBox>(R.id.checkStockCliente)

        productoViewModel.categorias.observe(this) { listaCategorias ->
            val nombres = mutableListOf("Todos")
            nombres.addAll(listaCategorias.map { it.nombre })

            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFiltro.adapter = adapterSpinner
        }
        productoViewModel.cargarCategorias()

        // Función de filtrado
        val aplicarFiltro = {
            val categoriaSeleccionada = spinnerFiltro.selectedItem?.toString() ?: "Todos"
            val soloConStock = checkStock.isChecked
            val productos = productoViewModel.productos.value ?: emptyList()

            val filtrados = productos.filter { producto ->
                val coincideCategoria = categoriaSeleccionada == "Todos" ||
                        producto.categoria.nombre == categoriaSeleccionada
                val tieneStock = !soloConStock || producto.stock > 0
                coincideCategoria && tieneStock
            }.sortedByDescending { it.stock }

            adapter.actualizarLista(filtrados)
        }

        checkStock.setOnCheckedChangeListener { _, _ -> aplicarFiltro() }
        spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                aplicarFiltro()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onResume() {
        super.onResume()
        productoViewModel.cargarProductos()
        productoViewModel.cargarCategorias()
    }
}
