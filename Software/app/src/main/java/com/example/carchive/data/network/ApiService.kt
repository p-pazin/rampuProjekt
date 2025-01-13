package com.example.carchive.data.network

import com.example.carchive.adapters.OfferAdapter
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("User/login")
    suspend fun login(@Body body: LoginRequestDto): LoginDto

    @GET("Vehicle")
    suspend fun getVehicles(): List<VehicleDto>

    @POST("Vehicle")
    suspend fun postVehicle(@Body body: VehicleDtoPost): Response<Unit>

    @PUT("Vehicle/{vehicleId}")
    suspend fun putVehicle(
        @Path("vehicleId") id: Int,
        @Body body: VehicleDto
    ): Response<Unit>

    @DELETE("Vehicle/{id}")
    suspend fun deleteVehicle(@Path("id") id: Int): Response<Unit>

    @GET("Offer")
    suspend fun getOffers(): List<OfferDto>

    @GET("offer/{offerId}")
    suspend fun getVehiclesForOffer(@Path("id") id: Int): List<VehicleDto>

    @GET("User")
    suspend fun getUser(): UserDto
}