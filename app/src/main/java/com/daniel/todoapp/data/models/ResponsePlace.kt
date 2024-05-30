package com.daniel.todoapp.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponsePlace(
	@field:SerializedName("data")
	val data: List<Place>? = null
) : Parcelable

@Parcelize
data class Place(
	@field:SerializedName("id")
	val idPlace: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("formatted_address")
	val address: String? = null,

	@field:SerializedName("address_component")
	val addressComponents: List<String>? = null,

	@field:SerializedName("rating")
	val rating: Double? = 0.0,

	@field:SerializedName("total_ratings")
	val totalRatings: Int? = 0,

	@field:SerializedName("photos")
	val photos: List<String>? = null
) : Parcelable
