package com.packetalk.user.model.assign_camera_user


import com.google.gson.annotations.SerializedName
import com.packetalk.home.model.group_camera_model.CameraDetailsFull

data class AssignCameraItem(
    @SerializedName("Object")
    val objectX: ArrayList<CameraDetailsFull>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)