package com.example.nearbyplace.model


import com.google.gson.annotations.SerializedName

data class Viewport(
    @SerializedName("northeast")
    var northeast: Northeast,
    @SerializedName("southwest")
    var southwest: Southwest
)