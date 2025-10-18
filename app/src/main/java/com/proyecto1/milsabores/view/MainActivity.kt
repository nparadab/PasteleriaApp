package com.proyecto1.milsabores.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto1.milsabores.databinding.ActivityMainBinding
import com.proyecto1.milsabores.viewmodel.ProductoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        adapter = ProductoAdapter()
        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductos.adapter = adapter

        // Observar cambios en la lista de productos
        viewModel.productos.observe(this) {
            adapter.actualizarLista(it)
        }

        // Acción del botón para agregar producto
        binding.btnAgregar.setOnClickListener {
            viewModel.agregarProducto("Torta de Chocolate", 4500.0)
        }

        // Cargar productos al iniciar
        viewModel.cargarProductos()
    }
}
