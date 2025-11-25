package com.proyecto1.milsabores.network

import com.proyecto1.milsabores.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") name: String): Response<MealResponse>
}
