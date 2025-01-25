package com.example.carchive.data.repositories

import android.util.Log
import com.example.carchive.data.dto.ContractDto
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.dto.InvoiceDtoPost
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse
import retrofit2.Response

class InvoiceRepository {
    private val networkClient = Network().getInstance()

    suspend fun getInvoices(): Result<List<InvoiceDto>> {
        return safeResponse {
            networkClient.getInvoices()
        }
    }

    suspend fun postInvoiceSale(contractId: Int, invoiceDto: InvoiceDtoPost): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postInvoiceSell(contractId, invoiceDto)
        }
    }

    suspend fun postInvoiceRent(contractId: Int, invoiceDto: InvoiceDtoPost): Result<Response<Unit>> {
        return safeResponse {
            networkClient.postInvoiceRentStart(contractId, invoiceDto)
        }
    }
}