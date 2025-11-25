package com.proyecto1.milsabores.network

import com.proyecto1.milsabores.model.CategoriaDTO
import retrofit2.http.GET

interface CategoriaApiService {
    @GET("categorias")
    suspend fun obtenerCategorias(): List<CategoriaDTO>
}
