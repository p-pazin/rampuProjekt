package com.example.carchive.helpers

import com.example.carchive.entities.Car
import com.example.carchive.entities.Contact
import com.example.carchive.entities.ContactActivity
import com.example.carchive.R
import com.example.carchive.entities.ContactOfferState
import com.example.carchive.entities.User

object MockDataLoader {

    val cars = mutableListOf(
        Car(
            id = 1,
            marka = "BMW",
            model = "320d",
            type = 2.0,
            productionYear = "2018",
            registration = "VT 420 PP",
            kilometers = 50000,
            location = "Varaždin",
            motor = "Dizel",
            enginePower = 190,
            gearbox = "Automatski",
            rentSell = false,
            price = 20000.0,
            imageCar = "https://example.com/images/bmw_320d.jpg"
        ),
        Car(
            id = 2,
            marka = "Audi",
            model = "A4",
            type = 1.8,
            productionYear = "2017",
            registration = "VZ 110 AV",
            kilometers = 30000,
            location = "Ludbreg",
            motor = "Benzin",
            enginePower = 150,
            gearbox = "Manualni",
            rentSell = true,
            price = 300.0,
            imageCar = "https://example.com/images/audi_a4.jpg"
        ),
        Car(
            id = 3,
            marka = "Mercedes",
            model = "C200",
            type = 2.0,
            productionYear = "2019",
            registration = "ZG 789 CD",
            kilometers = 40000,
            location = "Zagreb",
            motor = "Dizel",
            enginePower = 160,
            gearbox = "Automatski",
            rentSell = false,
            price = 25000.0,
            imageCar = "https://example.com/images/mercedes_c200.jpg"
        ),
        Car(
            id = 4,
            marka = "Volkswagen",
            model = "Golf 7",
            type = 1.6,
            productionYear = "2016",
            registration = "DU 302 AB",
            kilometers = 70000,
            location = "Dubrovnik",
            motor = "Dizel",
            enginePower = 115,
            gearbox = "Manualni",
            rentSell = true,
            price = 280.0,
            imageCar = "https://example.com/images/vw_golf7.jpg"
        ),
        Car(
            id = 5,
            marka = "Toyota",
            model = "Corolla",
            type = 1.8,
            productionYear = "2020",
            registration = "ST 600 GT",
            kilometers = 15000,
            location = "Split",
            motor = "Hibrid",
            enginePower = 122,
            gearbox = "Automatski",
            rentSell = false,
            price = 22000.0,
            imageCar = "https://example.com/images/toyota_corolla.jpg"
        ),
        Car(
            id = 6,
            marka = "Renault",
            model = "Clio",
            type = 1.2,
            productionYear = "2015",
            registration = "OS 405 RE",
            kilometers = 90000,
            location = "Osijek",
            motor = "Benzin",
            enginePower = 90,
            gearbox = "Manualni",
            rentSell = true,
            price = 250.0,
            imageCar = "https://example.com/images/renault_clio.jpg"
        ),
        Car(
            id = 7,
            marka = "Ford",
            model = "Focus",
            type = 1.5,
            productionYear = "2018",
            registration = "KA 543 FO",
            kilometers = 60000,
            location = "Karlovac",
            motor = "Dizel",
            enginePower = 120,
            gearbox = "Manualni",
            rentSell = false,
            price = 18000.0,
            imageCar = "https://example.com/images/ford_focus.jpg"
        ),
        Car(
            id = 8,
            marka = "Opel",
            model = "Astra",
            type = 1.4,
            productionYear = "2017",
            registration = "RI 221 OP",
            kilometers = 75000,
            location = "Rijeka",
            motor = "Benzin",
            enginePower = 140,
            gearbox = "Manualni",
            rentSell = true,
            price = 260.0,
            imageCar = "https://example.com/images/opel_astra.jpg"
        ),
        Car(
            id = 9,
            marka = "Honda",
            model = "Civic",
            type = 1.6,
            productionYear = "2019",
            registration = "ZD 321 HO",
            kilometers = 20000,
            location = "Zadar",
            motor = "Benzin",
            enginePower = 182,
            gearbox = "Automatski",
            rentSell = false,
            price = 21000.0,
            imageCar = "https://example.com/images/honda_civic.jpg"
        ),
        Car(
            id = 10,
            marka = "Skoda",
            model = "Octavia",
            type = 2.0,
            productionYear = "2021",
            registration = "SL 875 SK",
            kilometers = 10000,
            location = "Slavonski Brod",
            motor = "Dizel",
            enginePower = 150,
            gearbox = "Automatski",
            rentSell = true,
            price = 320.0,
            imageCar = "https://example.com/images/skoda_octavia.jpg"
        )
    )


    fun getMockCarList(): MutableList<Car> {
        return cars;
    }

    fun addCar(car: Car){
        cars.add(car)
    }

    fun editCar(car: Car){
        val registracija = car.registration
        val carIndex = cars.indexOfFirst { it.registration == registracija }
        cars[carIndex] = car
    }

    fun deleteCar(carId: Int) {
        cars.removeAll { it.id == carId }
    }

    fun getMockContacts() : List<Contact> = listOf(
        Contact(1, "Ivo", "Ivić", "97626517542", "Hrvatska", "Daruvar",
            "Gradska 21", "-", "0987863451", "iivic@mail.hr",
            "Zainteresiran za obiteljski automobil.",
            "Kupnja",
            ContactActivity("Aktivni kontakt", R.drawable.ic_aktivan_kontakt),
            ContactOfferState("Ponuda nije poslana", R.drawable.ic_x)
        ),
        Contact(2, "Ana", "Anić", "89722475418", "Hrvatska", "Rijeka",
            "Kratka 12", "-", "0998796542", "aanic@mail.hr",
            "-",
            "Kupnja",
            ContactActivity("Neaktivni kontakt", R.drawable.ic_neaktivan_kontakt),
            ContactOfferState("Ponuda poslana", R.drawable.ic_check)
        ),
        Contact(3, "Mirko", "Filipović", "88236556441", "Hrvatska", "Vinkovci",
            "Augusta Šenoe 17", "-", "098786241", "mfilipovic@mail.hr",
            "Treba mu automobil zbog putovanja na posao.",
            "Najam",
            ContactActivity("Aktivni kontakt", R.drawable.ic_aktivan_kontakt),
            ContactOfferState("Ponuda nije poslana", R.drawable.ic_x)
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