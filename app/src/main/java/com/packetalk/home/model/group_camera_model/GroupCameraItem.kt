package com.packetalk.home.model.group_camera_model


import com.google.gson.annotations.SerializedName

data class GroupCameraItem(
    @SerializedName("Object")
    val groups: ArrayList<Groups>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)