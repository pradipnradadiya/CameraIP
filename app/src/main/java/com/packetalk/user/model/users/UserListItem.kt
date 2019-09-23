package com.packetalk.user.model.users


import com.google.gson.annotations.SerializedName

data class UserListItem(
    @SerializedName("Object")
    val objectX: ArrayList<Object>,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any
)