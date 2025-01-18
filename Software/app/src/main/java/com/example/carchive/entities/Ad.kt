package com.example.carchive.entities

import java.io.Serializable

data class Ad(
    val id: Int,
    val title: String,
    val description: String,
    val paymentMethod: String,
    val dateOfPublishment: String,
    val brand: String,
    val model: String,
    val link: String
): Serializable
