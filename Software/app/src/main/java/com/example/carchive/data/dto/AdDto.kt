package com.example.carchive.data.dto

import com.example.carchive.entities.Ad

data class AdDto(
    val id: Int,
    val title: String,
    val description: String,
    val paymentMethod: String,
    val dateOfPublishment: String,
    val brand: String,
    val model: String,
    val links: List<String>
)

fun AdDto.toEntity() =
    Ad(
        id = id,
        title = title,
        description = description,
        paymentMethod = paymentMethod,
        dateOfPublishment = dateOfPublishment,
        brand = brand,
        model = model,
        links = links.ifEmpty { listOf("") }
    )
