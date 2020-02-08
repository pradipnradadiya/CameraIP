package com.packetalk.login.model
import com.google.gson.annotations.SerializedName

data class LoginItem(
    @SerializedName("Base64PWD")
    val base64PWD: String,
    @SerializedName("cameraurl")
    val cameraurl: Any,
    @SerializedName("ErrorMsg")
    val errorMsg: Any,
    @SerializedName("IsAuthenticate")
    val isAuthenticate: Boolean,
    @SerializedName("IsLocalUser")
    val isLocalUser: Boolean,
    @SerializedName("MemberID")
    val memberID: Int,
    @SerializedName("memberTypes")
    val memberTypes: Int,
    @SerializedName("Status")
    val status: String,
    @SerializedName("url")
    val url: Any,
    @SerializedName("UserName")
    val userName: String
)