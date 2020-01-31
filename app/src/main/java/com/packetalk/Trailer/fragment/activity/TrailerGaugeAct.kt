package com.packetalk.Trailer.fragment.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerSubGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.TrailerSubGaugeItem
import com.packetalk.chart.ChartAct
import com.packetalk.util.*
import com.packetalk.view_model.trailer.TrailerGaugeDataModel
import kotlinx.android.synthetic.main.act_trailer_gauge.*
import kotlinx.android.synthetic.main.gauge_engine_running.*
import kotlinx.android.synthetic.main.gauge_fuel_level.*
import kotlinx.android.synthetic.main.gauge_last_time_engine_run.*
import kotlinx.android.synthetic.main.gauge_total_run_time.*
import kotlinx.android.synthetic.main.gauge_voltage.*
import org.json.JSONArray
import kotlin.math.roundToInt


class TrailerGaugeAct : BaseActivity(), View.OnClickListener {
    private var adapter: TrailerSubGaugeAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var arrSubGaugeList = ArrayList<TrailerSubGaugeItem>()
    private var vitalType: String? = null
    private var trailerName: String? = null
    private var defaultMinValue = 0.0F
    private var defaultMaxValue = 100.0F

    override fun getLayoutResourceId(): Int {
        return R.layout.act_trailer_gauge
    }

