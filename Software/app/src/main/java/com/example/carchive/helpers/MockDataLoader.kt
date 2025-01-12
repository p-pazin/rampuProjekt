package com.example.carchive.helpers

import com.example.carchive.entities.Vehicle
import com.example.carchive.entities.Contact
import com.example.carchive.entities.User

object MockDataLoader {

    val vehicles: MutableList<Vehicle> = mutableListOf(
        Vehicle(
            id = 1,
            brand = "BMW",
            model = "320d",
            type = 2.0,
            productionYear = "2018",
            registration = "VT 420 PP",
            kilometers = 50000,
            location = "Varaždin",
            motor = "Dizel",
            enginePower = 190,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 20000.0,
            imageCar = "https://example.com/images/bmw_320d.jpg"
        ),
        Vehicle(
            id = 2,
            brand = "Audi",
            model = "A4",
            type = 1.8,
            productionYear = "2017",
            registration = "VZ 110 AV",
            kilometers = 30000,
            location = "Ludbreg",
            motor = "Benzin",
            enginePower = 150,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 300.0,
            imageCar = "https://example.com/images/audi_a4.jpg"
        ),
        Vehicle(
            id = 3,
            brand = "Mercedes",
            model = "C200",
            type = 2.0,
            productionYear = "2019",
            registration = "ZG 789 CD",
            kilometers = 40000,
            location = "Zagreb",
            motor = "Dizel",
            enginePower = 160,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 25000.0,
            imageCar = "https://example.com/images/mercedes_c200.jpg"
        ),
        Vehicle(
            id = 4,
            brand = "Volkswagen",
            model = "Golf 7",
            type = 1.6,
            productionYear = "2016",
            registration = "DU 302 AB",
            kilometers = 70000,
            location = "Dubrovnik",
            motor = "Dizel",
            enginePower = 115,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 280.0,
            imageCar = "https://example.com/images/vw_golf7.jpg"
        ),
        Vehicle(
            id = 5,
            brand = "Toyota",
            model = "Corolla",
            type = 1.8,
            productionYear = "2020",
            registration = "ST 600 GT",
            kilometers = 15000,
            location = "Split",
            motor = "Hibrid",
            enginePower = 122,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 22000.0,
            imageCar = "https://example.com/images/toyota_corolla.jpg"
        ),
        Vehicle(
            id = 6,
            brand = "Renault",
            model = "Clio",
            type = 1.2,
            productionYear = "2015",
            registration = "OS 405 RE",
            kilometers = 90000,
            location = "Osijek",
            motor = "Benzin",
            enginePower = 90,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 250.0,
            imageCar = "https://example.com/images/renault_clio.jpg"
        ),
        Vehicle(
            id = 7,
            brand = "Ford",
            model = "Focus",
            type = 1.5,
            productionYear = "2018",
            registration = "KA 543 FO",
            kilometers = 60000,
            location = "Karlovac",
            motor = "Dizel",
            enginePower = 120,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 18000.0,
            imageCar = "https://example.com/images/ford_focus.jpg"
        ),
        Vehicle(
            id = 8,
            brand = "Opel",
            model = "Astra",
            type = 1.4,
            productionYear = "2017",
            registration = "RI 221 OP",
            kilometers = 75000,
            location = "Rijeka",
            motor = "Benzin",
            enginePower = 140,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 260.0,
            imageCar = "https://example.com/images/opel_astra.jpg"
        ),
        Vehicle(
            id = 9,
            brand = "Honda",
            model = "Civic",
            type = 1.6,
            productionYear = "2019",
            registration = "ZD 321 HO",
            kilometers = 20000,
            location = "Zadar",
            motor = "Benzin",
            enginePower = 182,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = false,
            price = 21000.0,
            imageCar = "https://example.com/images/honda_civic.jpg"
        ),
        Vehicle(
            id = 10,
            brand = "Skoda",
            model = "Octavia",
            type = 2.0,
            productionYear = "2021",
            registration = "SL 875 SK",
            kilometers = 10000,
            location = "Slavonski Brod",
            motor = "Dizel",
            enginePower = 150,
            gearbox = Vehicle.GearboxType.AUTOMATIC,
            rentSell = true,
            price = 320.0,
            imageCar = "https://example.com/images/skoda_octavia.jpg"
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

    fun getMockContacts() : MutableList<Contact> = mutableListOf(
        Contact(1, "Ivo", "Ivić", "97626517542", "Hrvatska", "Daruvar",
            "Gradska 21", "-", "0987863451", "iivic@mail.hr",
            "Zainteresiran za obiteljski automobil.",
            "Kupnja",
            "Aktivni kontakt",
            false
        ),
        Contact(2, "Ana", "Anić", "89722475418", "Hrvatska", "Rijeka",
            "Kratka 12", "-", "0998796542", "aanic@mail.hr",
            "-",
            "Kupnja",
            "Neaktivni kontakt",
            true
        ),
        Contact(3, "Mirko", "Filipović", "88236556441", "Hrvatska", "Vinkovci",
            "Augusta Šenoe 17", "-", "098786241", "mfilipovic@mail.hr",
            "Treba mu automobil zbog putovanja na posao.",
            "Najam",
            "Aktivni kontakt",
            false
        ),

    )

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