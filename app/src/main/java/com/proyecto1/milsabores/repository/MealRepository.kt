package com.proyecto1.milsabores.repository

import com.proyecto1.milsabores.network.ApiService

class MealRepository {
    suspend fun buscarReceta(nombre: String) =
        ApiService.mealApi.searchMeals(nombre)
}
