package com.example.nearbyplace.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("business_status")
    var businessStatus: String,
    @SerializedName("geometry")
    var geometry: Geometry,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("opening_hours")
    var openingHours: OpeningHours,
    @SerializedName("photos")
    var photos: List<Photo>,
    @SerializedName("place_id")
    var placeId: String,
    @SerializedName("plus_code")
    var plusCode: PlusCode,
    @SerializedName("rating")
    var rating: Double,
    @SerializedName("reference")
    var reference: String,
    @SerializedName("scope")
    var scope: String,
    @SerializedName("types")
    var types: List<String>,
    @SerializedName("user_ratings_total")
    var userRatingsTotal: Int,
    @SerializedName("vicinity")
    var vicinity: String
)