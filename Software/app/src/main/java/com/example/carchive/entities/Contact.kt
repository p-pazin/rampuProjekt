package com.example.carchive.entities

import java.io.Serializable
import java.time.LocalDate


data class Contact(
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
) : Serializable
