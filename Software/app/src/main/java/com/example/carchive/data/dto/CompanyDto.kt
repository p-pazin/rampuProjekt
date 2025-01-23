package com.example.carchive.data.dto

data class CompanyDto(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val pin: String,
    val approved: Int
) {
    companion object {
        val EMPTY = CompanyDto(
            id = 0,
            name = "",
            city = "",
            address = "",
            pin = "",
            approved = 0
        )
    }
}
