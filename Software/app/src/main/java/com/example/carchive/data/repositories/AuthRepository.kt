package com.example.carchive.data.repositories

import android.util.Log
import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.NewCompanyDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import retrofit2.Response

class AuthRepository {
    private val networkClient = Network().getInstance()

    suspend fun loginUser(loginRequest : LoginRequestDto): Result<LoginDto> {
        return safeResponse {
            networkClient.login(loginRequest)
        }
    }

    suspend fun registerCompany(newCompany: NewCompanyDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.register(newCompany)
        }
    }
}