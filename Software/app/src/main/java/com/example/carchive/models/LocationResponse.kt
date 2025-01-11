package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class LocationResponse(

	@field:SerializedName("latitude")
	val latitude: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Int? = null
)
