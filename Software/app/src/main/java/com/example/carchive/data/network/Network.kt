package com.example.carchive.data.network

import android.content.Context
import com.example.carchive.services.TokenManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val  BASE_URL = "https://carchive.online/"
class Network {

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(AuthInterceptor(TokenManager()))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(10.seconds.toJavaDuration()).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var INSTANCE: ApiService? = null

    fun getInstance(): ApiService {
        return INSTANCE ?: retrofit.create(ApiService::class.java).also {
            INSTANCE = it
        }
    }


}
