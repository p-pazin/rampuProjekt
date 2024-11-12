package com.example.carchive.entities

data class Car(
    val id: Int,
    val marka: String,
    val model: String,
    val registration: String,
    val location: String,
    val kilometers: String,
    val rentSell: Boolean,
    val price: Double,
    val imageCar: String
)
