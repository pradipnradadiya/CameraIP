package com.packetalk.user.model.users


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("Created_Date")
    val createdDate: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("ID")
    val iD: String,
    @SerializedName("Password")
    val password: Any,
    @SerializedName("ServerID_FK")
    val serverIDFK: Int,
    @SerializedName("Username")
    val username: String,
    @SerializedName("usertype")
    val usertype: Int,
    var isNew:Boolean = false

)