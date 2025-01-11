package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class InvoiceResponse(

	@field:SerializedName("vat")
	val vat: Int? = null,

	@field:SerializedName("paymentMethod")
	val paymentMethod: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("totalCost")
	val totalCost: Int? = null,

	@field:SerializedName("dateOfCreation")
	val dateOfCreation: String? = null,

	@field:SerializedName("mileage")
	val mileage: Int? = null
)
