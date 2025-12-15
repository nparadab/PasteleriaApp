package com.proyecto1.milsabores.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ✅ Interceptor que agrega el token solo si existe y no está vacío
    private val authInterceptor = Interceptor { chain ->
        val prefs = appContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        val original: Request = chain.request()
        val builder = original.newBuilder()

        if (!token.isNullOrBlank()) {
            builder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(builder.build())
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    private val backendRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://milsabores-api.onrender.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productoApi: ProductoApiService by lazy {
        backendRetrofit.create(ProductoApiService::class.java)
    }

    val categoriaApi: CategoriaApiService by lazy {
        backendRetrofit.create(CategoriaApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        backendRetrofit.create(UserApiService::class.java)
    }

    val usuarioApi: UsuarioApiService by lazy {
        backendRetrofit.create(UsuarioApiService::class.java)
    }

    private val mealRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mealApi: MealApiService by lazy {
        mealRetrofit.create(MealApiService::class.java)
    }

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
