package com.example.carchive.data.dto

data class ContractDto (
    val id: Int,
    val title: String,
    val place: String,
    val dateOfCreation: String,
    val type: Int,
    val content: String,
    val signed: Int,
    val contactName: String?
)