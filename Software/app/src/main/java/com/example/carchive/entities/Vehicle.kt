package com.example.carchive.entities


data class Vehicle(
    val id: Int,
    val registration: String,
    val rentSell: Boolean,
    val brand: String,
    val kilometers: Int,
    val productionYear: String,
    val model: String,
    val motor: String,
    val cubicCapacity: Double,
    val enginePower: Double,
    val registeredTo: String,
    val color: String,
    val driveType: String,
    val price: Double,
    val gearbox: GearboxType,
    val type: String,
    val condition: String,
    val location: String,
    val imageCar: String
){
    enum class GearboxType(val externalName : String){
        MANUAL("Manual"),
        AUTOMATIC("Automatic"),
        UNKNOWN("Unknown");
        companion object{
            fun getByExternalName(value : String):GearboxType =
                if (value.equals(MANUAL.externalName, ignoreCase = true)){
                    MANUAL
                }else if(value.equals(MANUAL.externalName, ignoreCase = true)){
                    AUTOMATIC
                }
                else{
                    UNKNOWN
                }
        }

    }
}

