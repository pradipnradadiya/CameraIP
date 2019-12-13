package com.packetalk.chart.model


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("AverageStatus")
    val averageStatus: AverageStatus,
    @SerializedName("Header")
    val header: String,
    @SerializedName("HeaderDate")
    val headerDate: String,
    @SerializedName("Lable")
    val lable: ArrayList<String>,
    @SerializedName("Value")
    val value: ArrayList<Float>
)