package com.example.carchive.entities

data class Contact(
    val id : Int,
    val firstName : String,
    val lastName : String,
    val pin : String,
    val country : String,
    val city : String,
    val address : String,
    val phoneNumber : String,
    val mobilePhoneNumber : String,
    val emailAddress : String,
    val description : String,
    val state : String,
    val activity : ContactActivity,
    val offerState : ContactOfferState
)
