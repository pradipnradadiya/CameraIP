package com.packetalk.Trailer.fragment.model.trailer
import com.google.gson.annotations.SerializedName

data class BattryVolRange(

    @SerializedName("IsFromDB") val isFromDB: Boolean,
    @SerializedName("VitalType") val vitalType: String,
    @SerializedName("GaugeType") val gaugeType: String,
    @SerializedName("Ranges") val ranges: Ranges
)