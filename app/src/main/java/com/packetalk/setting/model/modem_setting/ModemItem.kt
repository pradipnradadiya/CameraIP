package com.packetalk.setting.model.modem_setting


import com.google.gson.annotations.SerializedName

data class ModemItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)