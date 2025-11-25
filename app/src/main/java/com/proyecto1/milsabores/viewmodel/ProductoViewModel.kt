package com.proyecto1.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proyecto1.milsabores.model.ProductoDTO
import com.proyecto1.milsabores.model.CategoriaDTO
import com.proyecto1.milsabores.repository.ProductoRepository
import com.proyecto1.milsabores.repository.CategoriaRepository
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val productoRepo = ProductoRepository()
    private val categoriaRepo = CategoriaRepository()

    private val _productos = MutableLiveData<List<ProductoDTO>>()
    val productos: LiveData<List<ProductoDTO>> get() = _productos

    private val _categorias = MutableLiveData<List<CategoriaDTO>>()
    val categorias: LiveData<List<CategoriaDTO>> get() = _categorias

    enum class ResultadoOperacion { EXITO, ERROR }

    private val _resultado = MutableLiveData<ResultadoOperacion>()
    val resultado: LiveData<ResultadoOperacion> get() = _resultado

    // ✅ Productos
    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val lista = productoRepo.obtenerTodos()
                _productos.postValue(lista)
            } catch (e: Exception) {
                e.printStackTrace()
                _productos.postValue(emptyList())
                _resultado.postValue(ResultadoOperacion.ERROR)
            }
        }
    }

    fun agregarProducto(producto: ProductoDTO) {
        viewModelScope.launch {
            try {
                val ok = productoRepo.insertar(producto)
                _resultado.postValue(if (ok) ResultadoOperacion.EXITO else ResultadoOperacion.ERROR)
                if (ok) cargarProductos()
            } catch (e: Exception) {
                e.printStackTrace()
                _resultado.postValue(ResultadoOperacion.ERROR)
            }
        }
    }

    fun editarProducto(producto: ProductoDTO) {
        viewModelScope.launch {
            try {
                val ok = productoRepo.editar(producto)
                _resultado.postValue(if (ok) ResultadoOperacion.EXITO else ResultadoOperacion.ERROR)
                if (ok) cargarProductos()
            } catch (e: Exception) {
                e.printStackTrace()
                _resultado.postValue(ResultadoOperacion.ERROR)
            }
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            try {
                val ok = productoRepo.eliminar(id)
                _resultado.postValue(if (ok) ResultadoOperacion.EXITO else ResultadoOperacion.ERROR)
                if (ok) cargarProductos()
            } catch (e: Exception) {
                e.printStackTrace()
                _resultado.postValue(ResultadoOperacion.ERROR)
            }
        }
    }

    // ✅ Categorías
    fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val lista = categoriaRepo.obtenerTodas()
                _categorias.postValue(lista)
            } catch (e: Exception) {
                e.printStackTrace()
                _categorias.postValue(emptyList())
            }
        }
    }
}
