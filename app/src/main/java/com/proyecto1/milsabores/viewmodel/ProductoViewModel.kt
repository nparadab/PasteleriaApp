package com.proyecto1.milsabores.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proyecto1.milsabores.database.ProductoRepository
import com.proyecto1.milsabores.model.Producto

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductoRepository(application)
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> get() = _productos

    fun cargarProductos() {
        _productos.value = repository.obtenerTodos()
    }

    fun agregarProducto(nombre: String, precio: Double) {
        if (repository.insertar(nombre, precio)) {
            cargarProductos()
        }
    }
}
