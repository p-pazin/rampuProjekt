package com.example.carchive.helpers

import com.example.carchive.entities.Car

object MockCarDataLoaderCars {

    fun getMockCarList(): List<Car> {
        return listOf(
            Car(
                id = 1,
                marka = "BMW",
                model = "320d",
                registration = "VT 420 PP",
                location = "Varaždin",
                kilometers = "50,000 km",
                rentSell = false,
                price = 20000.0,
                imageCar = ""
            ),
            Car(
                id = 2,
                marka = "Audi",
                model = "A4",
                registration = "VZ 110 AV",
                location = "Ludbreg",
                kilometers = "30,000 km",
                rentSell = true,
                price = 300.0,
                imageCar = ""
            )
        )
    }
}
