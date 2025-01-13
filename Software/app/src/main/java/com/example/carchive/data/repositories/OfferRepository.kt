package com.example.carchive.data.repositories

import android.util.Log
import com.example.carchive.data.dto.OfferDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse

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
}