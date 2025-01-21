package com.example.carchive.data.dto

import java.io.Serializable

data class ContractDetailedSaleDto (
    val id: Int,
    val title: String,
    val place: String,
    val dateOfCreation: String,
    val type: Int,
    val content: String,
    val signed: Int,
    val companyName: String,
    val companyPin: String,
    val companyAddress: String,
    val contactName: String,
    val contactPin: String,
    val contactAddress: String,
    val vehicle: VehicleDto?,
    val price: Int,
    val userName: String,
    val offerId: Int,
    val vehicles: List<VehicleDto>?
) : Serializable