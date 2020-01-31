package com.packetalk.user.model


import com.google.gson.annotations.SerializedName

data class CommonItem(
    @SerializedName("Object")
    val objectX: Any,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)