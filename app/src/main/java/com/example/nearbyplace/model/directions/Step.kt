package com.example.nearbyplace.model.directions


import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("distance")
    var distance: DistanceX,
    @SerializedName("duration")
    var duration: DurationX,
    @SerializedName("end_location")
    var endLocation: EndLocationX,
    @SerializedName("html_instructions")
    var htmlInstructions: String,
    @SerializedName("maneuver")
    var maneuver: String,
    @SerializedName("polyline")
    var polyline: Polyline,
    @SerializedName("start_location")
    var startLocation: StartLocationX,
    @SerializedName("travel_mode")
    var travelMode: String
)