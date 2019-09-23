package com.packetalk.user.model.assign_camera_user


import com.google.gson.annotations.SerializedName
import com.packetalk.home.model.group_camera_model.CameraDetailsFull

data class PostAssignCamera(
    @SerializedName("UserCameraDetails")
    val objectX: ArrayList<CameraDetailsFull>,
    @SerializedName("MemberID")
    val memberId: String
)