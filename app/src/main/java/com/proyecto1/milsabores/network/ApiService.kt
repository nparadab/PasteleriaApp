package com.proyecto1.milsabores.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://milsabores-api.onrender.com/api/") // URL base correcta
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productoApi: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    val categoriaApi: CategoriaApiService by lazy {
        retrofit.create(CategoriaApiService::class.java)
    }
}
