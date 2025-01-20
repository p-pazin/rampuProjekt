package com.example.carchive.data.repositories

import com.example.carchive.data.dto.InsuranceDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse

class InsuranceRepository {
    private val networkClient = Network().getInstance()

    suspend fun getInsurances(): Result<List<InsuranceDto>> {
        return safeResponse {
            networkClient.getInsurances()
        }
    }
}