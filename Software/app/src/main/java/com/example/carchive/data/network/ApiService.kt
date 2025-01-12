package com.example.carchive.data.network

import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.VehicleDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("User/login")
    suspend fun login(@Body body: LoginRequestDto): LoginDto

    @GET("Vehicle")
    suspend fun getVehicles(): List<VehicleDto>

    @GET("User")
    suspend fun getUser(): UserDto
}