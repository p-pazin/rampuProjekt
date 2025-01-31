package com.example.carchive.data.repositories

import com.example.carchive.data.dto.PenaltyDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse

class PenaltyRepository {
    private val networkClient = Network().getInstance()

    suspend fun getPenalties(): Result<List<PenaltyDto>> {
        return safeResponse {
            networkClient.getPenalties()
        }
    }
}