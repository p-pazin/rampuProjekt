package com.example.carchive.data.repositories

import android.content.Context
import android.util.Log
import com.example.carchive.data.dto.UploadResponse
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.dto.VehicleIdResponse
import com.example.carchive.data.dto.VehiclePhotoDto
import com.example.carchive.data.dto.toDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.http.HTTP
import java.io.File

class VehicleRepository {
    private val networkClient = Network().getInstance()

    suspend fun getVehicles(): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehicles()
            Log.d("VehicleRepository", "Dohvaćeni DTO: $response")
            response.map { it.toEntity() }
        }
    }

    suspend fun getVehiclesSale(): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehiclesSale()
            response.map { it.toEntity() }
        }
    }

    suspend fun getVehiclesRent(): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehiclesRent()
            response.map { it.toEntity() }
        }
    }

    suspend fun getVehiclesCatalog(): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehiclesCatalog()
            Log.d("VehicleRepository", "Dohvaćeni DTO: $response")
            response.map { it.toEntity() }
        }
    }


    suspend fun postVehicle(vehicleDto: VehicleDtoPost): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postVehicle(vehicleDto)
        }
    }

    suspend fun putVehicle(id: Int, vehicleDto: VehicleDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.putVehicle(id, vehicleDto)
        }
    }

    suspend fun deleteVehicle(id: Int): Result<Response<Unit>>{
        return safeResponse {
            networkClient.deleteVehicle(id)
        }
    }



    suspend fun getVehicleIdByReg(reg: String): Result<Response<List<VehicleIdResponse>>> {
        return safeResponse {
            networkClient.getVehicleIdByReg(reg)
        }
    }

    suspend fun uploadPhoto(file: File): Result<Response<UploadResponse>> {
        return safeResponse {
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", file.name, requestBody)

            networkClient.uploadPhoto(part)
        }
    }

    suspend fun connectVehicleToPhoto(vehicleId: Int, filePath: String): Result<Response<Unit>> {
        return safeResponse {
            networkClient.connectVehicleToPhoto(vehicleId, filePath)
        }
    }

    suspend fun getVehiclePhotos(vehicleId: Int): Result<List<VehiclePhotoDto>> {
        return try {
            val response = networkClient.getPhotosForVehicle(vehicleId)

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception("Failed to fetch photos: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteVehiclePhoto(id: Int): Result<Response<Unit>> {
        return safeResponse {
            networkClient.deleteVehiclePhoto(id)
        }
    }
}
