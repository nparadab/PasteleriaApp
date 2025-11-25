package com.proyecto1.milsabores.repository

import android.util.Log
import com.proyecto1.milsabores.model.CategoriaDTO
import com.proyecto1.milsabores.network.ApiService

class CategoriaRepository {

    suspend fun obtenerTodas(): List<CategoriaDTO> {
        return try {
            ApiService.categoriaApi.obtenerCategorias()
        } catch (e: Exception) {
            Log.e("CategoriaRepository", "Error al obtener categorías", e)
            emptyList()
        }
    }
}
