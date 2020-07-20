package com.example.nearbyplace.model.directions


import com.google.gson.annotations.SerializedName

data class ViaWaypoint(
    @SerializedName("location")
    var location: Location,
    @SerializedName("step_index")
    var stepIndex: Int,
    @SerializedName("step_interpolation")
    var stepInterpolation: Double
)