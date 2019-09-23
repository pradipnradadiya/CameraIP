package com.packetalk.setting.request.trailor.group


import com.google.gson.annotations.SerializedName
import com.packetalk.setting.model.trailer.Object

data class AssignUserToGroupRequest(
    @SerializedName("GroupID")
    val groupId: String,
    @SerializedName("UserIdList")
    val objectX: ArrayList<Int>
)