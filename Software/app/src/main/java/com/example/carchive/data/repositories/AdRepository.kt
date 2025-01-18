package com.example.carchive.data.repositories

import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Ad
import com.example.carchive.util.safeResponse

class AdRepository {
    private val networkClient = Network().getInstance()

    suspend fun getAds(): Result<List<Ad>> {
        return safeResponse {
            networkClient.getAds().map {
                it.toEntity()
            }
        }
    }
}