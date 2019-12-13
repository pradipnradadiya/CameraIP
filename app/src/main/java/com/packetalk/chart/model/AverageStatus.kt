package com.packetalk.chart.model


import com.google.gson.annotations.SerializedName

data class AverageStatus(
    @SerializedName("Average")
    val average: Double,
    @SerializedName("status")
    val status: String
)