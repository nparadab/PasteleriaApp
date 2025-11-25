package com.proyecto1.milsabores.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    // 🔗 Backend Render
    private val backendRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://milsabores-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productoApi: ProductoApiService by lazy {
        backendRetrofit.create(ProductoApiService::class.java)
    }

    val categoriaApi: CategoriaApiService by lazy {
        backendRetrofit.create(CategoriaApiService::class.java)
    }

    // 🔗 API externa: TheMealDB
    private val mealRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mealApi: MealApiService by lazy {
        mealRetrofit.create(MealApiService::class.java)
    }

    // 🔗 API externa: Open-Meteo (sin API key)
    private val openMeteoRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val openMeteoApi: OpenMeteoApiService by lazy {
        openMeteoRetrofit.create(OpenMeteoApiService::class.java)
    }
}
