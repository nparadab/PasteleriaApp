package com.proyecto1.milsabores.repository

import com.proyecto1.milsabores.model.ProductoDTO
import com.proyecto1.milsabores.network.ApiService
import retrofit2.Response

class ProductoRepository {

    suspend fun obtenerTodos(): List<ProductoDTO> {
        return ApiService.productoApi.obtenerProductos()
    }

    suspend fun insertar(producto: ProductoDTO): Boolean {
        val response: Response<ProductoDTO> = ApiService.productoApi.agregarProducto(producto)
        return response.isSuccessful && response.body() != null
    }

    suspend fun editar(producto: ProductoDTO): Boolean {
        val response: Response<ProductoDTO> = ApiService.productoApi.editarProducto(producto.id!!, producto)
        return response.isSuccessful && response.body() != null
    }

    suspend fun eliminar(id: Long): Boolean {
        val response: Response<Void> = ApiService.productoApi.eliminarProducto(id)
        return response.isSuccessful
    }
}
