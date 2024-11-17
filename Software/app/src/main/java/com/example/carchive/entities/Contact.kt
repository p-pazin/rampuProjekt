package com.example.carchive.entities

import java.io.Serializable


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
    val activity : String,
    val state : String,
    val offerSent : Boolean
) : Serializable
