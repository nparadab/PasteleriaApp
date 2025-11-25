package com.proyecto1.milsabores.model

data class MealResponse(val meals: List<Meal>?)

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String?,
    val strMealThumb: String?
)
