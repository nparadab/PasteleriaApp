package com.proyecto1.milsabores

import com.proyecto1.milsabores.network.ApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ApiTests {


    @Test
    fun `backend devuelve productos`() = runBlocking {
        val productos = ApiService.productoApi.obtenerProductos()
        assertTrue(productos.isNotEmpty())
    }


    @Test
    fun `buscar receta devuelve resultados`() = runBlocking {
        val response = ApiService.mealApi.searchMeals("cake")
        assertTrue(response.isSuccessful)
        assertNotNull(response.body()?.meals)
    }


    @Test
    fun `buscar receta inexistente devuelve null`() = runBlocking {
        val response = ApiService.mealApi.searchMeals("zzzz")
        assertTrue(response.isSuccessful)
        assertNull(response.body()?.meals)
    }


    @Test
    fun `obtener clima actual de Santiago con OpenMeteo`() = runBlocking {
        val response = ApiService.openMeteoApi.getCurrentWeather(-33.45, -70.66)
        assertTrue(response.isSuccessful)
        assertNotNull(response.body()?.current_weather?.temperature)
    }


    @Test
    fun `respuesta de clima contiene temperatura y viento`() = runBlocking {
        val response = ApiService.openMeteoApi.getCurrentWeather(-33.45, -70.66)
        assertTrue(response.isSuccessful)
        val weather = response.body()?.current_weather
        assertNotNull(weather?.temperature)
        assertNotNull(weather?.windspeed)
    }
}
