package com.packetalk.setting.model.server_setting


import com.google.gson.annotations.SerializedName

data class ServerItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)