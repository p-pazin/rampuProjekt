package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class AdResponse(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("paymentMethod")
	val paymentMethod: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("dateOfPublishment")
	val dateOfPublishment: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null
)
