package com.example.carchive.data.dto

import com.example.carchive.entities.Vehicle


data class VehicleDto(
    val id: Int,
    val registration: String,
    val state: Int,
    val brand: String,
    val mileage: Int,
    val productionYear: Int,
    val model: String,
    val engine: String,
    val cubicCapacity: Int,
    val enginePower: Int,
    val registeredTo: String,
    val color: String,
    val driveType: String,
    val price: Double,
    val transmissionType: String,
    val type: String,
    val condition: String
)

fun VehicleDto.toEntity() =
    Vehicle(
        id = id,
        brand = brand,
        model = model,
        type = 0.0,
        productionYear = "",
        registration = registration,
        kilometers = mileage,
        location = "",
        motor = "",
        enginePower = enginePower,
        gearbox = Vehicle.GearboxType.getByExternalName(transmissionType),
        rentSell = true,
        price = price,
        imageCar = ""
    )
