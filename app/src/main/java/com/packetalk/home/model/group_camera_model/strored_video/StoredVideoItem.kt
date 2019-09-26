package com.packetalk.home.model.group_camera_model.strored_video


import com.google.gson.annotations.SerializedName

data class StoredVideoItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)