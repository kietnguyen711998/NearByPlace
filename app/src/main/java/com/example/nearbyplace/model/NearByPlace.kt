package com.example.nearbyplace.model


import com.google.gson.annotations.SerializedName

data class NearByPlace(
    @SerializedName("html_attributions")
    var htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    var nextPageToken: String,
    @SerializedName("results")
    var results: List<Result>,
    @SerializedName("status")
    var status: String
)