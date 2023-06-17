package com.example.testovoe14

import com.google.gson.annotations.SerializedName

data class Crypta(
    @SerializedName("itemNumber") val itemNumber : Int,
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("isBought") var isBought : Boolean,
    @SerializedName("image") val image : String
)
