package com.sunandsandsports.assignment.model


import com.google.gson.annotations.SerializedName


data class RandomUserDetail(
    @SerializedName("dob")
    val dob: Dob,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: Name,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("picture")
    val picture: Picture
)