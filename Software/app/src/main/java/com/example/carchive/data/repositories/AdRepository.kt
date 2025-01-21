package com.example.carchive.data.repositories

import com.example.carchive.data.dto.AdDto
import com.example.carchive.data.dto.AdDtoPost
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Ad
import com.example.carchive.util.safeResponse
import retrofit2.Response

class AdRepository {
    private val networkClient = Network().getInstance()

    suspend fun getAds(): Result<List<Ad>> {
        return safeResponse {
            networkClient.getAds().map { adDto ->
                adDto.toEntity()
            }
        }
    }
    suspend fun getAd(id:Int): Result<Ad> {
        return safeResponse {
            networkClient.getAd(id).toEntity()
        }
    }
    suspend fun deleteAd(id:Int): Result<Unit> {
        return safeResponse {
            networkClient.deleteAd(id)
        }
    }

    suspend fun putAd(ad: AdDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.putAd(ad)
        }
    }

    suspend fun postAd(adDto: AdDto, id : Int): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postAd(adDto, id)
        }
    }

}