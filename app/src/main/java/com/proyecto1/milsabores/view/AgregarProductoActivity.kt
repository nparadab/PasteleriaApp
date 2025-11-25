package com.proyecto1.milsabores.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.databinding.ActivityAgregarProductoBinding
import com.proyecto1.milsabores.model.ProductoDTO
import com.proyecto1.milsabores.viewmodel.ProductoViewModel
import com.proyecto1.milsabores.viewmodel.ProductoViewModel.ResultadoOperacion

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private val productoViewModel: ProductoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Animación inicial
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.root.startAnimation(fadeIn)

        // ✅ Poblar spinner dinámicamente desde backend
        productoViewModel.categorias.observe(this) { lista ->
            val nombres = lista.map { it.nombre }
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spCategoria.adapter = adapterSpinner
        }
        productoViewModel.cargarCategorias()

        // Botón agregar
        binding.btnAgregar.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.scale_success)
            binding.btnAgregar.startAnimation(anim)

            val nombre = binding.etNombreProducto.text.toString().trim()
            val precioTexto = binding.etPrecioProducto.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()
            val stockTexto = binding.etStock.text.toString().trim()

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

            // ✅ Obtener categoría seleccionada desde el LiveData
            val index = binding.spCategoria.selectedItemPosition
            val categoriaSeleccionada = productoViewModel.categorias.value?.get(index)

            if (categoriaSeleccionada == null) {
                mostrarDialogoError()
                return@setOnClickListener
            }

            // Crear ProductoDTO y enviarlo al ViewModel
            val producto = ProductoDTO(
                nombre = nombre,
                precio = precio,
                descripcion = descripcion,
                stock = stock,
                categoria = categoriaSeleccionada
            )

            productoViewModel.agregarProducto(producto)
        }

        // Botón volver
        binding.btnVolverInicio.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }

        // Observamos resultado desde el ViewModel
        productoViewModel.resultado.observe(this) { estado ->
            when (estado) {
                ResultadoOperacion.EXITO -> {
                    mostrarDialogoConfirmacion()
                    limpiarFormulario()
                }
                ResultadoOperacion.ERROR -> {
                    mostrarDialogoError()
                }
            }
        }
    }

    private fun limpiarFormulario() {
        binding.etNombreProducto.text.clear()
        binding.etPrecioProducto.text.clear()
        binding.etDescripcion.text.clear()
        binding.etStock.text.clear()
        binding.spCategoria.setSelection(0)
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
