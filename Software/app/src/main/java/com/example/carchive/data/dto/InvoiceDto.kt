package com.example.carchive.data.dto

data class InvoiceDto(
    val id: Int,
    val dateOfCreation: String,
    val vat: Double,
    val paymentMethod: String,
    val totalCost: Double,
    val mileage: Int?,
    val contractId: Int
)

data class InvoiceDtoPost(
    val dateOfCreation: String,
    val vat: Double,
    val paymentMethod: String,
    val mileage: Int?
)
