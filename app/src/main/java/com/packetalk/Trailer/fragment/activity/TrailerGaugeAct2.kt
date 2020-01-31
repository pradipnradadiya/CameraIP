package com.packetalk.Trailer.fragment.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.github.anastr.speedviewlib.components.indicators.Indicator
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerSubGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.TrailerSubGaugeItem
import com.packetalk.chart.ChartAct
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.util.invisible
import com.packetalk.util.setLoader
import com.packetalk.view_model.trailer.TrailerGaugeDataModel
import kotlinx.android.synthetic.main.act_trailer_gauge2.*
import kotlinx.android.synthetic.main.gauge_solar_battery_voltage.*
import kotlinx.android.synthetic.main.gauge_solar_current.*
import kotlinx.android.synthetic.main.gauge_solar_power.*
import kotlinx.android.synthetic.main.gauge_solar_voltage.*
import kotlinx.android.synthetic.main.gauge_temperature.*
import org.json.JSONArray

class TrailerGaugeAct2 : BaseActivity(), View.OnClickListener {
    var adapter: TrailerSubGaugeAdapter? = null
    var layoutManager: LinearLayoutManager? = null
    private var arrSubGaugeList = ArrayList<TrailerSubGaugeItem>()
    var vitalType: String? = null
    var trailerName: String? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_trailer_gauge2
    }

    override fun init() {
        vitalType = intent.getStringExtra("vitalType")
        trailerName = intent.getStringExtra("trailerName")
        layoutManager = LinearLayoutManager(this@TrailerGaugeAct2)
    }

    override fun initView() {
        bindToolBarBack(trailerName.toString())
        recycleviewTrailerGauge.layoutManager = layoutManager
    }

    override fun postInitView() {
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(recycleviewTrailerGauge)
//        adapter = TrailerSubGaugeAdapter(this@TrailerGaugeAct,arrSubGaugeList)
//        recycleviewTrailerGauge.adapter = adapter
    }

    override fun addListener() {
        fabMap.setOnClickListener {

        }
        linSubBorder1.setOnClickListener(this)
        linSubBorder2.setOnClickListener(this)
        linSubBorder3.setOnClickListener(this)
        linSubBorder5.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun loadData() {
        loader.controller = setLoader()
        val data =
            ViewModelProviders.of(this@TrailerGaugeAct2).get(TrailerGaugeDataModel::class.java)
        data.getTrailer(vitalType.toString(), trailerName.toString()).observe(this,
            Observer<TrailerSubGaugeItem> { gaugeData ->
                AppLogger.e("data is----------" + gaugeData.objectX.toString())
                tvTotalRunTime.text = "${gaugeData.objectX.temperature} F"
                loader.invisible()
                linContainer.visibility = View.VISIBLE
                //SOLAR VOLTAGE
                if (vitalType == "solar") {
                    when (gaugeData.objectX.riskSolarVoltage) {
                        "Critical" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border_red)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border_red)
                        }
                        "Warning" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border_yellow)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border_yellow)
                        }
                        "Normal" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border)
                        }
                    }
                }


                //--------------------------------------SOLAR VOLTAGE----------------------------------------------
                tvFualLevel.text = "${gaugeData.objectX.solarVoltage} V"
                if (gaugeData.objectX.solarVoltageRange.isFromDB) {
                    val startVal = gaugeData.objectX.solarVoltageRange.ranges.normal[0]
                    val endVal = gaugeData.objectX.solarVoltageRange.ranges.critical[1]

                    val rangeCount = endVal - startVal
                    val range1 =
                        gaugeData.objectX.solarVoltageRange.ranges.normal[1] - gaugeData.objectX.solarVoltageRange.ranges.normal[0]
                    val range2 =
                        gaugeData.objectX.solarVoltageRange.ranges.warning[1] - gaugeData.objectX.solarVoltageRange.ranges.warning[0]
                    val range3 =
                        gaugeData.objectX.solarVoltageRange.ranges.critical[1] - gaugeData.objectX.solarVoltageRange.ranges.critical[0]
                    val per1 = ((range1 * 100) / rangeCount)
                    val per2 = ((range2 * 100) / rangeCount) + per1
                    val per3 = ((range3 * 100) / rangeCount) + per2

                    AppLogger.e("per1----$per1")
                    AppLogger.e("per2----$per2")
                    AppLogger.e("per3----$per3")

                    speedViewSolarVoltage.setMinSpeed(startVal)
                    speedViewSolarVoltage.maxGaugeValue = endVal
                    speedViewSolarVoltage.tickNumber = 10
                    speedViewSolarVoltage.speedTo(gaugeData.objectX.solarVoltage.toFloat())
                    speedViewSolarVoltage.cancelSpeedAnimator()

                    speedViewSolarVoltage.setLowSpeedPercent(0f)
                    speedViewSolarVoltage.setMediumSpeedPercent(per1)
                    speedViewSolarVoltage.setAverageSpeedPercent(per2)
                    speedViewSolarVoltage.setAverageHighSpeedPercent(per3)


                    speedViewSolarVoltage.setLowSpeedColor(resources.getColor(R.color.green))
                    speedViewSolarVoltage.setMediumSpeedColor(resources.getColor(R.color.green))
                    speedViewSolarVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                    speedViewSolarVoltage.setAverageHighSpeedColor(resources.getColor(R.color.red))
                    speedViewSolarVoltage.setHighSpeedColor(resources.getColor(R.color.red))


                } else {

                    if (gaugeData.objectX.solarType == "SOLAR") {
                        var fuelStartEndValue =
                            gaugeData.objectX.fULLRangeSOLARVOLTAGE.replace("[", "")
                        fuelStartEndValue = fuelStartEndValue.replace("]", "")
                        val fr = fuelStartEndValue.split("-")

                        speedViewSolarVoltage.setMinSpeed(fr[0].toFloat())
                        speedViewSolarVoltage.maxGaugeValue = fr[1].toFloat()
                        speedViewSolarVoltage.tickNumber = 10
                        speedViewSolarVoltage.speedTo(gaugeData.objectX.solarVoltage.toFloat())
                        speedViewSolarVoltage.cancelSpeedAnimator()

                        val jsonArrayFuel = JSONArray(gaugeData.objectX.rangeSOLARVOLTAGE)
                        val fuelRangeArray = jsonArrayFuel.join(",").split(",")
                        speedViewSolarVoltage.setLowSpeedPercent(0f)
                        val rangeCount = fr[1].toInt() - fr[0].toInt()
                        speedViewSolarVoltage.setMediumSpeedPercent(fuelRangeArray[0].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setAverageSpeedPercent(fuelRangeArray[1].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setAverageHighSpeedPercent(fuelRangeArray[1].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setLowSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setMediumSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                        speedViewSolarVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setHighSpeedColor(resources.getColor(R.color.red))
                    } else {
                        var fuelStartEndValue =
                            gaugeData.objectX.fullSolarVoltage12.replace("[", "")
                        fuelStartEndValue = fuelStartEndValue.replace("]", "")
                        val fr = fuelStartEndValue.split("-")
                        val rangeCount = fr[1].toInt() - fr[0].toInt()
                        speedViewSolarVoltage.setMinSpeed(fr[0].toFloat())
                        speedViewSolarVoltage.maxGaugeValue = fr[1].toFloat()
                        speedViewSolarVoltage.tickNumber = 10
                        speedViewSolarVoltage.speedTo(gaugeData.objectX.solarVoltage.toFloat())
                        speedViewSolarVoltage.cancelSpeedAnimator()

                        val jsonArrayFuel = JSONArray(gaugeData.objectX.rangeSOLARVOLTAGE)
                        val fuelRangeArray = jsonArrayFuel.join(",").split(",")
                        speedViewSolarVoltage.setLowSpeedPercent(0f)
                        speedViewSolarVoltage.setMediumSpeedPercent(fuelRangeArray[0].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setAverageSpeedPercent(fuelRangeArray[1].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setAverageHighSpeedPercent(fuelRangeArray[1].toFloat() * 100 / rangeCount)
                        speedViewSolarVoltage.setLowSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setMediumSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                        speedViewSolarVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarVoltage.setHighSpeedColor(resources.getColor(R.color.red))
                    }
                }

                //--------------------------------------SOLAR CURRENT----------------------------------------------
                if (gaugeData.objectX.SolarCurrentRange.isFromDB) {
                    val startVal = gaugeData.objectX.SolarCurrentRange.ranges.normal[0]
                    val endVal = gaugeData.objectX.SolarCurrentRange.ranges.normal[1]

                    tvSolarCurrent.text = "${gaugeData.objectX.solarCurrent} A"
                    speedViewSolarCurrent.setMinSpeed(startVal)
                    speedViewSolarCurrent.maxGaugeValue = endVal
                    speedViewSolarCurrent.tickNumber = 10
                    speedViewSolarCurrent.speedTo(gaugeData.objectX.solarPower)
                    speedViewSolarCurrent.cancelSpeedAnimator()
                    speedViewSolarCurrent.setLowSpeedPercent(0f)
                    speedViewSolarCurrent.setMediumSpeedPercent(0f)
                    speedViewSolarCurrent.setAverageSpeedPercent(0f)
                    speedViewSolarCurrent.setAverageHighSpeedPercent(0f)
                    speedViewSolarCurrent.setHighSpeedColor(resources.getColor(R.color.green))


                } else {

                    if (gaugeData.objectX.solarType == "SOLAR") {
                        var solarCurrentStartEndValue =
                            gaugeData.objectX.fULLRangeCurrent.replace("[", "")
                        solarCurrentStartEndValue = solarCurrentStartEndValue.replace("]", "")
                        val sc = solarCurrentStartEndValue.split("-")
                        tvSolarCurrent.text = "${gaugeData.objectX.solarCurrent} A"
                        speedViewSolarCurrent.setMinSpeed(sc[0].toFloat())
                        speedViewSolarCurrent.maxGaugeValue = sc[1].toFloat()
                        speedViewSolarCurrent.tickNumber = 10
                        speedViewSolarCurrent.speedTo(gaugeData.objectX.solarPower)
                        speedViewSolarCurrent.cancelSpeedAnimator()
                        speedViewSolarCurrent.setLowSpeedPercent(0f)
                        speedViewSolarCurrent.setMediumSpeedPercent(0f)
                        speedViewSolarCurrent.setAverageSpeedPercent(0f)
                        speedViewSolarCurrent.setAverageHighSpeedPercent(0f)
                        speedViewSolarCurrent.setHighSpeedColor(resources.getColor(R.color.green))
                    } else {
                        var solarCurrentStartEndValue =
                            gaugeData.objectX.fullRangeCurrent12.replace("[", "")
                        solarCurrentStartEndValue = solarCurrentStartEndValue.replace("]", "")
                        val sc = solarCurrentStartEndValue.split("-")
                        tvSolarCurrent.text = "${gaugeData.objectX.solarCurrent} A"
                        speedViewSolarCurrent.setMinSpeed(sc[0].toFloat())
                        speedViewSolarCurrent.maxGaugeValue = sc[1].toFloat()
                        speedViewSolarCurrent.tickNumber = 10
                        speedViewSolarCurrent.speedTo(gaugeData.objectX.solarPower)
                        speedViewSolarCurrent.cancelSpeedAnimator()
                        speedViewSolarCurrent.setLowSpeedPercent(0f)
                        speedViewSolarCurrent.setMediumSpeedPercent(0f)
                        speedViewSolarCurrent.setAverageSpeedPercent(0f)
                        speedViewSolarCurrent.setAverageHighSpeedPercent(0f)
                        speedViewSolarCurrent.setHighSpeedColor(resources.getColor(R.color.green))
                    }
                }


                //--------------------------------------SOLAR POWER----------------------------------------------
                if (gaugeData.objectX.solarPowerRange.isFromDB) {
                    val startVal = gaugeData.objectX.solarPowerRange.ranges.normal[0]
                    val endVal = gaugeData.objectX.solarPowerRange.ranges.normal[1]
                    tvSolarPower.text = "${gaugeData.objectX.solarPower} W/Hr"
                    speedViewSolarPower.setMinSpeed(startVal)
                    speedViewSolarPower.maxGaugeValue = endVal
                    speedViewSolarPower.tickNumber = 10
                    speedViewSolarPower.speedTo(gaugeData.objectX.solarPower)
                    speedViewSolarPower.cancelSpeedAnimator()
                    speedViewSolarPower.setLowSpeedPercent(0f)
                    speedViewSolarPower.setMediumSpeedPercent(0f)
                    speedViewSolarPower.setAverageSpeedPercent(0f)
                    speedViewSolarPower.setAverageHighSpeedPercent(0f)
                    speedViewSolarPower.setHighSpeedColor(resources.getColor(R.color.green))
                } else {


                    if (gaugeData.objectX.solarType == "SOLAR") {
                        var solarPowerStartEndValue =
                            gaugeData.objectX.fULLRangePower.replace("[", "")
                        solarPowerStartEndValue = solarPowerStartEndValue.replace("]", "")
                        val sp = solarPowerStartEndValue.split("-")
                        tvSolarPower.text = "${gaugeData.objectX.solarPower} W/Hr"
                        speedViewSolarPower.setMinSpeed(sp[0].toFloat())
                        speedViewSolarPower.maxGaugeValue = sp[1].toFloat()
                        speedViewSolarPower.tickNumber = 10
                        speedViewSolarPower.speedTo(gaugeData.objectX.solarPower)
                        speedViewSolarPower.cancelSpeedAnimator()
                        speedViewSolarPower.setLowSpeedPercent(0f)
                        speedViewSolarPower.setMediumSpeedPercent(0f)
                        speedViewSolarPower.setAverageSpeedPercent(0f)
                        speedViewSolarPower.setAverageHighSpeedPercent(0f)
                        speedViewSolarPower.setHighSpeedColor(resources.getColor(R.color.green))
                    } else {
                        var solarPowerStartEndValue =
                            gaugeData.objectX.fullRangePower12.replace("[", "")
                        solarPowerStartEndValue = solarPowerStartEndValue.replace("]", "")
                        val sp = solarPowerStartEndValue.split("-")
                        tvSolarPower.text = "${gaugeData.objectX.solarPower} W/Hr"
                        speedViewSolarPower.setMinSpeed(sp[0].toFloat())
                        speedViewSolarPower.maxGaugeValue = sp[1].toFloat()
                        speedViewSolarPower.tickNumber = 10
                        speedViewSolarPower.speedTo(gaugeData.objectX.solarPower)
                        speedViewSolarPower.cancelSpeedAnimator()
                        speedViewSolarPower.setLowSpeedPercent(0f)
                        speedViewSolarPower.setMediumSpeedPercent(0f)
                        speedViewSolarPower.setAverageSpeedPercent(0f)
                        speedViewSolarPower.setAverageHighSpeedPercent(0f)
                        speedViewSolarPower.setHighSpeedColor(resources.getColor(R.color.green))
                    }
                }


                //--------------------------------------BATTERY VOLTAGE----------------------------------------------
                when (gaugeData.objectX.riskBatteryValtage) {
                    "Critical" -> {
                        linMainBorder2.setBackgroundResource(R.drawable.trailer_border_red)
                        linSubBorder2.setBackgroundResource(R.drawable.trailer_border_red)
                    }
                    "Warning" -> {
                        linMainBorder2.setBackgroundResource(R.drawable.trailer_border_yellow)
                        linSubBorder2.setBackgroundResource(R.drawable.trailer_border_yellow)
                    }
                    "Normal" -> {
                        linMainBorder2.setBackgroundResource(R.drawable.trailer_border)
                        linSubBorder2.setBackgroundResource(R.drawable.trailer_border)
                    }
                }
                if (gaugeData.objectX.batteryVoltageRange.isFromDB) {
                    tvBatteryVoltage.text = "${gaugeData.objectX.batteryVoltage} V"
                    val startVal = gaugeData.objectX.batteryVoltageRange.ranges.critical[0]
                    val endVal = gaugeData.objectX.batteryVoltageRange.ranges.normal[1]
                    val rangeCount = endVal - startVal
                    val range1 =
                        gaugeData.objectX.batteryVoltageRange.ranges.critical[1] - gaugeData.objectX.batteryVoltageRange.ranges.critical[0]
                    val range2 =
                        gaugeData.objectX.batteryVoltageRange.ranges.warning[1] - gaugeData.objectX.batteryVoltageRange.ranges.warning[0]
                    val range3 =
                        gaugeData.objectX.batteryVoltageRange.ranges.normal[1] - gaugeData.objectX.batteryVoltageRange.ranges.normal[0]
                    val per1 = (range1 * 100) / rangeCount
                    val per2 = ((range2 * 100) / rangeCount) + per1
                    val per3 = ((range3 * 100) / rangeCount) + per2
                    speedViewSolarBatteryVoltage.setMinSpeed(startVal)
                    speedViewSolarBatteryVoltage.maxGaugeValue = endVal
                    speedViewSolarBatteryVoltage.tickNumber = 10
                    speedViewSolarBatteryVoltage.speedTo(gaugeData.objectX.batteryVoltage.toFloat())
                    speedViewSolarBatteryVoltage.cancelSpeedAnimator()
                    speedViewSolarBatteryVoltage.setLowSpeedPercent(0F)
                    speedViewSolarBatteryVoltage.setMediumSpeedPercent(per1)
                    speedViewSolarBatteryVoltage.setAverageSpeedPercent(per2)
                    speedViewSolarBatteryVoltage.setAverageHighSpeedPercent(per3)
                    speedViewSolarBatteryVoltage.setLowSpeedColor(resources.getColor(R.color.red))
                    speedViewSolarBatteryVoltage.setMediumSpeedColor(resources.getColor(R.color.red))
                    speedViewSolarBatteryVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                    speedViewSolarBatteryVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                    speedViewSolarBatteryVoltage.setHighSpeedColor(resources.getColor(R.color.green))


                } else {

                    if (gaugeData.objectX.solarType == "SOLAR") {
                        tvBatteryVoltage.text = "${gaugeData.objectX.batteryVoltage} V"
                        var voltageStartEndValue =
                            gaugeData.objectX.fULLRangeBattryVOLTAGE.replace("[", "")
                        voltageStartEndValue = voltageStartEndValue.replace("]", "")
                        val vr = voltageStartEndValue.split("-")
                        speedViewSolarBatteryVoltage.setMinSpeed(vr[0].toFloat())
                        speedViewSolarBatteryVoltage.maxGaugeValue = vr[1].toFloat()
                        speedViewSolarBatteryVoltage.tickNumber = 10
                        speedViewSolarBatteryVoltage.speedTo(gaugeData.objectX.batteryVoltage.toFloat())
                        speedViewSolarBatteryVoltage.cancelSpeedAnimator()

                        val jsonArrayVoltage = JSONArray(gaugeData.objectX.rangeBattryVOLTAGE)
                        val voltageRangeArray = jsonArrayVoltage.join(",").split(",")

                        AppLogger.e("battery voltage")
                        val rangeCount = vr[1].toInt() - vr[0].toInt()
                        val range1 = voltageRangeArray[0].toFloat() - vr[0].toInt()
                        val range2 = voltageRangeArray[1].toFloat() - vr[0].toInt()
                        val per1 = (range1 * 100) / rangeCount
                        val per2 = ((range2 * 100) / rangeCount)

                        speedViewSolarBatteryVoltage.setLowSpeedPercent(0F)
                        speedViewSolarBatteryVoltage.setMediumSpeedPercent(per1)
                        speedViewSolarBatteryVoltage.setAverageSpeedPercent(per2)
                        speedViewSolarBatteryVoltage.setAverageHighSpeedPercent(per2)
                        speedViewSolarBatteryVoltage.setLowSpeedColor(resources.getColor(R.color.red))
                        speedViewSolarBatteryVoltage.setMediumSpeedColor(resources.getColor(R.color.red))
                        speedViewSolarBatteryVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                        speedViewSolarBatteryVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarBatteryVoltage.setHighSpeedColor(resources.getColor(R.color.green))
                    } else {
                        tvBatteryVoltage.text = "${gaugeData.objectX.batteryVoltage} V"
                        var voltageStartEndValue =
                            gaugeData.objectX.fullBatteryVoltage12.replace("[", "")
                        voltageStartEndValue = voltageStartEndValue.replace("]", "")
                        val vr = voltageStartEndValue.split("-")
                        speedViewSolarBatteryVoltage.setMinSpeed(vr[0].toFloat())
                        speedViewSolarBatteryVoltage.maxGaugeValue = vr[1].toFloat()
                        speedViewSolarBatteryVoltage.tickNumber = 10
                        speedViewSolarBatteryVoltage.speedTo(gaugeData.objectX.batteryVoltage.toFloat())
                        speedViewSolarBatteryVoltage.cancelSpeedAnimator()

                        val jsonArrayVoltage = JSONArray(gaugeData.objectX.rangeBattryVOLTAGE)
                        val voltageRangeArray = jsonArrayVoltage.join(",").split(",")
                        var count = vr[1].toInt() - vr[0].toInt()
                        AppLogger.e("battery voltage")
                        val rangeCount = vr[1].toInt() - vr[0].toInt()
                        val range1 = voltageRangeArray[0].toFloat() - vr[0].toInt()
                        val range2 = voltageRangeArray[1].toFloat() - vr[0].toInt()
                        val per1 = (range1 * 100) / rangeCount
                        val per2 = ((range2 * 100) / rangeCount)
                        speedViewSolarBatteryVoltage.setLowSpeedPercent(0F)
                        speedViewSolarBatteryVoltage.setMediumSpeedPercent(per1)
                        speedViewSolarBatteryVoltage.setAverageSpeedPercent(per2)
                        speedViewSolarBatteryVoltage.setAverageHighSpeedPercent(per2)
                        speedViewSolarBatteryVoltage.setLowSpeedColor(resources.getColor(R.color.red))
                        speedViewSolarBatteryVoltage.setMediumSpeedColor(resources.getColor(R.color.red))
                        speedViewSolarBatteryVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                        speedViewSolarBatteryVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                        speedViewSolarBatteryVoltage.setHighSpeedColor(resources.getColor(R.color.green))
                    }
                }


                //--------------------------------------TEMPERATURE--------------------------------------
                if (gaugeData.objectX.solarTemperatureRange.isFromDB) {
                    val startVal = gaugeData.objectX.solarTemperatureRange.ranges.critical[0]
                    val endVal = gaugeData.objectX.solarTemperatureRange.ranges.higherCritical[1]
                    val rangeCount = endVal - startVal
                    AppLogger.e("rangecount--------$rangeCount")
                    val range1 =
                        gaugeData.objectX.solarTemperatureRange.ranges.critical[1] - gaugeData.objectX.solarTemperatureRange.ranges.critical[0]
                    AppLogger.e("range1--------$range1")
                    val range2 =
                        gaugeData.objectX.solarTemperatureRange.ranges.warning[1] - gaugeData.objectX.solarTemperatureRange.ranges.warning[0]
                    AppLogger.e("range2--------$range2")
                    val range3 =
                        gaugeData.objectX.solarTemperatureRange.ranges.normal[1] - gaugeData.objectX.solarTemperatureRange.ranges.normal[0]
                    AppLogger.e("range3--------$range3")
                    val range4 =
                        gaugeData.objectX.solarTemperatureRange.ranges.higherWarning[1] - gaugeData.objectX.solarTemperatureRange.ranges.higherWarning[0]
                    AppLogger.e("range4--------$range4")
                    val range5 =
                        gaugeData.objectX.solarTemperatureRange.ranges.higherCritical[1] - gaugeData.objectX.solarTemperatureRange.ranges.higherCritical[0]
                    AppLogger.e("range5--------$range5")
                    val per1 = (range1 * 100) / rangeCount
                    val per2 = ((range2 * 100) / rangeCount) + per1
                    val per3 = ((range3 * 100) / rangeCount) + per2
                    val per4 = ((range4 * 100) / rangeCount) + per3
                    AppLogger.e("per1--------$per1")
                    AppLogger.e("per2--------$per2")
                    AppLogger.e("per3--------$per3")
                    AppLogger.e("per4--------$per4")
                    speedViewTemperature.setMinSpeed(startVal)
                    speedViewTemperature.maxGaugeValue = endVal
                    speedViewTemperature.tickNumber = 10
                    speedViewTemperature.speedTo(gaugeData.objectX.temperature.toFloat())
                    speedViewTemperature.cancelSpeedAnimator()
                    speedViewTemperature.setLowSpeedPercent(per1)
                    speedViewTemperature.setMediumSpeedPercent(per2)
                    speedViewTemperature.setAverageSpeedPercent(per3)
                    speedViewTemperature.setAverageHighSpeedPercent(per4)
                    speedViewTemperature.setLowSpeedColor(resources.getColor(R.color.red))
                    speedViewTemperature.setMediumSpeedColor(resources.getColor(R.color.yellow))
                    speedViewTemperature.setAverageSpeedColor(resources.getColor(R.color.green))
                    speedViewTemperature.setAverageHighSpeedColor(resources.getColor(R.color.yellow))
                    speedViewTemperature.setHighSpeedColor(resources.getColor(R.color.red))

                } else {
                    if (gaugeData.objectX.solarType == "SOLAR") {
                        var fullRangeTotalRunTime =
                            gaugeData.objectX.fULLRangeTEMPERATURE.replace("[", "")
                        fullRangeTotalRunTime = fullRangeTotalRunTime.replace("]", "")
                        val rr = fullRangeTotalRunTime.split("|")
                        AppLogger.e("rr----------$rr")
                        val rangeCount =  rr[1].toInt() - rr[0].toInt()

                        speedViewTemperature.setMinSpeed(rr[0].toFloat())
                        speedViewTemperature.maxGaugeValue = rr[1].toFloat()
                        speedViewTemperature.tickNumber = 10
                        speedViewTemperature.speedTo(gaugeData.objectX.temperature.toFloat())
                        speedViewTemperature.cancelSpeedAnimator()

                        val jsonArrayTemp = JSONArray(gaugeData.objectX.rangeTEMPERATURE)
                        val tempRangeArray = jsonArrayTemp.join(",").split(",")
                        val range1 = tempRangeArray[0].toFloat() - rr[0].toInt()
                        val range2 = tempRangeArray[1].toFloat() - rr[0].toInt()
                        val range3 = tempRangeArray[2].toFloat() - rr[0].toInt()
                        val range4 = tempRangeArray[3].toFloat() - rr[0].toInt()
                        val per1 = (range1 * 100) / rangeCount
                        val per2 = ((range2 * 100) / rangeCount)
                        val per3 = ((range3 * 100) / rangeCount)
                        val per4 = ((range4 * 100) / rangeCount)

                        AppLogger.e("per1-----$per1")
                        AppLogger.e("per1-----$per2")
                        AppLogger.e("per1-----$per3")
                        AppLogger.e("per1-----$per4")

                        speedViewTemperature.setLowSpeedPercent(per1)
                        speedViewTemperature.setMediumSpeedPercent(per2)
                        speedViewTemperature.setAverageSpeedPercent(per3)
                        speedViewTemperature.setAverageHighSpeedPercent(per4)
                        speedViewTemperature.setLowSpeedColor(resources.getColor(R.color.red))
                        speedViewTemperature.setMediumSpeedColor(resources.getColor(R.color.yellow))
                        speedViewTemperature.setAverageSpeedColor(resources.getColor(R.color.green))
                        speedViewTemperature.setAverageHighSpeedColor(resources.getColor(R.color.yellow))
                        speedViewTemperature.setHighSpeedColor(resources.getColor(R.color.red))

                    } else {
                        var fullRangeTotalRunTime =
                            gaugeData.objectX.fullTemperature12.replace("[", "")
                        fullRangeTotalRunTime = fullRangeTotalRunTime.replace("]", "")
                        val rr = fullRangeTotalRunTime.split("|")
                        AppLogger.e("rr----------$rr")
                        val rangeCount =  rr[1].toInt() - rr[0].toInt()
                        speedViewTemperature.setMinSpeed(rr[0].toFloat())
                        speedViewTemperature.maxGaugeValue = rr[1].toFloat()
                        speedViewTemperature.tickNumber = 10
                        speedViewTemperature.setIndicator(Indicator.Indicators.HalfLineIndicator)
                        speedViewTemperature.indicatorWidth = 4.0F
                        speedViewTemperature.indicatorColor = resources.getColor(R.color.black)
                        speedViewTemperature.speedTo(gaugeData.objectX.temperature.toFloat())
                        speedViewTemperature.cancelSpeedAnimator()

                        val jsonArrayTemp = JSONArray(gaugeData.objectX.rangeTEMPERATURE)
                        val tempRangeArray = jsonArrayTemp.join(",").split(",")
                        val range1 = tempRangeArray[0].toFloat() - rr[0].toInt()
                        val range2 = tempRangeArray[1].toFloat() - rr[0].toInt()
                        val range3 = tempRangeArray[2].toFloat() - rr[0].toInt()
                        val range4 = tempRangeArray[3].toFloat() - rr[0].toInt()
                        val per1 = (range1 * 100) / rangeCount
                        val per2 = ((range2 * 100) / rangeCount)
                        val per3 = ((range3 * 100) / rangeCount)
                        val per4 = ((range4 * 100) / rangeCount)

                        speedViewTemperature.setLowSpeedPercent(per1)
                        speedViewTemperature.setMediumSpeedPercent(per2)
                        speedViewTemperature.setAverageSpeedPercent(per3)
                        speedViewTemperature.setAverageHighSpeedPercent(per4)
                        speedViewTemperature.setLowSpeedColor(resources.getColor(R.color.red))
                        speedViewTemperature.setMediumSpeedColor(resources.getColor(R.color.yellow))
                        speedViewTemperature.setAverageSpeedColor(resources.getColor(R.color.green))
                        speedViewTemperature.setAverageHighSpeedColor(resources.getColor(R.color.yellow))
                        speedViewTemperature.setHighSpeedColor(resources.getColor(R.color.red))
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        val intent = Intent(this@TrailerGaugeAct2, ChartAct::class.java)
        when (v?.id) {
            R.id.linSubBorder1 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "SOLAR_VOLTAGE")
                startActivity(intent)
            }
            R.id.linSubBorder2 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "BATTERY_VOLTAGE")
                startActivity(intent)
            }
            R.id.linSubBorder3 -> {
//                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
//                intent.putExtra(AppConstants.GAUGE_TYPE, "SOLAR_VOLTAGE")
//                startActivity(intent)
            }
            R.id.linSubBorder5 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "TEMERATURE")
                startActivity(intent)
            }
        }
    }
}
