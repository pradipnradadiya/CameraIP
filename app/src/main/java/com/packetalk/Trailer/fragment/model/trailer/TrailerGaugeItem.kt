package com.packetalk.Trailer.fragment.model.trailer


import com.google.gson.annotations.SerializedName

data class TrailerGaugeItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>?,
    @SerializedName("ResponseResult")
    val responseResult: Boolean?,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any?
)