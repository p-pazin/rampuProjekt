package com.example.carchive.data.repositories

import android.util.Log
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.OfferPostDto
import com.example.carchive.data.dto.VehicleDto
import com.example.carchive.data.dto.VehicleDtoPost
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import org.json.JSONObject
import retrofit2.Response

class OfferRepository {
    private val networkClient = Network().getInstance()

    suspend fun getOffers(): Result<List<OfferDto>> {
        return safeResponse {
            networkClient.getOffers()
        }
    }

    suspend fun getVehiclesForOffer(id: Int): Result<List<Vehicle>> {
        return safeResponse {
            val response = networkClient.getVehiclesForOffer(id)
            response.map { it.toEntity() }
        }
    }

    suspend fun postOffer(offer: OfferPostDto, contactId: Int, vehicles: List<Int>): Result<Response<Unit>> {
        vehicles.map {  }
        return safeResponse {
            networkClient.postOffer(contactId, vehicles, offer)
        }
    }

    suspend fun putOffer(offer: OfferDto, contactId: Int, vehicles: List<Int>): Result<Response<Unit>> {
        vehicles.map {  }
        return safeResponse {
            networkClient.putOffer(contactId, vehicles, offer)
        }
    }

    suspend fun deleteOffer(id: Int): Result<Response<Unit>> {
        return try {
            val response = networkClient.deleteOffer(id)
            if (response.isSuccessful) {
                Result.Success(response)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (response.code() == 404 && errorBody != null) {
                    JSONObject(errorBody).optString("message", "Unknown error")
                } else {
                    "Error: ${response.code()}"
                }
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}