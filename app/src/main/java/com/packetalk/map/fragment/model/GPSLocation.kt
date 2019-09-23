package com.packetalk.map.fragment.model


import com.google.gson.annotations.SerializedName
import com.packetalk.home.model.group_camera_model.CameraDetailsFull

data class GPSLocation(
    @SerializedName("account")
    val account: Any,
    @SerializedName("accuracy")
    val accuracy: Any,
    @SerializedName("Camdetails")
    val camdetails: ArrayList<CameraDetailsFull>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("method")
    val method: Any,
    @SerializedName("ReceiveDate")
    val receiveDate: String,
    @SerializedName("resource_url")
    val resourceUrl: Any,
    @SerializedName("_router")
    val _router: String,
    @SerializedName("router")
    val router: String,
    @SerializedName("TrailerNo")
    val trailerNo: String,
    @SerializedName("_updated_at")
    val _updatedAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)