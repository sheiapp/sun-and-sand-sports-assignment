package com.sunandsandsports.assignment.model


import com.google.gson.annotations.SerializedName

data class Dob(
    @SerializedName("age")
    val age: String,
    @SerializedName("date")
    val date: String
)