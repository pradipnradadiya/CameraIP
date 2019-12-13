package com.packetalk.chart.model


import com.google.gson.annotations.SerializedName

data class ChartItem(
    @SerializedName("Object")
    val objectX: Object,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)