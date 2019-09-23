package com.packetalk.setting.model.trailer


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("Community")
    val community: String,
    @SerializedName("DNS")
    var dNS: String,
    @SerializedName("EngineStatus")
    val engineStatus: Any,
    @SerializedName("IP")
    val iP: String,
    @SerializedName("OID")
    val oID: String,
    @SerializedName("Object")
    val objectX: Any,
    @SerializedName("PointerValue")
    val pointerValue: Double,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any,
    @SerializedName("Risk")
    val risk: Any,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("TrailerName")
    var trailerName: String,
    @SerializedName("Type")
    val type: String
)