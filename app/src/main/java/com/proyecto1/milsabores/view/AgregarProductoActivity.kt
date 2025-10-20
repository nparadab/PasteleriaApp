package com.proyecto1.milsabores.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.database.ProductoRepository
import com.proyecto1.milsabores.databinding.ActivityAgregarProductoBinding
import java.text.SimpleDateFormat
import java.util.*

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private lateinit var adapter: ProductoAdapter
    private lateinit var productoRepository: ProductoRepository
    private var imagenSeleccionada: Uri? = null
    private lateinit var fechaCreacion: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        productoRepository = ProductoRepository(this)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.root.startAnimation(fadeIn)

        val categorias = listOf("Pastel", "Torta", "Cupcake", "Galleta")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategoria.adapter = adapterSpinner

        adapter = ProductoAdapter()
        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductos.adapter = adapter

        fechaCreacion = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        binding.tvFechaCreacion.text = "Fecha de creación: $fechaCreacion"

        val selectorImagen = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imagenSeleccionada = uri
            binding.ivPreview.setImageURI(uri)
        }

        binding.btnSeleccionarImagen.setOnClickListener {
            selectorImagen.launch("image/*")
        }

        binding.btnAgregar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.scale_success)
            binding.btnAgregar.startAnimation(anim)

            val nombre = binding.etNombreProducto.text.toString().trim()
            val precioTexto = binding.etPrecioProducto.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()
            val stockTexto = binding.etStock.text.toString().trim()
            val categoria = binding.spCategoria.selectedItem?.toString() ?: "Sin categoría"

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

            val imagenUri = imagenSeleccionada?.toString()
            val exito = productoRepository.insertar(nombre, precio, descripcion, categoria, stock, fechaCreacion, imagenUri)

            if (exito) {
                mostrarDialogoConfirmacion()

                binding.etNombreProducto.text.clear()
                binding.etPrecioProducto.text.clear()
                binding.etDescripcion.text.clear()
                binding.etStock.text.clear()
                binding.spCategoria.setSelection(0)
                binding.ivPreview.setImageDrawable(null)
                imagenSeleccionada = null

                val ultimo = productoRepository.obtenerTodos().lastOrNull()
                if (ultimo != null) {
                    adapter.actualizarLista(listOf(ultimo))
                }
            } else {
                mostrarDialogoError()
            }
        }

        binding.btnVolverInicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }

        val ultimo = productoRepository.obtenerTodos().lastOrNull()
        if (ultimo != null) {
            adapter.actualizarLista(listOf(ultimo))
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_producto_agregado)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun mostrarDialogoError() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_error_producto)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}
