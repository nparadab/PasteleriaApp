package com.proyecto1.milsabores.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto1.milsabores.model.CurrentWeather
import com.proyecto1.milsabores.repository.ClimaRepository
import kotlinx.coroutines.launch

class ClimaViewModel : ViewModel() {

    private val repo = ClimaRepository()
    val clima = MutableLiveData<CurrentWeather>()

    fun cargarClima(lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = repo.obtenerClima(lat, lon)
            if (response.isSuccessful) {
                clima.postValue(response.body()?.current_weather)
            }
        }
    }
}
