package com.example.carchive.data.repositories

import com.example.carchive.data.dto.LocationDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse

class LocationRepository {
    private val networkClient = Network().getInstance()

    suspend fun getLocations() : Result<List<LocationDto>> {
        return safeResponse {
            networkClient.getLocations()
        }
    }
}