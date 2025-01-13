package com.example.carchive.data.repositories

import android.content.Context
import android.util.Log
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.dto.toDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import retrofit2.Response
import retrofit2.http.HTTP

class VehicleRepository {
    private val networkClient = Network().getInstance()

    suspend fun getVehicles(): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehicles()
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
}
