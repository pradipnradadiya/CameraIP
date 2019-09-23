package com.packetalk.map.fragment.model


import com.google.gson.annotations.SerializedName

data class MapItem(
    @SerializedName("Object")
    val objectX: Object,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)