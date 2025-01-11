package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class VehicleResponse(

	@field:SerializedName("registeredTo")
	val registeredTo: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("driveType")
	val driveType: String? = null,

	@field:SerializedName("cubicCapacity")
	val cubicCapacity: Int? = null,

	@field:SerializedName("productionYear")
	val productionYear: Int? = null,

	@field:SerializedName("enginePower")
	val enginePower: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("condition")
	val condition: String? = null,

	@field:SerializedName("engine")
	val engine: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("registration")
	val registration: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("transmissionType")
	val transmissionType: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: Int? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("mileage")
	val mileage: Int? = null
)
