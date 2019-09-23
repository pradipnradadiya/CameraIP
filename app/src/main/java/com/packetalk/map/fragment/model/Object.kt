package com.packetalk.map.fragment.model


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("CamerasLocations")
    val camerasLocations: List<Any>,
    @SerializedName("GPSLocations")
    val gPSLocations: ArrayList<GPSLocation>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseText")
    val responseText: Any
)