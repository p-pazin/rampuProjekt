package com.example.carchive.data.repositories

import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.YearlyInfoDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Contact
import com.example.carchive.util.safeResponse

class StatsRepository {
    private val networkClient = Network().getInstance()

    suspend fun getContactStatusStats(): Result<ContactStatusStatsDto> {
        return safeResponse {
            networkClient.getContactStatusStats()
        }
    }

    suspend fun getContactCreationStats(): Result<YearlyInfoDto> {
        return safeResponse {
            networkClient.getContactCreationStats()
        }
    }

    suspend fun getInvoiceCreationStats(): Result<YearlyInfoDto> {
        return safeResponse {
            networkClient.getInvoiceCreationStats()
        }
    }
}