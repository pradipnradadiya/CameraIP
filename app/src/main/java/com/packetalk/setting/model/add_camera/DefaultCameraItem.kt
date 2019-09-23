package com.packetalk.setting.model.add_camera


import com.google.gson.annotations.SerializedName
import com.packetalk.home.model.group_camera_model.CameraDetailsFull

data class DefaultCameraItem(
    @SerializedName("Object")
//    val objectX: List<Object>,
    val objectX: ArrayList<CameraDetailsFull>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)