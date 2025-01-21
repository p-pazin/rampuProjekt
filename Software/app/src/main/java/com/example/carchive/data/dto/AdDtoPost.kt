package com.example.carchive.data.dto

import com.example.carchive.entities.Ad

data class AdDtoPost(
    val title: String,
    val description: String,
    val paymentMethod: String,
    val dateOfPublishment: String,
    val link: String? = ""
)

fun AdDtoPost.toEntity() =
    AdDto(
        id = 0,
        title = title,
        description = description,
        paymentMethod = paymentMethod,
        dateOfPublishment = dateOfPublishment,
        brand = "",
        model = "",
        links = listOf(link ?: "")
    )



