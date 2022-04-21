package com.sunandsandsports.assignment.model


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("title")
    val title: String
) {
    fun getFullName(): String {
        return "$title, $first, $last"
    }
}