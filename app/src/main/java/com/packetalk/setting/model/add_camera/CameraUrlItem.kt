package com.packetalk.setting.model.add_camera


import com.google.gson.annotations.SerializedName

data class CameraUrlItem(
    @SerializedName("Object")
    val objectX: ArrayList<ObjectX>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)