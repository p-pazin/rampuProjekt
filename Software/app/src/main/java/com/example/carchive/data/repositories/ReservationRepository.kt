package com.example.carchive.data.repositories

import com.example.carchive.data.dto.ReservationDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse

class ReservationRepository {
    private val networkClient = Network().getInstance()

    suspend fun getReservations(): Result<List<ReservationDto>> {
        return safeResponse {
            networkClient.getReservations()
        }
    }
}