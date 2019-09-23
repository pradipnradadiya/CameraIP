package com.packetalk.user.model.log


import com.google.gson.annotations.SerializedName

data class LogItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)