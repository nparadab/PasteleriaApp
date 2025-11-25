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
import com.proyecto1.milsabores.model.ProductoDTO
import com.proyecto1.milsabores.viewmodel.ProductoViewModel

class ListaProductosActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val productoViewModel: ProductoViewModel by viewModels()

    private var productoSeleccionado: ProductoDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_productos)
        supportActionBar?.hide()

        // Configurar RecyclerView
        recycler = findViewById(R.id.recyclerProductos)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter { producto ->
            productoSeleccionado = producto
            Toast.makeText(this, "Seleccionado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
        }
        recycler.adapter = adapter

        // Observa productos
        productoViewModel.productos.observe(this) { lista ->
            adapter.actualizarLista(lista.sortedByDescending { it.stock })
        }
        productoViewModel.cargarProductos()

        // Botón volver
        findViewById<Button>(R.id.btnVolverInicio).setOnClickListener {
            startActivity(Intent(this, InicioActivity::class.java))
            finish()
        }

        // Spinner dinámico de categorías desde backend
        val spinnerFiltro = findViewById<Spinner>(R.id.spinnerFiltro)
        val checkStock = findViewById<CheckBox>(R.id.checkStock)

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

        // Botones de acción
        findViewById<Button>(R.id.btnAgregarProducto).setOnClickListener {
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }

        findViewById<Button>(R.id.btnEditarProducto).setOnClickListener {
            productoSeleccionado?.let {
                val intent = Intent(this, FormularioActivity::class.java)
                intent.putExtra("producto", it) // ProductoDTO implementa Serializable
                startActivity(intent)
            } ?: Toast.makeText(this, "Selecciona un producto primero", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnEliminarProducto).setOnClickListener {
            productoSeleccionado?.let {
                productoViewModel.eliminarProducto(it.id ?: return@let)
                Toast.makeText(this, "Producto eliminado: ${it.nombre}", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(this, "Selecciona un producto primero", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        productoViewModel.cargarProductos()
        productoViewModel.cargarCategorias()
    }
}
