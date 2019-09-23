package com.packetalk.setting.request.trailor.my_camera_setting


import com.google.gson.annotations.SerializedName
import com.packetalk.setting.model.trailer.Object

data class MyCameraSettingDeleteRequest(
    @SerializedName("AdminCameraID_PKs")
    val objectX: ArrayList<Int>
)