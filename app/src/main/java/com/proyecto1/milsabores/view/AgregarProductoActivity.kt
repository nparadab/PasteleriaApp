package com.proyecto1.milsabores.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto1.milsabores.databinding.ActivityAgregarProductoBinding
import com.proyecto1.milsabores.viewmodel.ProductoViewModel
import java.text.SimpleDateFormat
import java.util.*

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private val viewModel: ProductoViewModel by viewModels()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // ✅ Cargar categorías en el Spinner
        val categorias = listOf("Pastel", "Torta", "Cupcake", "Galleta")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategoria.adapter = adapterSpinner

        // ✅ Configurar RecyclerView
        adapter = ProductoAdapter()
        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductos.adapter = adapter

        // ✅ Observar cambios en la lista de productos
        viewModel.productos.observe(this) {
            adapter.actualizarLista(it)
        }

        // ✅ Acción del botón para agregar producto desde formulario
        binding.btnAgregar.setOnClickListener {
            val nombre = binding.etNombreProducto.text.toString().trim()
            val precioTexto = binding.etPrecioProducto.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()
            val stockTexto = binding.etStock.text.toString().trim()
            val categoria = binding.spCategoria.selectedItem?.toString() ?: "Sin categoría"

            // Validaciones
            if (nombre.isEmpty()) {
                binding.etNombreProducto.error = "Ingresa un nombre"
                return@setOnClickListener
            }

            if (precioTexto.isEmpty()) {
                binding.etPrecioProducto.error = "Ingresa un precio"
                return@setOnClickListener
            }

            val precio = precioTexto.toDoubleOrNull()
            if (precio == null || precio <= 0) {
                binding.etPrecioProducto.error = "Precio inválido"
                return@setOnClickListener
            }

            if (descripcion.length < 10) {
                binding.etDescripcion.error = "Descripción muy corta"
                return@setOnClickListener
            }

            val stock = stockTexto.toIntOrNull()
            if (stock == null || stock < 0) {
                binding.etStock.error = "Stock inválido"
                return@setOnClickListener
            }

            val fechaCreacion = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val imagenUri: String? = null // Puedes agregar lógica para seleccionar imagen más adelante

            viewModel.agregarProducto(nombre, precio, descripcion, categoria, stock, fechaCreacion, imagenUri)

            // ✅ Mostrar confirmación
            Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show()

            // ✅ Limpiar los campos
            binding.etNombreProducto.text.clear()
            binding.etPrecioProducto.text.clear()
            binding.etDescripcion.text.clear()
            binding.etStock.text.clear()
            binding.spCategoria.setSelection(0)
        }

        // ✅ Cargar productos al iniciar
        viewModel.cargarProductos()
    }
}
