package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class CompanyResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("pin")
	val pin: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
