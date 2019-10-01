package com.packetalk.Trailer.fragment.model.trailer


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("Community")
    val community: Any,
    @SerializedName("DNS")
    val dNS: String,
    @SerializedName("EngineStatus")
    val engineStatus: String,
    @SerializedName("IP")
    val iP: String,
    @SerializedName("IsHybrid")
    val isHybrid: Int,
    @SerializedName("OID")
    val oID: Any,
    @SerializedName("Object")
    val objectX: Any,
    @SerializedName("PointerValue")
    val pointerValue: Double,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any,
    @SerializedName("Risk")
    val risk: String,
    @SerializedName("RouterID")
    val routerID: Double,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("TrailerName")
    val trailerName: String,
    @SerializedName("Type")
    val type: String,
    var isOn: Boolean = false
)