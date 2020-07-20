package com.example.nearbyplace.model.directions


import com.google.gson.annotations.SerializedName

data class DurationX(
    @SerializedName("text")
    var text: String,
    @SerializedName("value")
    var value: Int
)