package com.example.carchive.entities


data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val type: Double,
    val productionYear: String,
    val registration: String,
    val kilometers: Int,
    val location: String,
    val motor: String,
    val enginePower: Int,
    val gearbox: GearboxType,
    val rentSell: Boolean,
    val price: Double,
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

