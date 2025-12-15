package com.proyecto1.milsabores.network

import com.proyecto1.milsabores.model.Usuario
import retrofit2.Response
import retrofit2.http.GET

interface UsuarioApiService {

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>
}
