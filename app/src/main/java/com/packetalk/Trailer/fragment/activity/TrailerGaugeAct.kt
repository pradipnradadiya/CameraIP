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
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.view_model.trailer.TrailerGaugeDataModel
import kotlinx.android.synthetic.main.act_trailer_gauge.*
import kotlinx.android.synthetic.main.gauge_engine_running.*
import kotlinx.android.synthetic.main.gauge_fuel_level.*
import kotlinx.android.synthetic.main.gauge_last_time_engine_run.*
import kotlinx.android.synthetic.main.gauge_total_run_time.*
import kotlinx.android.synthetic.main.gauge_voltage.*
import org.json.JSONArray

class TrailerGaugeAct : BaseActivity(), View.OnClickListener {
    private var adapter: TrailerSubGaugeAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var arrSubGaugeList = ArrayList<TrailerSubGaugeItem>()
    private var vitalType: String? = null
    private var trailerName: String? = null

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
        showProgressDialog("Trailer Gauge", "Please wait..")

        val data =
            ViewModelProviders.of(this@TrailerGaugeAct).get(TrailerGaugeDataModel::class.java)
        data.getTrailer(vitalType.toString(), trailerName.toString()).observe(this,
            Observer<TrailerSubGaugeItem> { gaugeData ->
                AppLogger.e("data is----------" + gaugeData.objectX.toString())
                hideProgressDialog()

                tvFualLevel.text = "${gaugeData.objectX.fUELLEVEL} %"
                tvVoltage.text = "${gaugeData.objectX.bATTERYVOLTAGE} V"
                if (gaugeData.objectX.isIdel) {
//                    tvEnginRunning.text = "${gaugeData.objectX.idelStatus} "
                    tvEnginRunning.text = "Idle"
                } else {
                    tvEnginRunning.text = "Off"
                }

                tvLastTimeEnginRun.text = "${gaugeData.objectX.lastTimeEngineRun} Min"
                tvTotalRunTime.text = "${gaugeData.objectX.tOTALENGINERUNTIME} Hrs"

                //Fuel Gauge
                if (vitalType == "diesel") {
                    when {
                        gaugeData.objectX.riskFUELLEVEL == "Critical" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border_red)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border_red)
                        }
                        gaugeData.objectX.riskFUELLEVEL == "Warning" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border_yellow)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border_yellow)
                        }
                        gaugeData.objectX.riskFUELLEVEL == "Normal" -> {
                            linMainBorder1.setBackgroundResource(R.drawable.trailer_border)
                            linSubBorder1.setBackgroundResource(R.drawable.trailer_border)
                        }

                    }
                }

                var fuelStartEndValue = gaugeData.objectX.fULLRangeFUELLEVEL.replace("[", "")
                fuelStartEndValue = fuelStartEndValue.replace("]", "")
                val fr = fuelStartEndValue.split("-")

                speedViewFuelLevel.setMinSpeed(fr[0].toFloat())
                speedViewFuelLevel.setGaugeMaxSpeed(fr[1].toFloat())

                val list = ArrayList<Float>()
                val count = fr[1].toInt() / 10
                for (i in 0..count) {
                    list.add(i.toFloat() * 10)
                }

                speedViewFuelLevel.setTicks(list)
                speedViewFuelLevel.speedTo(60f)
                speedViewFuelLevel.cancelSpeedAnimator()

                val jsonArrayFuel = JSONArray(gaugeData.objectX.rangeFUELLEVEL)
                val fuelRangeArray = jsonArrayFuel.join(",").split(",")
//                AppLogger.e("fuel range array-----------$fuelRangeArray")


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

                //VoltageGauge
                if (vitalType == "diesel") {
                    when {
                        gaugeData.objectX.riskVOLTAGE == "Critical" -> {
                            linMainBorder2.setBackgroundResource(R.drawable.trailer_border_red)
                            linSubBorder2.setBackgroundResource(R.drawable.trailer_border_red)
                        }
                        gaugeData.objectX.riskVOLTAGE == "Warning" -> {
                            linMainBorder2.setBackgroundResource(R.drawable.trailer_border_yellow)
                            linSubBorder2.setBackgroundResource(R.drawable.trailer_border_yellow)
                        }
                        gaugeData.objectX.riskVOLTAGE == "Normal" -> {
                            linMainBorder2.setBackgroundResource(R.drawable.trailer_border)
                            linSubBorder2.setBackgroundResource(R.drawable.trailer_border)
                        }
                    }
                }

                var voltageStartEndValue = gaugeData.objectX.fULLRangeVOLTAGE.replace("[", "")
                voltageStartEndValue = voltageStartEndValue.replace("]", "")
                val vr = voltageStartEndValue.split("-")
                speedViewVoltage.setMinSpeed(vr[0].toFloat())
                speedViewVoltage.setGaugeMaxSpeed(vr[1].toFloat())
                val listVolatge = ArrayList<Float>()
                val startVoltage = vr[0].toInt() - 1


                val countVoltage = vr[1].toInt() - 1
                AppLogger.e("count voltage------------$countVoltage")
                for (i in startVoltage..countVoltage) {
                    AppLogger.e(i.toString())
                    val par = i + 1
                    AppLogger.e("" + par.toFloat())
                    listVolatge.add(par.toFloat())
                }

                speedViewVoltage.setTicks(listVolatge)
                speedViewVoltage.speedTo(25f)
                speedViewVoltage.cancelSpeedAnimator()
                val jsonArrayVoltage = JSONArray(gaugeData.objectX.rangeVOLTAGE)
                val voltageRangeArray = jsonArrayVoltage.join(",").split(",")

                speedViewVoltage.setLowSpeedPercent(18.38F)
                speedViewVoltage.setLowSpeedColor(Color.RED)

                speedViewVoltage.setMediumSpeedPercent(20F)
                speedViewVoltage.setMediumSpeedColor(Color.CYAN)
                speedViewVoltage.setAverageSpeedPercent(20F)
                speedViewVoltage.setAverageHighSpeedPercent(20F)


