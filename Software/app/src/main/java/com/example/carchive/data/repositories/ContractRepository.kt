package com.example.carchive.data.repositories

import com.example.carchive.data.dto.ContactDto
import com.example.carchive.data.dto.ContractDetailedRentDto
import com.example.carchive.data.dto.ContractDetailedSaleDto
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.util.safeResponse
import retrofit2.Response

class ContractRepository {
    private val networkClient = Network().getInstance()

    suspend fun getContracts() : Result<List<ContractDto>> {
        return safeResponse {
            networkClient.getContracts()
        }
    }

    suspend fun getContractSaleById(id : Int) : Result<ContractDetailedSaleDto> {
        return safeResponse {
            networkClient.getContractSaleById(id)
        }
    }

    suspend fun getContractRentById(id : Int) : Result<ContractDetailedRentDto> {
        return safeResponse {
            networkClient.getContractRentById(id)
        }
    }

    suspend fun postContractSale(contactId: Int?, vehicleId: Int?, offerId: Int?,
                                 contractDto: ContractDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postContractSale(contactId, vehicleId, offerId, contractDto)
        }
    }

    suspend fun postContractRent(contactId: Int?, vehicleId: Int?, reservationId: Int?,
                                 insuranceId: Int?, contractDto: ContractDto): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postContractRent(contactId, vehicleId, reservationId, insuranceId, contractDto)
        }
    }
}