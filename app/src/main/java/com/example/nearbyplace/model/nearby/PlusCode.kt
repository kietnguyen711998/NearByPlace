package com.example.nearbyplace.model.nearby


import com.google.gson.annotations.SerializedName

data class PlusCode(
    @SerializedName("compound_code")
    var compoundCode: String,
    @SerializedName("global_code")
    var globalCode: String
)