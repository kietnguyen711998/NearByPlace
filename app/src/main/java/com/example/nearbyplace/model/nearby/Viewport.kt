package com.example.nearbyplace.model.nearby


import com.example.nearbyplace.model.nearby.Northeast
import com.example.nearbyplace.model.nearby.Southwest
import com.google.gson.annotations.SerializedName

data class Viewport(
    @SerializedName("northeast")
    var northeast: Northeast,
    @SerializedName("southwest")
    var southwest: Southwest
)