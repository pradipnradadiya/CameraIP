package com.packetalk.setting.model.add_camera


import com.google.gson.annotations.SerializedName

data class ObjectX(
    @SerializedName("Cameraname")
    val cameraname: Any,
    @SerializedName("IsDeleted")
    val isDeleted: Any,
    @SerializedName("Port")
    val port: Any,
    @SerializedName("RouterStatus")
    val routerStatus: Any,
    @SerializedName("ServerID")
    val serverID: Int,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("Uri")
    val uri: String
)