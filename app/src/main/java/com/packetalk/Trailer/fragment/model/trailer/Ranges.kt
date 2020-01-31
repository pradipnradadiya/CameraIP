package com.packetalk.Trailer.fragment.model.trailer

import com.google.gson.annotations.SerializedName

data class Ranges(
    @SerializedName("Normal") val normal: ArrayList<Float>,
    @SerializedName("Warning") val warning: ArrayList<Float>,
    @SerializedName("Critical") val critical: ArrayList<Float>,
    @SerializedName("HigherCritical") val higherCritical: ArrayList<Float>,
    @SerializedName("HigherWarning") val higherWarning: ArrayList<Float>
)