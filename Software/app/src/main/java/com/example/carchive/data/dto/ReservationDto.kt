package com.example.carchive.data.dto

data class ReservationDto (
    val id: Int,
    val state: Int,
    val price: Int,
    val startDate: String,
    val endDate: String,
    val dateOfCreation: String,
    val maxMileage: Int,
    val vehicleId: Int,
    val contactId: Int
){
    companion object {
        val EMPTY = ReservationDto(
            id = 0,
            state = 0,
            price = 0,
            startDate = "",
            endDate = "",
            dateOfCreation = "",
            maxMileage = 0,
            vehicleId = 0,
            contactId = 0
        )
    }
}
