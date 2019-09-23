package com.packetalk.setting.request.trailor


import com.google.gson.annotations.SerializedName
import com.packetalk.setting.model.trailer.Object

data class TrailerRequest(
    @SerializedName("Trailers")
    val objectX: ArrayList<Object>
)