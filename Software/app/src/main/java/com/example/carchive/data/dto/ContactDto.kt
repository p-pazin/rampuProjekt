package com.example.carchive.data.dto

import com.example.carchive.entities.Contact
import java.time.LocalDate

data class ContactDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val pin: String,
    val dateOfCreation: String,
    val state: Int,
    val country: String,
    val city: String,
    val address: String,
    val telephoneNumber: String,
    val mobileNumber: String,
    val email: String,
    val description: String
)

fun ContactDto.toEntity() =
    Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        pin = pin,
        dateOfCreation = dateOfCreation,
        state = state,
        country = country,
        city = city,
        address = address,
        telephoneNumber = telephoneNumber,
        mobileNumber = mobileNumber,
        email = email,
        description = description
    )
