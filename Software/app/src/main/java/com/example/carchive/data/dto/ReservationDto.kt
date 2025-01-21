package com.example.carchive.data.dto

data class ReservationDto (
    val id: Int,
    val state: Int,
    val price: Int,
    val startDate: String,
    val endDate: String,
    val dateOfCreation: String,
    val maxMileage: Int
)