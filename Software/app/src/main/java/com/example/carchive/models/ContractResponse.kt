package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class ContractResponse(

	@field:SerializedName("companyPin")
	val companyPin: String? = null,

	@field:SerializedName("contactPin")
	val contactPin: String? = null,

	@field:SerializedName("contactName")
	val contactName: String? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("signed")
	val signed: Int? = null,

	@field:SerializedName("vehicles")
	val vehicles: List<VehiclesItem?>? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("dateOfCreation")
	val dateOfCreation: String? = null,

	@field:SerializedName("vehicleCubicCapacity")
	val vehicleCubicCapacity: String? = null,

	@field:SerializedName("vehicleBrand")
	val vehicleBrand: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("companyAddress")
	val companyAddress: String? = null,

	@field:SerializedName("vehicleModel")
	val vehicleModel: String? = null,

	@field:SerializedName("contactAddress")
	val contactAddress: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("place")
	val place: String? = null,

	@field:SerializedName("vehicleRegistration")
	val vehicleRegistration: String? = null
)

data class VehiclesItem(

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
