package com.packetalk.user.model.log


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("Action")
    val action: String,
    @SerializedName("Desc")
    val desc: String,
    @SerializedName("ID")
    val iD: Int
)