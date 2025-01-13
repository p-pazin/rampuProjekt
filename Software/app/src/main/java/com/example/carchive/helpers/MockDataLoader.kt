package com.example.carchive.helpers

import com.example.carchive.entities.Vehicle
import com.example.carchive.entities.Contact
import com.example.carchive.entities.User
import java.time.LocalDate

object MockDataLoader {

    val vehicles: MutableList<Vehicle> = mutableListOf(
        Vehicle(
            id = 1,
            brand = "BMW",
            model = "320d",
            type = "",
            productionYear = "2018",
            registration = "VT 420 PP",
            kilometers = 50000,
            location = "Varaždin",
            motor = "Dizel",
            enginePower = 190.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 20000.0,
            imageCar = "https://example.com/images/bmw_320d.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO(),
        ),
        Vehicle(
            id = 2,
            brand = "Audi",
            model = "A4",
            type = "",
            productionYear = "2017",
            registration = "VZ 110 AV",
            kilometers = 30000,
            location = "Ludbreg",
            motor = "Benzin",
            enginePower = 150.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 300.0,
            imageCar = "https://example.com/images/audi_a4.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 3,
            brand = "Mercedes",
            model = "C200",
            type = "",
            productionYear = "2019",
            registration = "ZG 789 CD",
            kilometers = 40000,
            location = "Zagreb",
            motor = "Dizel",
            enginePower = 160.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 25000.0,
            imageCar = "https://example.com/images/mercedes_c200.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 4,
            brand = "Volkswagen",
            model = "Golf 7",
            type = "",
            productionYear = "2016",
            registration = "DU 302 AB",
            kilometers = 70000,
            location = "Dubrovnik",
            motor = "Dizel",
            enginePower = 115.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 280.0,
            imageCar = "https://example.com/images/vw_golf7.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 5,
            brand = "Toyota",
            model = "Corolla",
            type = "",
            productionYear = "2020",
            registration = "ST 600 GT",
            kilometers = 15000,
            location = "Split",
            motor = "Hibrid",
            enginePower = 122.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 22000.0,
            imageCar = "https://example.com/images/toyota_corolla.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 6,
            brand = "Renault",
            model = "Clio",
            type = "",
            productionYear = "2015",
            registration = "OS 405 RE",
            kilometers = 90000,
            location = "Osijek",
            motor = "Benzin",
            enginePower = 90.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 250.0,
            imageCar = "https://example.com/images/renault_clio.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 7,
            brand = "Ford",
            model = "Focus",
            type = "",
            productionYear = "2018",
            registration = "KA 543 FO",
            kilometers = 60000,
            location = "Karlovac",
            motor = "Dizel",
            enginePower = 120.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 18000.0,
            imageCar = "https://example.com/images/ford_focus.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 8,
            brand = "Opel",
            model = "Astra",
            type = "",
            productionYear = "2017",
            registration = "RI 221 OP",
            kilometers = 75000,
            location = "Rijeka",
            motor = "Benzin",
            enginePower = 140.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 260.0,
            imageCar = "https://example.com/images/opel_astra.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 9,
            brand = "Honda",
            model = "Civic",
            type = "",
            productionYear = "2019",
            registration = "ZD 321 HO",
            kilometers = 20000,
            location = "Zadar",
            motor = "Benzin",
            enginePower = 182.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 21000.0,
            imageCar = "https://example.com/images/honda_civic.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        ),
        Vehicle(
            id = 10,
            brand = "Skoda",
            model = "Octavia",
            type = "",
            productionYear = "2021",
            registration = "SL 875 SK",
            kilometers = 10000,
            location = "Slavonski Brod",
            motor = "Dizel",
            enginePower = 150.0,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 320.0,
            imageCar = "https://example.com/images/skoda_octavia.jpg",
            color = "",
            cubicCapacity = TODO(),
            registeredTo = TODO(),
            driveType = TODO(),
            condition = TODO()
        )
    )


    fun getMockCarList(): MutableList<Vehicle> {
        return vehicles;
    }
    fun addCar(vehicle: Vehicle){
        vehicles.add(vehicle)
    }

    fun editCar(vehicle: Vehicle){
        val registracija = vehicle.registration
        val carIndex = vehicles.indexOfFirst { it.registration == registracija }
        vehicles[carIndex] = vehicle
    }

    fun deleteCar(carId: Int) {
        vehicles.removeAll { it.id == carId }
    }

    private val users = mutableListOf(
        User(1, "Ivo", "Ivić", "97626517542", "iivic@mail.hr", "Marko123"),
        User(2, "Pero", "Kos", "0911234123", "pkos@mail.hr", "Marko123"),
        User(3, "Stefan", "Ludbreg", "0956767678", "sludbreg@mail.hr", "Marko123"),
        User(4, "Stefan", "Ludbreg", "0956767678", "a", "1")
    )

    fun getUsers(): List<User> = users

    fun addUser(user: User) {
        users.add(user)
    }

    fun getLastID(): Int {
        return users.maxByOrNull { it.id }?.id ?: 0
    }

}