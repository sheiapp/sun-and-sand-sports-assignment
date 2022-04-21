package com.sunandsandsports.assignment.model


import com.google.gson.annotations.SerializedName

data class RandomUserResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<RandomUserDetail>
)