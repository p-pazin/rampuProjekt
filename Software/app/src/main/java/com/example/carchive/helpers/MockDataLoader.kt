package com.example.carchive.helpers

import com.example.carchive.entities.User

object MockDataLoader {
    private val users = mutableListOf(
        User(1, "Ivo", "Ivić", "97626517542", "iivic@mail.hr", "Marko123"),
        User(2, "Pero", "Kos", "0911234123", "pkos@mail.hr", "Marko123"),
        User(3, "Stefan", "Ludbreg", "0956767678", "sludbreg@mail.hr", "Marko123")
    )

    fun getLastID(): Int {
        return users.maxByOrNull { it.id }?.id ?: 0
    }

    fun getUsers():List<User> = users

    fun addUser(user: User){
        users.add(user)
    }
}