package com.example.carchive.data.network

import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.YearlyInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("User/login")
    suspend fun login(@Body body: LoginRequestDto): LoginDto

    @GET("Vehicle")
    suspend fun getVehicles(): List<VehicleDto>

    @GET("User")
    suspend fun getUser(): UserDto

    @GET("Contact")
    suspend fun getContacts(): List<ContactDto>

    @POST("Contact")
    suspend fun postContact(@Body body: ContactDto): Response<Unit>

    @PUT("Contact/{id}")
    suspend fun putContact(@Path("id") id: Int, @Body body: ContactDto): Response<Unit>

    @DELETE("Contact/{id}")
    suspend fun deleteContact(@Path("id") id: Int): Response<Unit>

    @GET("ContactStatus")
    suspend fun getContactStatusStats(): ContactStatusStatsDto

    @GET("ContactCreation")
    suspend fun getContactCreationStats(): YearlyInfoDto

    @GET("InvoiceCreation")
    suspend fun getInvoiceCreationStats(): YearlyInfoDto
}