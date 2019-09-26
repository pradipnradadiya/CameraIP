package com.packetalk.home.model.group_camera_model.strored_video


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("DateTime")
    val dateTime: String,
    @SerializedName("Duration")
    val duration: String,
    @SerializedName("FileName")
    val fileName: String,
    @SerializedName("FileNo")
    val fileNo: Int,
    @SerializedName("FileSize")
    val fileSize: String,
    @SerializedName("FolderName")
    val folderName: String
)