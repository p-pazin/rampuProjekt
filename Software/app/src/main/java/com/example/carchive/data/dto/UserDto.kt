package com.example.carchive.data.dto

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String
) {
    companion object {
        val EMPTY = UserDto(
            id = 0,
            firstName = "",
            lastName = "",
            email = ""
        )
    }
}