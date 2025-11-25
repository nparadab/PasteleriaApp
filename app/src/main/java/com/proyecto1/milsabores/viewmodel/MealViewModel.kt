package com.proyecto1.milsabores.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto1.milsabores.model.Meal
import com.proyecto1.milsabores.repository.MealRepository
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {

    private val repo = MealRepository()
    val recetas = MutableLiveData<List<Meal>>()

    fun buscar(nombre: String) {
        viewModelScope.launch {
            val response = repo.buscarReceta(nombre)
            if (response.isSuccessful) {
                recetas.postValue(response.body()?.meals ?: emptyList())
            }
        }
    }
}
