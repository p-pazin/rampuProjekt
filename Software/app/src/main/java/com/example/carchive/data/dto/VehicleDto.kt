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
    val cubicCapacity: Double,
    val enginePower: Double,
    val color: String,
    val driveType: String,
    val price: Double,
    val transmissionType: String,
    val type: String,
    val condition: String,
    val registeredTo: String
)

data class VehicleDtoPost(
    val registration: String,
    val state: Int,
    val brand: String,
    val mileage: Int,
    val productionYear: Int,
    val model: String,
    val engine: String,
    val cubicCapacity: Double,
    val enginePower: Double,
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
        type = type,
        productionYear = productionYear.toString(),
        registration = registration,
        kilometers = mileage,
        color = color,
        motor = engine,
        enginePower = enginePower,
        gearbox = Vehicle.GearboxType.getByExternalName(transmissionType),
        rentSell = state,
        price = price,
        imageCar = "",
        cubicCapacity = cubicCapacity,
        registeredTo = registeredTo,
        driveType = driveType,
        condition = condition,
        location = ""
    )

fun Vehicle.toDto() =
    VehicleDto(
        id = id,
        registration = registration,
        state = rentSell,
        brand = brand,
        mileage = kilometers,
        productionYear = productionYear.toIntOrNull() ?: 0,
        model = model,
        engine = motor,
        cubicCapacity = cubicCapacity,
        enginePower = enginePower,
        color = color,
        driveType = driveType,
        price = price,
        transmissionType = gearbox.externalName,
        type = type.toString(),
        condition = condition,
        registeredTo = registeredTo
    )

