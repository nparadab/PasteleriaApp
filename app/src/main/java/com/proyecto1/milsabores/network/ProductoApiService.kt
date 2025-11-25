package com.proyecto1.milsabores.network

import com.proyecto1.milsabores.model.ProductoDTO
import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {

    @GET("productos")
    suspend fun obtenerProductos(): List<ProductoDTO>

    @POST("productos")
    suspend fun agregarProducto(@Body producto: ProductoDTO): Response<ProductoDTO>

    @PUT("productos/{id}")
    suspend fun editarProducto(
        @Path("id") id: Long,
        @Body producto: ProductoDTO
    ): Response<ProductoDTO>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long): Response<Void>
}