//                AppLogger.e("voltage range array-----------$voltageRangeArray")
                for ((i, value) in voltageRangeArray.withIndex()) {
                    when (i) {
                        0 -> {
                            val per: Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setLowSpeedPercent(per)
//                            speedViewVoltage.setLowSpeedColor(Color.RED)
                        }
                        1 -> {
                            val per: Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setMediumSpeedPercent(per)
                        }
                        2 -> {
                            val per: Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setAverageSpeedPercent(per)
                        }
                        3 -> {
                            val per: Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setAverageHighSpeedPercent(per)
                        }
                    }
                }

                //ENGINE RUNNING

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

                speedViewEngineRunning.setMinSpeed(0f)
                speedViewEngineRunning.setGaugeMaxSpeed(120f)
                speedViewEngineRunning.cancelSpeedAnimator()
                speedViewEngineRunning.setLowSpeedPercent(0f)
                speedViewEngineRunning.setMediumSpeedPercent(0f)
                speedViewEngineRunning.setAverageSpeedPercent(0f)
                speedViewEngineRunning.setAverageHighSpeedPercent(0f)
                speedViewEngineRunning.setHighSpeedColor(Color.GREEN)

                //LAST TIME ENGINE RUN
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

                speedViewLastTimeEngineRun.setMinSpeed(er[0].toFloat())
                speedViewLastTimeEngineRun.setGaugeMaxSpeed(er[1].toFloat())

                val listEngineRun = ArrayList<Float>()
                val countEngineRun = er[1].toInt() / 5
                for (i in 0..countEngineRun) {
                    listEngineRun.add(i.toFloat() * 5)
                }

                speedViewLastTimeEngineRun.setTicks(listEngineRun)
                speedViewLastTimeEngineRun.speedTo(15f)
                speedViewLastTimeEngineRun.cancelSpeedAnimator()
                speedViewLastTimeEngineRun.setLowSpeedPercent(8f)
                speedViewLastTimeEngineRun.setMediumSpeedPercent(12f)
                speedViewLastTimeEngineRun.setAverageSpeedPercent(12f)
                speedViewLastTimeEngineRun.setAverageHighSpeedPercent(12f)
                speedViewLastTimeEngineRun.setHighSpeedColor(Color.RED)

                //TOTAL RUN TIME
                linMainBorder5.setBackgroundResource(R.drawable.trailer_border)
                linSubBorder5.setBackgroundResource(R.drawable.trailer_border)

                var fullRangeTotalRunTime = gaugeData.objectX.fULLRangeTOTALRUNTIME.replace("[", "")
                fullRangeTotalRunTime = fullRangeTotalRunTime.replace("]", "")
                val rr = fullRangeTotalRunTime.split("-")
                speedViewTotalRunTime.setMinSpeed(rr[0].toFloat())
                speedViewTotalRunTime.setGaugeMaxSpeed(rr[1].toFloat())
                val listTotalRunTime = ArrayList<Float>()
                val countTotalRuntime = rr[1].toInt() / 5

                for (i in 0..countTotalRuntime) {
                    listTotalRunTime.add(i.toFloat() * 5)
                }
                speedViewTotalRunTime.setTicks(listTotalRunTime)
                speedViewTotalRunTime.cancelSpeedAnimator()
                speedViewTotalRunTime.setLowSpeedPercent(0f)
                speedViewTotalRunTime.setMediumSpeedPercent(0f)
                speedViewTotalRunTime.setAverageSpeedPercent(0f)
                speedViewTotalRunTime.setAverageHighSpeedPercent(0f)
                speedViewTotalRunTime.setHighSpeedColor(Color.RED)

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
