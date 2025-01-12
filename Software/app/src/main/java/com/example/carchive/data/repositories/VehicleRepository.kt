package com.example.carchive.data.repositories

import android.content.Context
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse

class VehicleRepository {
    private val networkClient = Network().getInstance()
    suspend fun getVehicles():Result <List<Vehicle>>{
        return safeResponse {
            networkClient.getVehicles().map {
                it.toEntity()
            }
        }
    }
}