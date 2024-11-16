package com.example.carchive.entities

import java.io.Serializable

data class Car(
    val id: Int,
    val marka: String,
    val model: String,
    val type: Double,
    val productionYear: String,
    val registration: String,
    val kilometers: Int,
    val location: String,
    val motor: String,
    val enginePower: Int,
    val gearbox: String,
    val rentSell: Boolean,
    val price: Double,
    val imageCar: String
): Serializable
