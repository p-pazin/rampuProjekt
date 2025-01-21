package com.example.carchive.data.dto

import java.io.Serializable

data class ContractDetailedRentDto (
    val id: Int,
    val title: String,
    val place: String,
    val dateOfCreation: String,
    val type: Int,
    val content: String,
    val signed: Int,
    val name: String,
    val city: String,
    val address: String,
    val pin: String,
    val firstNameDirector: String,
    val lastNameDirector: String,
    val firstNameContact: String,
    val lastNameContact: String,
    val pinContact: String,
    val countryContact: String,
    val cityContact: String,
    val addressContact: String,
    val brand: String,
    val model: String,
    val engine: String,
    val registration: String,
    val mileage: Int,
    val reservationId: Int,
    val price: Int,
    val startDate: String,
    val endDate: String,
    val maxMileage: Int,
    val nameInsurance: String,
    val costInsurance: Int
) : Serializable