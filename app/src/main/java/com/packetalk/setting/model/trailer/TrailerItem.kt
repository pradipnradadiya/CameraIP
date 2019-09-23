package com.packetalk.setting.model.trailer


import com.google.gson.annotations.SerializedName

data class TrailerItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)