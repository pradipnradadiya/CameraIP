package com.packetalk.setting.model.add_camera


import com.google.gson.annotations.SerializedName
import com.packetalk.home.model.group_camera_model.CameraDetailsFull

data class CamToGroupRequest(
    @SerializedName("GroupID")
    val groupId: Int,
    @SerializedName("Cameras")
    val objectX: ArrayList<CameraDetailsFull>

)