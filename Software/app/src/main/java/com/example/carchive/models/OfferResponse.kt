package com.example.carchive.models

import com.google.gson.annotations.SerializedName

data class OfferResponse(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("dateOfCreation")
	val dateOfCreation: String? = null
)
