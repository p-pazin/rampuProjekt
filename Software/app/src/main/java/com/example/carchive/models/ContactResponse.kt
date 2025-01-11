package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class ContactResponse(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("telephoneNumber")
	val telephoneNumber: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("mobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("dateOfCreation")
	val dateOfCreation: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("pin")
	val pin: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