    override fun init() {
        vitalType = intent.getStringExtra("vitalType")
        trailerName = intent.getStringExtra("trailerName")
        layoutManager = LinearLayoutManager(this@TrailerGaugeAct)
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
        linSubBorder4.setOnClickListener(this)
        linSubBorder5.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun loadData() {

//        showProgressDialog("Trailer Gauge", "Please wait..")
        loader.controller = setLoader()
        val data =
            ViewModelProviders.of(this@TrailerGaugeAct).get(TrailerGaugeDataModel::class.java)
        data.getTrailer(vitalType.toString(), trailerName.toString()).observe(this,
            Observer<TrailerSubGaugeItem> { gaugeData ->
                AppLogger.e("data is----------" + gaugeData.objectX.toString())

                //Gauge text value
                tvFualLevel.text =
                    "${CommonUtils.upperCaseFirstLetter(gaugeData.objectX.fUELLEVEL.toString())} %"
                tvVoltage.text =
                    "${CommonUtils.upperCaseFirstLetter(gaugeData.objectX.bATTERYVOLTAGE.toString())} V"
                if (gaugeData.objectX.rUNNINGSTATE == 0.toDouble()) {
//                    tvEnginRunning.text = "${gaugeData.objectX.idelStatus} "
                    tvEnginRunning.text = "Idle"
                } else {
                    tvEnginRunning.text = "Off"
                }
                tvLastTimeEnginRun.text = "${gaugeData.objectX.lastTimeEngineRun} Min"
                tvTotalRunTime.text = "${gaugeData.objectX.tOTALENGINERUNTIME} \nHrs"


                //Fuel Gauge***********************************************
                if (vitalType == "diesel") {
                    when (gaugeData.objectX.riskFUELLEVEL) {
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

                if (gaugeData.objectX.fuelLevelRange.isFromDB) {
                    AppLogger.e("if fuel")
                    val startValue = gaugeData.objectX.fuelLevelRange.ranges.critical[0]
                    val endValue = gaugeData.objectX.fuelLevelRange.ranges.normal[1]
                    val totalPer = endValue - startValue
                    speedViewFuelLevel.setMinSpeed(startValue)
                    speedViewFuelLevel.maxGaugeValue = endValue
                    speedViewFuelLevel.tickNumber = 5
                    speedViewFuelLevel.speedTo(gaugeData.objectX.fUELLEVEL.toFloat())
                    speedViewFuelLevel.cancelSpeedAnimator()

                    val criticalRange =
                        gaugeData.objectX.fuelLevelRange.ranges.critical[1] - gaugeData.objectX.fuelLevelRange.ranges.critical[0]
                    val warningRange =
                        gaugeData.objectX.fuelLevelRange.ranges.warning[1] - gaugeData.objectX.fuelLevelRange.ranges.warning[0]
                    val normalRange =
                        gaugeData.objectX.fuelLevelRange.ranges.normal[1] - gaugeData.objectX.fuelLevelRange.ranges.normal[0]

                    AppLogger.e("percent----${(criticalRange * 100) / totalPer}")
                    AppLogger.e("percent----${(warningRange * 100) / totalPer}")
                    AppLogger.e("percent----${(normalRange * 100) / totalPer}")


                    val per1 = (criticalRange * 100) / totalPer
                    val per2 = ((warningRange * 100) / totalPer) + per1
                    val per3 = ((normalRange * 100) / totalPer) + per2

                    speedViewFuelLevel.setLowSpeedPercent(0F)
                    speedViewFuelLevel.setMediumSpeedPercent(per1)
                    speedViewFuelLevel.setAverageSpeedPercent(per2)
                    speedViewFuelLevel.setAverageHighSpeedPercent(per3)
                    speedViewFuelLevel.setLowSpeedColor(resources.getColor(R.color.red))
                    speedViewFuelLevel.setMediumSpeedColor(resources.getColor(R.color.red))
                    speedViewFuelLevel.setAverageSpeedColor(resources.getColor(R.color.yellow))
                    speedViewFuelLevel.setAverageHighSpeedColor(resources.getColor(R.color.green))
                    speedViewFuelLevel.setHighSpeedColor(resources.getColor(R.color.green))





                } else {
                    AppLogger.e("if fuel")
                    var fuelStartEndValue = gaugeData.objectX.fULLRangeFUELLEVEL.replace("[", "")
                    fuelStartEndValue = fuelStartEndValue.replace("]", "")
                    val fr = fuelStartEndValue.split("-")
                    speedViewFuelLevel.setMinSpeed(fr[0].toFloat())
                    speedViewFuelLevel.maxGaugeValue = fr[1].toFloat()
                    speedViewFuelLevel.tickNumber = 10
                    speedViewFuelLevel.speedTo(gaugeData.objectX.fUELLEVEL.toFloat())
                    speedViewFuelLevel.cancelSpeedAnimator()
                    val jsonArrayFuel = JSONArray(gaugeData.objectX.rangeFUELLEVEL)
                    val fuelRangeArray = jsonArrayFuel.join(",").split(",")
                    for ((i, value) in fuelRangeArray.withIndex()) {
                        when (i) {
                            0 -> {
                                val per: Float = (value.toFloat() * 100) / fr[1].toFloat()
                                speedViewFuelLevel.setLowSpeedPercent(per)
                                speedViewFuelLevel.setLowSpeedColor(Color.RED)
                                if (fuelRangeArray.size == 1) {
                                    speedViewFuelLevel.setMediumSpeedPercent(per)
                                    speedViewFuelLevel.setAverageSpeedPercent(per)
                                    speedViewFuelLevel.setAverageHighSpeedPercent(per)
                                }
                            }
                            1 -> {
                                val per: Float = (value.toFloat() * 100) / fr[1].toFloat()
                                speedViewFuelLevel.setMediumSpeedPercent(per)
                                if (fuelRangeArray.size == 2) {
                                    speedViewFuelLevel.setAverageSpeedPercent(per)
                                    speedViewFuelLevel.setAverageHighSpeedPercent(per)
                                    speedViewFuelLevel.setAverageSpeedColor(resources.getColor(R.color.green))
                                    speedViewFuelLevel.setAverageHighSpeedColor(resources.getColor(R.color.green))
                                    speedViewFuelLevel.setHighSpeedColor(resources.getColor(R.color.green))
                                }
                            }
                            2 -> {
                                val per: Float = (value.toFloat() * 100) / fr[1].toFloat()
                                speedViewFuelLevel.setAverageSpeedPercent(per)
                                if (fuelRangeArray.size == 3) {
                                    speedViewFuelLevel.setAverageHighSpeedPercent(per)
                                }
                            }
                            3 -> {
                                val per: Float = (value.toFloat() * 100) / fr[1].toFloat()
                                speedViewFuelLevel.setAverageHighSpeedPercent(per)
                            }
                        }
                    }
                }


                //VoltageGauge****************************************************************
                if (vitalType == "diesel") {
                    when (gaugeData.objectX.riskVOLTAGE) {
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
                }

                if (gaugeData.objectX.voltageRange.isFromDB) {
                    AppLogger.e("if voltage")
                    val startValue = gaugeData.objectX.voltageRange.ranges.critical[0]
                    val endValue = gaugeData.objectX.voltageRange.ranges.normal[1]
                    val totalPer = endValue - startValue
                    speedViewVoltage.setMinSpeed(startValue)
                    speedViewVoltage.maxGaugeValue = endValue
                    speedViewVoltage.tickNumber = 5
                    speedViewVoltage.speedTo(gaugeData.objectX.bATTERYVOLTAGE.toFloat())
                    speedViewVoltage.cancelSpeedAnimator()

                    val criticalRange =
                        gaugeData.objectX.voltageRange.ranges.critical[1] - gaugeData.objectX.voltageRange.ranges.critical[0]
                    val warningRange =
                        gaugeData.objectX.voltageRange.ranges.warning[1] - gaugeData.objectX.voltageRange.ranges.warning[0]
                    val normalRange =
                        gaugeData.objectX.voltageRange.ranges.normal[1] - gaugeData.objectX.voltageRange.ranges.normal[0]

                    AppLogger.e("percent----${(criticalRange * 100) / totalPer}")
                    AppLogger.e("percent----${(warningRange * 100) / totalPer}")
                    AppLogger.e("percent----${(normalRange * 100) / totalPer}")


                    val per1 = (criticalRange * 100) / totalPer
                    val per2 = ((warningRange * 100) / totalPer) + per1
                    val per3 = ((normalRange * 100) / totalPer) + per2

                    speedViewVoltage.setLowSpeedPercent(0F)
                    speedViewVoltage.setMediumSpeedPercent(per1)
                    speedViewVoltage.setAverageSpeedPercent(per2)
                    speedViewVoltage.setAverageHighSpeedPercent(per3)
                    speedViewVoltage.setLowSpeedColor(resources.getColor(R.color.red))
                    speedViewVoltage.setMediumSpeedColor(resources.getColor(R.color.red))
                    speedViewVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                    speedViewVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                    speedViewVoltage.setHighSpeedColor(resources.getColor(R.color.green))


                } else {
                    AppLogger.e("else voltage")
                    var voltageStartEndValue = gaugeData.objectX.fULLRangeVOLTAGE.replace("[", "")
                    voltageStartEndValue = voltageStartEndValue.replace("]", "")
                    val vr = voltageStartEndValue.split("-")
                    speedViewVoltage.setMinSpeed(vr[0].toFloat())
                    speedViewVoltage.maxGaugeValue = vr[1].toFloat()
                    speedViewVoltage.tickNumber = 5
                    speedViewVoltage.speedTo(gaugeData.objectX.bATTERYVOLTAGE.toFloat())
                    speedViewVoltage.cancelSpeedAnimator()
                    val jsonArrayVoltage = JSONArray(gaugeData.objectX.rangeVOLTAGE)
                    val voltageRangeArray = jsonArrayVoltage.join(",").split(",")

                    val rangeCount = vr[1].toInt() - vr[0].toInt()
                    val range1 = voltageRangeArray[0].toFloat() - vr[0].toInt()
                    val range2 = voltageRangeArray[1].toFloat() - vr[0].toInt()
                    val per1 = (range1 * 100) / rangeCount
                    val per2 = ((range2 * 100) / rangeCount)
                    AppLogger.e("per1--------$per1")
                    AppLogger.e("per2--------$per2")
                    speedViewVoltage.setLowSpeedPercent(0F)
                    speedViewVoltage.setMediumSpeedPercent(per1)
                    speedViewVoltage.setAverageSpeedPercent(per2)
                    speedViewVoltage.setAverageHighSpeedPercent(per2)
                    speedViewVoltage.setLowSpeedColor(Color.RED)
                    speedViewVoltage.setMediumSpeedColor(Color.RED)
                    speedViewVoltage.setAverageSpeedColor(resources.getColor(R.color.yellow))
                    speedViewVoltage.setAverageHighSpeedColor(resources.getColor(R.color.green))
                    speedViewVoltage.setHighSpeedColor(resources.getColor(R.color.green))
                }


                //ENGINE RUNNING**********************************
                if (vitalType == "diesel") {
                    when (gaugeData.objectX.idelStatus) {
                        "Critical" -> {
                            linMainBorder3.setBackgroundResource(R.drawable.trailer_border_red)
                            linSubBorder3.setBackgroundResource(R.drawable.trailer_border_red)
                        }
                        "Warning" -> {
                            linMainBorder3.setBackgroundResource(R.drawable.trailer_border_yellow)
                            linSubBorder3.setBackgroundResource(R.drawable.trailer_border_yellow)
                        }
                        "Normal" -> {
                            linMainBorder3.setBackgroundResource(R.drawable.trailer_border)
                            linSubBorder3.setBackgroundResource(R.drawable.trailer_border)
                        }
                    }
                }

                speedViewEngineRunning.setMinSpeed(0.0.toFloat())
                speedViewEngineRunning.maxGaugeValue = 120.0.toFloat()
                speedViewEngineRunning.tickNumber = 0

                speedViewEngineRunning.setLowSpeedPercent(0f)
                speedViewEngineRunning.setMediumSpeedPercent(0f)
                speedViewEngineRunning.setAverageSpeedPercent(0f)
                speedViewEngineRunning.setAverageHighSpeedPercent(0f)


                speedViewEngineRunning.setLowSpeedColor(resources.getColor(R.color.green))
                speedViewEngineRunning.setMediumSpeedColor(resources.getColor(R.color.green))
                speedViewEngineRunning.setAverageSpeedColor(resources.getColor(R.color.green))
                speedViewEngineRunning.setAverageHighSpeedColor(resources.getColor(R.color.green))
                speedViewEngineRunning.setHighSpeedColor(resources.getColor(R.color.green))


                //LAST TIME ENGINE RUN******************************************************
                if (vitalType == "diesel") {
                    when (gaugeData.objectX.riskLastTimeEngineRun) {
                        "Critical" -> {
                            linMainBorder4.setBackgroundResource(R.drawable.trailer_border_red)
                            linSubBorder4.setBackgroundResource(R.drawable.trailer_border_red)
                        }
                        "Warning" -> {
                            linMainBorder4.setBackgroundResource(R.drawable.trailer_border_yellow)
                            linSubBorder4.setBackgroundResource(R.drawable.trailer_border_yellow)
                        }
                        "Normal" -> {
                            linMainBorder4.setBackgroundResource(R.drawable.trailer_border)
                            linSubBorder4.setBackgroundResource(R.drawable.trailer_border)
                        }
                    }
                }

                var fullRangeLastTimeEngineRun =
                    gaugeData.objectX.fULLRangeLastTimeENGINERUN.replace("[", "")
                fullRangeLastTimeEngineRun = fullRangeLastTimeEngineRun.replace("]", "")
                val er = fullRangeLastTimeEngineRun.split("-")


                var minValsLastTimeEngineRun = 0.0F
                var maxValsLastTimeEngineRun = 0.0F
                val perLastTimeEngineRun = gaugeData.objectX.lastTimeEngineRun * 30 / 100
                if (gaugeData.objectX.lastTimeEngineRun > 90) {
                    minValsLastTimeEngineRun =
                        (gaugeData.objectX.lastTimeEngineRun - perLastTimeEngineRun).toFloat()
                    maxValsLastTimeEngineRun =
                        (gaugeData.objectX.lastTimeEngineRun + perLastTimeEngineRun).toFloat()
                } else {
                    minValsLastTimeEngineRun = defaultMinValue
                    maxValsLastTimeEngineRun = defaultMaxValue
                }

                AppLogger.e("min value LAST  -------$minValsLastTimeEngineRun")
                AppLogger.e("max value LAST  --------$maxValsLastTimeEngineRun")

                speedViewLastTimeEngineRun.maxGaugeValue = maxValsLastTimeEngineRun
                speedViewLastTimeEngineRun.setMinSpeed(minValsLastTimeEngineRun)
                speedViewLastTimeEngineRun.tickNumber = 10

                speedViewLastTimeEngineRun.speedTo(gaugeData.objectX.lastTimeEngineRun.toFloat())
                speedViewLastTimeEngineRun.cancelSpeedAnimator()
                speedViewLastTimeEngineRun.setLowSpeedPercent(0f)
                speedViewLastTimeEngineRun.setMediumSpeedPercent(50f)
                speedViewLastTimeEngineRun.setAverageSpeedPercent(75f)
                speedViewLastTimeEngineRun.setAverageHighSpeedPercent(75f)
                speedViewLastTimeEngineRun.setLowSpeedColor(resources.getColor(R.color.green))
                speedViewLastTimeEngineRun.setMediumSpeedColor(resources.getColor(R.color.green))
                speedViewLastTimeEngineRun.setAverageSpeedColor(resources.getColor(R.color.green))
                speedViewLastTimeEngineRun.setAverageHighSpeedColor(resources.getColor(R.color.green))
                speedViewLastTimeEngineRun.setHighSpeedColor(resources.getColor(R.color.green))



                //TOTAL RUN TIME****************************************************
                linMainBorder5.setBackgroundResource(R.drawable.trailer_border)
                linSubBorder5.setBackgroundResource(R.drawable.trailer_border)

                var fullRangeTotalRunTime = gaugeData.objectX.fULLRangeTOTALRUNTIME.replace("[", "")
                fullRangeTotalRunTime = fullRangeTotalRunTime.replace("]", "")
                val rr = fullRangeTotalRunTime.split("-")

                val minValsTotalTuntime: Float
                val maxValsTotalTuntime: Float
                val per = gaugeData.objectX.tOTALENGINERUNTIME * 40 / 100

                if (gaugeData.objectX.tOTALENGINERUNTIME > 90) {
                    minValsTotalTuntime =
                        (gaugeData.objectX.tOTALENGINERUNTIME - per.roundToInt()).toInt().toFloat()
                    maxValsTotalTuntime =
                        (gaugeData.objectX.tOTALENGINERUNTIME + per.roundToInt()).toInt().toFloat()
                } else {
                    minValsTotalTuntime = defaultMinValue
                    maxValsTotalTuntime = defaultMaxValue
                }

                AppLogger.e("min value--------$minValsTotalTuntime")
                AppLogger.e("max value--------$maxValsTotalTuntime")

                speedViewTotalRunTime.maxGaugeValue = maxValsTotalTuntime
                speedViewTotalRunTime.setMinSpeed(minValsTotalTuntime)

                speedViewTotalRunTime.tickNumber = 10
                speedViewTotalRunTime.setLowSpeedPercent(0f)
                speedViewTotalRunTime.setMediumSpeedPercent(0f)
                speedViewTotalRunTime.setAverageSpeedPercent(0f)
                speedViewTotalRunTime.setAverageHighSpeedPercent(0f)
                speedViewTotalRunTime.setHighSpeedColor(resources.getColor(R.color.green))

                AppLogger.e("${gaugeData.objectX.bATTERYVOLTAGE}")
                if (gaugeData.objectX.bATTERYVOLTAGE.equals(2.00)) {
                    linMainBorder2.setBackgroundResource(R.drawable.trailer_border_red)
                    linMainBorder3.setBackgroundResource(R.drawable.trailer_border_red)
                    linMainBorder4.setBackgroundResource(R.drawable.trailer_border_red)
                    tvVoltage.text = "Not Reachable"
                    tvEnginRunning.text = "Not Reachable"
                    tvLastTimeEnginRun.text = "Not Reachable"
                }
                loader.invisible()
                linContainer.visibility = View.VISIBLE
            })
    }

    override fun onClick(v: View?) {
        val intent = Intent(this@TrailerGaugeAct, ChartAct::class.java)
        when (v?.id) {
            R.id.linSubBorder1 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "FUEL_LEVEL")
                startActivity(intent)
            }

            R.id.linSubBorder2 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "BATTERY_VOLTAGE")
                startActivity(intent)
            }

            R.id.linSubBorder3 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "ENGINE_SPEED")
                startActivity(intent)
            }

            R.id.linSubBorder4 -> {
//                intent.putExtra(AppConstants.TRAILER_NAME,trailerName)
//                intent.putExtra(AppConstants.GAUGE_TYPE,"FUEL_LEVEL")
//                startActivity(intent)
            }

            R.id.linSubBorder5 -> {
                intent.putExtra(AppConstants.TRAILER_NAME, trailerName)
                intent.putExtra(AppConstants.GAUGE_TYPE, "TOTAL_ENGINE_RUN_TIME")
                startActivity(intent)
            }
        }
    }
}
