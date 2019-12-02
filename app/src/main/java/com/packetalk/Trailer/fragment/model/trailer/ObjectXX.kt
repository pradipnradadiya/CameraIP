package com.packetalk.Trailer.fragment.model.trailer


import com.google.gson.annotations.SerializedName

data class ObjectXX(
    @SerializedName("ALLRisk")
    val aLLRisk: String,
    @SerializedName("BATTERY_VOLTAGE")
    val bATTERYVOLTAGE: Double,
    @SerializedName("BatteryVoltage")
    val batteryVoltage: Double,
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
    val engineStatus: String,
    @SerializedName("FUEL_LEVEL")
    val fUELLEVEL: Double,
    @SerializedName("FULL_Range_Battry_VOLTAGE")
    val fULLRangeBattryVOLTAGE: String,
    @SerializedName("FULL_Range_FUEL_LEVEL")
    val fULLRangeFUELLEVEL: String,
    @SerializedName("FULL_RangeLast_Time_ENGINE_RUN")
    val fULLRangeLastTimeENGINERUN: String,
    @SerializedName("FULL_Range_SOLAR_VOLTAGE")
    val fULLRangeSOLARVOLTAGE: String,
    @SerializedName("FULL_Range_TEMPERATURE")
    val fULLRangeTEMPERATURE: String,
    @SerializedName("FULL_Range_TOTAL_RUN_TIME")
    val fULLRangeTOTALRUNTIME: String,
    @SerializedName("FULL_Range_VOLTAGE")
    val fULLRangeVOLTAGE: String,
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
    @SerializedName("LoadStatus")
    val loadStatus: Double,
    @SerializedName("OID")
    val oID: Any,
    @SerializedName("Object")
    val objectX: Any,
    @SerializedName("PointerValue")
    val pointerValue: Double,
    @SerializedName("RUNNING_STATE")
    val rUNNINGSTATE: Double,
    @SerializedName("Range_Battry_VOLTAGE")
    val rangeBattryVOLTAGE: String,
    @SerializedName("Range_FUEL_LEVEL")
    val rangeFUELLEVEL: String,
    @SerializedName("RangeLast_Time_ENGINE_RUN")
    val rangeLastTimeENGINERUN: String,
    @SerializedName("Range_SOLAR_VOLTAGE")
    val rangeSOLARVOLTAGE: String,
    @SerializedName("Range_TEMPERATURE")
    val rangeTEMPERATURE: String,
    @SerializedName("Range_VOLTAGE")
    val rangeVOLTAGE: String,
    @SerializedName("ResponseResult")
    val responseResult: Boolean,
    @SerializedName("ResponseStringResult")
    val responseStringResult: Any,
    @SerializedName("Risk")
    val risk: Any,
    @SerializedName("Risk_BatteryValtage")
    val riskBatteryValtage: String,
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
    @SerializedName("Risk_LoadStatus")
    val riskLoadStatus: String,
    @SerializedName("Risk_RUNNING_STATE")
    val riskRUNNINGSTATE: String,
    @SerializedName("Risk_SolarVoltage")
    val riskSolarVoltage: String,
    @SerializedName("Risk_Temperature")
    val riskTemperature: String,
    @SerializedName("Risk_VOLTAGE")
    val riskVOLTAGE: String,
    @SerializedName("RouterID")
    val routerID: Double,
    @SerializedName("SolarType")
    val solarType: String,
    @SerializedName("SolarVoltage")
    val solarVoltage: Double,
    @SerializedName("TOTAL_ENGINE_RUN_TIME")
    val tOTALENGINERUNTIME: Double,
    @SerializedName("Temperature")
    val temperature: Double,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("TrailerName")
    val trailerName: String,
    @SerializedName("Type")
    val type: String
)