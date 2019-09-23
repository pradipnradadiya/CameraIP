package com.packetalk.setting.request.trailor.my_camera_setting


import com.google.gson.annotations.SerializedName

data class MyCameraSettingPriorityRequest(
    @SerializedName("AdminCameraID_PKs")
    val objectX: ArrayList<Int>,

    @SerializedName("Priority")
    val objectY: ArrayList<Int>
)