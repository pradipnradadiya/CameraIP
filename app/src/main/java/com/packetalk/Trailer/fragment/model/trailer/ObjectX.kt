package com.packetalk.Trailer.fragment.model.trailer


import com.google.gson.annotations.SerializedName

data class ObjectX(
    @SerializedName("ALLRisk")
    val aLLRisk: String,
    @SerializedName("BATTERY_VOLTAGE")
    val bATTERYVOLTAGE: Double,
    @SerializedName("Community")
    val community: Any,
    @SerializedName("createddate")
    val createddate: String,
    @SerializedName("DNS")
    val dNS: Any,
    @SerializedName("DeviceID")
    val deviceID: Double,
    @SerializedName("ENGINE_RUN_TIME")
    val eNGINERUNTIME: Double,
    @SerializedName("ENGINE_SPEED")
    val eNGINESPEED: Double,
    @SerializedName("EngineStatus")
    val engineStatus: Any,
    @SerializedName("FUEL_LEVEL")
    val fUELLEVEL: Double,
    @SerializedName("IP")
    val iP: String,
    @SerializedName("IdelStatus")
    val idelStatus: String,
    @SerializedName("IsHybrid")
    val isHybrid: Any,
    @SerializedName("IsIdel")
    val isIdel: Boolean,
    @SerializedName("LastTimeEngineRun")
    val lastTimeEngineRun: Int,
    @SerializedName("OID")
    val oID: Any,
    @SerializedName("Object")
    val objectX: Any,
    @SerializedName("PointerValue")
    val pointerValue: Double,
    @SerializedName("RUNNING_STATE")
    val rUNNINGSTATE: Double,
    @SerializedName("Range_FUEL_LEVEL")
    val rangeFUELLEVEL: String,
    @SerializedName("RangeLast_Time_ENGINE_RUN")
    val rangeLastTimeENGINERUN: String,
    @SerializedName("Range_TOTAL_RUN_TIME")
    val rangeTOTALRUNTIME: String,
    @SerializedName("Range_VOLTAGE")
    val rangeVOLTAGE: String,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any,
    @SerializedName("Risk")
    val risk: Any,
    @SerializedName("Risk_ENGINE_RUN_Time")
    val riskENGINERUNTime: String,
    @SerializedName("Risk_ENGINE_SPEED")
    val riskENGINESPEED: String,
    @SerializedName("Risk_FUEL_LEVEL")
    val riskFUELLEVEL: String,
    @SerializedName("_RiskLastTimeEngineRun")
    val _riskLastTimeEngineRun: Any,
    @SerializedName("RiskLastTimeEngineRun")
    val riskLastTimeEngineRun: String,
    @SerializedName("Risk_RUNNING_STATE")
    val riskRUNNINGSTATE: String,
    @SerializedName("Risk_VOLTAGE")
    val riskVOLTAGE: String,
    @SerializedName("RouterID")
    val routerID: Double,
    @SerializedName("TOTAL_ENGINE_RUN_TIME")
    val tOTALENGINERUNTIME: Double,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("TrailerName")
    val trailerName: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("FULL_Range_FUEL_LEVEL")
    val fullRangeFuelLevel: String,
    @SerializedName("FULL_Range_VOLTAGE")
    val fullRangeVoltage: String,
    @SerializedName("FULL_RangeLast_Time_ENGINE_RUN")
    val fullRangeLastTimeEngineRun: String,
    @SerializedName("FULL_Range_TOTAL_RUN_TIME")
    val fullRangeTotalRunTime: String,

    @SerializedName("FULL_Range_SOLAR_VOLTAGE")
    val fullRangeSolarVoltage: String,
    @SerializedName("FULL_Range_Battry_VOLTAGE")
    val fullRangeBateryVoltage: String,
    @SerializedName("FULL_Range_TEMPERATURE")
    val fullRangeTemperature: String,

    @SerializedName("Risk_SolarVoltage")
    val Risk_SolarVoltage: String,
    @SerializedName("Risk_BatteryValtage")
    val Risk_BatteryValtage: String,
    @SerializedName("Risk_LoadStatus")
    val Risk_LoadStatus: String,
    @SerializedName("Risk_Temperature")
    val Risk_Temperature: String

)