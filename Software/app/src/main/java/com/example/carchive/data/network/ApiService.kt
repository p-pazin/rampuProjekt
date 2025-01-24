package com.example.carchive.data.network

import com.example.carchive.data.dto.AdDto
import com.example.carchive.data.dto.AdDtoPost
import com.example.carchive.data.dto.CompanyDto
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.dto.InsuranceDto
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.dto.LocationDto
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.NewCompanyDto
import com.example.carchive.data.dto.NewPasswordDto
import com.example.carchive.data.dto.NewUserDto
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.OfferPostDto
import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.dto.UploadResponse
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.dto.VehicleIdResponse
import com.example.carchive.data.dto.VehiclePhotoDto
import retrofit2.Response
import com.example.carchive.data.dto.YearlyInfoDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("User/login")
    suspend fun login(@Body body: LoginRequestDto): LoginDto

    @POST("Company")
    suspend fun register(@Body body: NewCompanyDto): Response<Unit>

    @GET("User")
    suspend fun getUser(): UserDto

    @GET("Company")
    suspend fun getCompany(): CompanyDto

    @GET("Company/users")
    suspend fun getCompanyUsers(): List<UserDto>

    @POST("User/new")
    suspend fun newUser(@Body body: NewUserDto): Response<Unit>

    @PUT("User/update")
    suspend fun updateUser(@Body body: UserDto): Response<Unit>

    @PUT("User/newpassword")
    suspend fun newpasswordUser(@Body newPassword: NewPasswordDto): Response<Unit>

    @DELETE("User/delete/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>

    @GET("Vehicle")
    suspend fun getVehicles(): List<VehicleDto>

    @GET("Vehicle/{vehicleId}")
    suspend fun getVehicleById(@Path("vehicleId") vehicleId: Int): VehicleDto

    @GET("Vehicle/sale")
    suspend fun getVehiclesSale(): List<VehicleDto>

    @GET("Vehicle/rent")
    suspend fun getVehiclesRent(): List<VehicleDto>

    @GET("Vehicle/catalog")
    suspend fun getVehiclesCatalog(): List<VehicleDto>

    @POST("Vehicle")
    suspend fun postVehicle(@Body body: VehicleDtoPost): Response<Unit>

    @PUT("Vehicle/{vehicleId}")
    suspend fun putVehicle(
        @Path("vehicleId") id: Int,
        @Body body: VehicleDto
    ): Response<Unit>

    @DELETE("Vehicle/{id}")
    suspend fun deleteVehicle(@Path("id") id: Int): Response<Unit>

    @GET("Vehicle/registration/{reg}")
    suspend fun getVehicleIdByReg(@Path("reg") reg: String): Response<List<VehicleIdResponse>>

    @POST("Vehicle/upload")
    @Multipart
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    @POST("Vehicle/connect/{vehicleId}")
    suspend fun connectVehicleToPhoto(
        @Path("vehicleId") vehicleId: Int,
        @Body photoUrl: String
    ): Response<Unit>

    @GET("Vehicle/photos/{vehicleId}")
    suspend fun getPhotosForVehicle(@Path("vehicleId") vehicleId: Int): Response<List<VehiclePhotoDto>>

    @GET("Vehicle/offer/{offerId}")
    suspend fun getVehiclesForOffer(@Path("offerId") id: Int): List<VehicleDto>

    @DELETE("Vehicle/photo/{photoId}")
    suspend fun deleteVehiclePhoto(@Path("photoId") id: Int): Response<Unit>

    @GET("Offer")
    suspend fun getOffers(): List<OfferDto>

    @POST("Offer")
    suspend fun postOffer(
        @Query("contactId") contactId: Int,
        @Query("vehiclesId") vehiclesId: List<Int>,
        @Body body: OfferPostDto
    ): Response<Unit>

    @PUT("Offer")
    suspend fun putOffer(
        @Query("contactId") contactId: Int,
        @Query("vehiclesId") vehiclesId: List<Int>,
        @Body body: OfferDto
    ): Response<Unit>

    @DELETE("Offer/{id}")
    suspend fun deleteOffer(@Path("id") id: Int): Response<Unit>



    @GET("Contact")
    suspend fun getContacts(): List<ContactDto>

    @GET("Contact/{contactId}")
    suspend fun getContactById(@Path("contactId") id: Int): ContactDto

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

    @GET("Ad")
    suspend fun getAds(): List<AdDto>

    @GET("Ad/{adId}")
    suspend fun getAd(@Path("adId") adId: Int): AdDto

    @PUT("Ad")
    suspend fun putAd(@Body body: AdDto): Response<Unit>

    @DELETE("Ad")
    suspend fun deleteAd(@Query("id") id: Int): Response<Unit>

    @POST("Ad")
    suspend fun postAd(@Body body: AdDto, @Query("id") id: Int): Response<Unit>


    @GET("Contract")
    suspend fun getContracts(): List<ContractDto>

    @GET("Contract/sell/{id}")
    suspend fun getContractSaleById(@Path("id") id: Int): ContractDetailedSaleDto

    @GET("Contract/rent/{id}")
    suspend fun getContractRentById(@Path("id") id: Int): ContractDetailedRentDto

    @GET("Insurance")
    suspend fun getInsurances(): List<InsuranceDto>

    @POST("Contract/sell")
    suspend fun postContractSale(@Query("contactId") contactId: Int?,
                                 @Query("vehicleId") vehicleId: Int?,
                                 @Query("offerId") offerId: Int?,
                                 @Body body: ContractDto): Response<Unit>

    @POST("Contract/rent")
    suspend fun postContractRent(@Query("reservationId") reservationId: Int?,
                                 @Query("insuranceId") insuranceId: Int?,
                                 @Body body: ContractDto): Response<Unit>

    @PUT("Contract/sell")
    suspend fun putContractSale(@Query("contactId") contactId: Int?,
                                 @Query("vehicleId") vehicleId: Int?,
                                 @Query("offerId") offerId: Int?,
                                 @Body body: ContractDto): Response<Unit>

    @PUT("Contract/rent")
    suspend fun putContractRent(@Query("reservationId") reservationId: Int?,
                                 @Query("insuranceId") insuranceId: Int?,
                                 @Body body: ContractDto): Response<Unit>

    @DELETE("Contract/{id}")
    suspend fun deleteContract(@Path("id") id: Int): Response<Unit>

    @GET("Invoice")
    suspend fun getInvoices(): List<InvoiceDto>
    @GET("Reservation")
    suspend fun getReservations(): List<ReservationDto>

    @GET("Reservation/{id}")
    suspend fun getReservation(@Path("id") id: Int): ReservationDto

    @POST("Reservation")
    suspend fun postReservation(
        @Query("contactid") contactid: Int?,
        @Query("vehicleid") vehicleid: Int?,
        @Body body: ReservationDto,): Response<Unit>

}