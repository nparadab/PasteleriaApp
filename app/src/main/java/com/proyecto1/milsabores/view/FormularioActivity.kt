package com.proyecto1.milsabores.view

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.proyecto1.milsabores.R
import com.proyecto1.milsabores.databinding.ActivityFormularioBinding
import com.proyecto1.milsabores.model.CategoriaDTO
import com.proyecto1.milsabores.model.ProductoDTO
import com.proyecto1.milsabores.viewmodel.ProductoViewModel
import com.proyecto1.milsabores.viewmodel.ProductoViewModel.ResultadoOperacion

class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private val productoViewModel: ProductoViewModel by viewModels()
    private var producto: ProductoDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // ✅ Recibir producto desde el Intent
        producto = intent.getSerializableExtra("producto") as? ProductoDTO

        // ✅ Cargar datos en el formulario
        producto?.let {
            binding.etNombreProducto.setText(it.nombre)
            binding.etPrecioProducto.setText(it.precio.toString())
            binding.etDescripcion.setText(it.descripcion)
            binding.etStock.setText(it.stock.toString())

            val categorias = listOf("Pastel", "Torta", "Cupcake", "Galleta")
            val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spCategoria.adapter = adapterSpinner

            val index = categorias.indexOf(it.categoria.nombre)
            if (index >= 0) binding.spCategoria.setSelection(index)
        }

        // ✅ Botón guardar cambios
        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreProducto.text.toString().trim()
            val precio = binding.etPrecioProducto.text.toString().toDoubleOrNull() ?: 0.0
            val descripcion = binding.etDescripcion.text.toString().trim()
            val stock = binding.etStock.text.toString().toIntOrNull() ?: 0
            val categoriaNombre = binding.spCategoria.selectedItem?.toString() ?: "Sin categoría"

            producto?.id?.let { id ->
                val categoria = CategoriaDTO(id = producto!!.categoria.id, nombre = categoriaNombre)

                val productoEditado = ProductoDTO(
                    id = id,
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion,
                    stock = stock,
                    categoria = categoria
                )

                productoViewModel.editarProducto(productoEditado)
            }
        }

        // ✅ Botón cancelar
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // ✅ Observamos resultado desde el ViewModel
        productoViewModel.resultado.observe(this) { estado ->
            when (estado) {
                ResultadoOperacion.EXITO -> {
                    mostrarDialogoConfirmacion()
                    finish()
                }
                ResultadoOperacion.ERROR -> {
                    mostrarDialogoError()
                }
            }
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
