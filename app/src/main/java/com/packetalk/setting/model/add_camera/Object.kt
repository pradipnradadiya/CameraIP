package com.packetalk.setting.model.add_camera


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("AdminCameraID_PK")
    val adminCameraIDPK: Int,
    @SerializedName("Cam_Latitude")
    val camLatitude: Any,
    @SerializedName("Cam_Longtitude")
    val camLongtitude: Any,
    @SerializedName("CameraIDOnDB")
    val cameraIDOnDB: Int,
    @SerializedName("CameraIDOnServer")
    val cameraIDOnServer: Int,
    @SerializedName("CameraName")
    val cameraName: Any,
    @SerializedName("CamerastatusOnserver")
    val camerastatusOnserver: Any,
    @SerializedName("GroupID")
    val groupID: Any,
    @SerializedName("Image_Camera")
    val imageCamera: Any,
    @SerializedName("IsNewCamera")
    val isNewCamera: Boolean,
    @SerializedName("Server_ID")
    val serverID: Int
)