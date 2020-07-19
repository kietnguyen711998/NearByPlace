package com.example.nearbyplace.model


import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    var location: Location,
    @SerializedName("viewport")
    var viewport: Viewport
)