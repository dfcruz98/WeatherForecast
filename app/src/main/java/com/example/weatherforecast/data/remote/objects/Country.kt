package com.example.weatherforecast.data.remote.objects

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country")
    val initials: String,
)
