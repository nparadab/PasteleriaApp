package com.proyecto1.milsabores.network

import com.proyecto1.milsabores.model.LoginRequest
import com.proyecto1.milsabores.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    // ✅ Login
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // ✅ Recuperar contraseña
    @POST("auth/recuperar")
    suspend fun recuperarPassword(@Body body: Map<String, String>): Response<ResponseBody>
}
