package com.example.nearbyplace.model.directions


import com.google.gson.annotations.SerializedName

data class OverviewPolyline(
    @SerializedName("points")
    var points: String
)