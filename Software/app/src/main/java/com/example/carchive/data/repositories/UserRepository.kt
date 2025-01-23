package com.example.carchive.data.repositories

import com.example.carchive.data.dto.CompanyDto
import com.example.carchive.data.dto.LoginDto
import com.example.carchive.data.dto.LoginRequestDto
import com.example.carchive.data.dto.NewCompanyDto
import com.example.carchive.data.dto.NewUserDto
import com.example.carchive.data.dto.UserDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Ad
import com.example.carchive.util.safeResponse
import retrofit2.Response

class UserRepository {
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

    suspend fun getUser(): Result<UserDto> {
        return safeResponse {
            networkClient.getUser()
        }
    }

    suspend fun getCompany(): Result<CompanyDto> {
        return safeResponse {
            networkClient.getCompany()
        }
    }

    suspend fun getCompanyUsers(): Result<List<UserDto>> {
        return safeResponse {
            networkClient.getCompanyUsers()
        }
    }

    suspend fun registerNewUser(newUser: NewUserDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.newUser(newUser)
        }
    }

    suspend fun updateUser(user: UserDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.updateUser(user)
        }
    }

    suspend fun deleteUser(user: UserDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.deleteUser(user)
        }
    }
}