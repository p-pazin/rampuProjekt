package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class ReservationResponse(

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("maxMileage")
	val maxMileage: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: Int? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null,

	@field:SerializedName("dateOfCreation")
	val dateOfCreation: String? = null
)
