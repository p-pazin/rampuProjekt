package com.example.carchive.data.network

import com.example.carchive.adapters.OfferAdapter
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.LocationDto
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.NewCompanyDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import retrofit2.Response
import com.example.carchive.data.dto.YearlyInfoDto
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

    @POST("Company")
    suspend fun register(@Body body: NewCompanyDto): Response<Unit>

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

    @GET("Vehicle/offer/{offerId}")
    suspend fun getVehiclesForOffer(@Path("offerId") id: Int): List<VehicleDto>

    @GET("User")
    suspend fun getUser(): UserDto

    @GET("Contact")
    suspend fun getContacts(): List<ContactDto>

    @GET("Contact/contacts/{offerId}")
    suspend fun getContactByOfferId(@Path("offerId") offerId: Int): ContactDto

    @POST("Contact")
    suspend fun postContact(@Body body: ContactDto): Response<Unit>

    @PUT("Contact/{id}")
    suspend fun putContact(@Path("id") id: Int, @Body body: ContactDto): Response<Unit>

    @DELETE("Contact/{id}")
    suspend fun deleteContact(@Path("id") id: Int): Response<Unit>

    @GET("Stats/ContactStatus")
    suspend fun getContactStatusStats(): ContactStatusStatsDto

    @GET("Stats/ContactCreation")
    suspend fun getContactCreationStats(): YearlyInfoDto

    @GET("Stats/InvoiceCreation")
    suspend fun getInvoiceCreationStats(): YearlyInfoDto

    @GET("Location")
    suspend fun getLocations(): List<LocationDto>
}