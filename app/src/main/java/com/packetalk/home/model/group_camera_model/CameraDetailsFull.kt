package com.packetalk.home.model.group_camera_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CameraDetailsFull(
    @SerializedName("AdminCameraID_PK")
    val adminCameraIDPK: Int,
    @SerializedName("CameraIDOnDB")
    val cameraIDOnDB: Int,
    @SerializedName("CameraIDOnServer")
    val cameraIDOnServer: Int,
    @SerializedName("CameraName")
    val cameraName: String,
    @SerializedName("Community")
    val community: Any,
    @SerializedName("DNS")
    val dNS: Any,
    @SerializedName("EngineStatus")
    val engineStatus: Any,
    @SerializedName("FullServerUri")
    val fullServerUri: Any,
    @SerializedName("GroupId")
    val groupId: Int,
    @SerializedName("GroupName")
    val groupName: String,
    @SerializedName("IP")
    val iP: String,
    @SerializedName("Image1")
    val image1: String,
    @SerializedName("Image2")
    val image2: String,
    @SerializedName("IsHide")
    val isHide: Any,
    @SerializedName("MODIFIED_DTE")
    val mODIFIEDDTE: String,
    @SerializedName("MemberID")
    val memberID: Int,
    @SerializedName("OID")
    val oID: Any,
    @SerializedName("Groups")
    val objectX: Any,
    @SerializedName("Password")
    val password: Any,
    @SerializedName("PointerValue")
    val pointerValue: Int,
    @SerializedName("Priority")
    var priority: Int,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any,
    @SerializedName("Risk")
    val risk: Any,
    @SerializedName("ServerID")
    val serverID: Int,
    @SerializedName("Server_Port")
    val serverPort: String,
    @SerializedName("ServerURL")
    val serverURL: String,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("TrailerName")
    val trailerName: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("USR_CAMERA_ID")
    val uSRCAMERAID: Int,
    @SerializedName("USR_CAMERA_ID_PK")
    val uSRCAMERAIDPK: Int,
    @SerializedName("UserName")
    val userName: Any,
    @SerializedName("userid")
    val userid: Any,
    var choice: Boolean = false,
    var isSelected: Boolean = false
) : Serializable