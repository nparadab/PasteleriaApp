package com.proyecto1.milsabores.repository

import com.proyecto1.milsabores.network.ApiService

class ClimaRepository {
    suspend fun obtenerClima(lat: Double, lon: Double) =
        ApiService.openMeteoApi.getCurrentWeather(lat, lon)
}
