package com.packetalk.home.model.group_camera_model


import com.google.gson.annotations.SerializedName

data class Groups(
    @SerializedName("CameraDetailsFull")
    var cameraDetailsFull: ArrayList<CameraDetailsFull>,
    @SerializedName("GroupID")
    val groupID: Int,
    @SerializedName("GroupName")
    var groupName: String,
    var isSelected: Boolean = false
) {
    override fun toString(): String {
        return groupName.trim()
    }
}