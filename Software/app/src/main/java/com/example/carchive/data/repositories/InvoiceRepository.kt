package com.example.carchive.data.repositories

import android.util.Log
import com.example.carchive.data.dto.InvoiceDto
import com.example.carchive.data.dto.toEntity
import com.example.carchive.data.network.Network
import com.example.carchive.data.network.Result
import com.example.carchive.entities.Vehicle
import com.example.carchive.util.safeResponse

class InvoiceRepository {
    private val networkClient = Network().getInstance()

    suspend fun getInvoices(): Result<List<InvoiceDto>> {
        return safeResponse {
            networkClient.getInvoices()
        }
    }
}