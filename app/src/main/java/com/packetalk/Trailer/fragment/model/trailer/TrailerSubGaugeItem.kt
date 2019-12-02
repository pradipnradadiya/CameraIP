package com.packetalk.Trailer.fragment.model.trailer


import com.google.gson.annotations.SerializedName

data class TrailerSubGaugeItem(
    @SerializedName("Object")
    val objectX: ObjectXX,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)