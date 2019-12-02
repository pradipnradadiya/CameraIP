package com.packetalk.Trailer.fragment.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerSubGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.Object
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.Trailer.fragment.model.trailer.TrailerSubGaugeItem
import com.packetalk.util.AppLogger
import com.packetalk.view_model.trailer.TrailerGaugeDataModel
import kotlinx.android.synthetic.main.act_trailer_gauge.*
import kotlinx.android.synthetic.main.gauge_engine_running.*
import kotlinx.android.synthetic.main.gauge_fuel_level.*
import kotlinx.android.synthetic.main.gauge_last_time_engine_run.*
import kotlinx.android.synthetic.main.gauge_total_run_time.*
import kotlinx.android.synthetic.main.gauge_voltage.*
import org.json.JSONArray

class TrailerGaugeAct2 : BaseActivity() {
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
        bindToolBarBack("Trailer")
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
    }

    @SuppressLint("SetTextI18n")
    override fun loadData() {
        showProgressDialog("Trailer Gauge", "Please wait..")
        val data =
            ViewModelProviders.of(this@TrailerGaugeAct2).get(TrailerGaugeDataModel::class.java)
        data.getTrailer(vitalType.toString(), trailerName.toString()).observe(this,
            Observer<TrailerSubGaugeItem> { gaugeData ->
                AppLogger.e("data is----------" + gaugeData.objectX.toString())
                hideProgressDialog()

                tvFualLevel.text = "${gaugeData.objectX.solarVoltage} V"
                tvVoltage.text = "${gaugeData.objectX.bATTERYVOLTAGE} V"
//                tvEnginRunning.text = "${gaugeData.objectX.idelStatus} "
//                tvLastTimeEnginRun.text = "${gaugeData.objectX.lastTimeEngineRun} Min"
                tvTotalRunTime.text = "${gaugeData.objectX.temperature} F"

                //Fuel Gauge
                if (vitalType == "solar") {
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


                var fuelStartEndValue = gaugeData.objectX.fULLRangeSOLARVOLTAGE.replace("[","")
                fuelStartEndValue = fuelStartEndValue.replace("]","")
                val fr = fuelStartEndValue.split("-")

                speedViewFuelLevel.setMinSpeed(fr[0].toFloat())
                speedViewFuelLevel.setGaugeMaxSpeed(fr[1].toFloat())
                val list = ArrayList<Float>()
                val count = fr[1].toInt() / 10
                for(i in 0..count){
                    list.add(i.toFloat() * 10)
                }
                speedViewFuelLevel.setTicks(list)
                speedViewFuelLevel.speedTo(60f)
                speedViewFuelLevel.cancelSpeedAnimator()

                val jsonArrayFuel = JSONArray(gaugeData.objectX.rangeSOLARVOLTAGE)
                val fuelRangeArray = jsonArrayFuel.join(",").split(",")
//                AppLogger.e("fuel range array-----------$fuelRangeArray")

                val per:Float = (fuelRangeArray[0].toFloat() * 100) / fr[1].toFloat()
                val per1:Float = (fuelRangeArray[1].toFloat() * 100) / fr[1].toFloat()

                AppLogger.e("-------------${per}")
                AppLogger.e("-------------${per1}")

//                speedViewFuelLevel.setLowSpeedPercent(87.5F)
//                speedViewFuelLevel.setMediumSpeedPercent(93.75F)

                for ((i, value) in fuelRangeArray.withIndex()) {
                    val per:Float = (value.toFloat() * 100) / fr[1].toFloat()

                    /*
                    when (i) {
                        0 -> {
                            val per:Float = (value.toFloat() * 100) / fr[1].toFloat()
                            speedViewFuelLevel.setLowSpeedPercent(per)
                            speedViewFuelLevel.setLowSpeedColor(Color.RED)
                            if (fuelRangeArray.size==1){
//                                speedViewFuelLevel.setMediumSpeedPercent(per)
                                speedViewFuelLevel.setAverageSpeedPercent(per)
                                speedViewFuelLevel.setAverageHighSpeedPercent(per)
                            }
                        }
                        1 -> {
                            val per:Float = (value.toFloat() * 100) / fr[1].toFloat()
                            speedViewFuelLevel.setMediumSpeedPercent(per)
                            if (fuelRangeArray.size==2){
                                speedViewFuelLevel.setAverageSpeedPercent(per)
                                speedViewFuelLevel.setAverageHighSpeedPercent(per)
                            }
                        }
                        2 -> {
                            val per:Float = (value.toFloat() * 100) / fr[1].toFloat()
                            speedViewFuelLevel.setAverageSpeedPercent(per)
                            if (fuelRangeArray.size==3){
                                speedViewFuelLevel.setAverageHighSpeedPercent(per)
                            }
                        }
                        3 -> {
                            val per:Float = (value.toFloat() * 100) / fr[1].toFloat()
                            speedViewFuelLevel.setAverageHighSpeedPercent(per)
                        }
                    }*/
                }



                //VoltageGauge
               /*
                var voltageStartEndValue = gaugeData.objectX.fULLRangeBattryVOLTAGE.replace("[","")
                voltageStartEndValue = voltageStartEndValue.replace("]","")
                val vr = voltageStartEndValue.split("-")
                speedViewVoltage.setMinSpeed(vr[0].toFloat())
                speedViewVoltage.setGaugeMaxSpeed(vr[1].toFloat())
                val listVolatge = ArrayList<Float>()
                val startVoltage = vr[0].toInt() - 1
                val countVoltage = vr[1].toInt() - 1
                AppLogger.e("count voltage------------$countVoltage")

                for(i in startVoltage..countVoltage){
                    AppLogger.e(i.toString())
                    val par = i + 1
                    AppLogger.e(""+par.toFloat())
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
                            val per:Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setLowSpeedPercent(per)
//                            speedViewVoltage.setLowSpeedColor(Color.RED)
                        }
                        1 -> {
                            val per:Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setMediumSpeedPercent(per)
                        }
                        2 -> {
                            val per:Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setAverageSpeedPercent(per)
                        }
                        3 -> {
                            val per:Float = (value.toFloat() * 100) / vr[1].toFloat()
                            AppLogger.e("per-----------$voltageRangeArray")
//                            speedViewVoltage.setAverageHighSpeedPercent(per)
                        }
                    }
                }*/

                //ENGINE RUNNING
                /*
                speedViewEngineRunning.setMinSpeed(0f)
                speedViewEngineRunning.setGaugeMaxSpeed(120f)
                speedViewEngineRunning.cancelSpeedAnimator()
                speedViewEngineRunning.setLowSpeedPercent(0f)
                speedViewEngineRunning.setMediumSpeedPercent(0f)
                speedViewEngineRunning.setAverageSpeedPercent(0f)
                speedViewEngineRunning.setAverageHighSpeedPercent(0f)
                speedViewEngineRunning.setHighSpeedColor(Color.GREEN)
*/

                //TOTAL RUN TIME
/*
                var fullRangeTotalRunTime = gaugeData.objectX.fULLRangeTEMPERATURE.replace("[","")
                fullRangeTotalRunTime = fullRangeTotalRunTime.replace("]","")
                val rr = fullRangeTotalRunTime.split("-")
                speedViewTotalRunTime.setMinSpeed(rr[0].toFloat())
                speedViewTotalRunTime.setGaugeMaxSpeed(rr[1].toFloat())
                val listTotalRunTime = ArrayList<Float>()
                val countTotalRuntime = rr[1].toInt() / 5 

                for(i in 0..countTotalRuntime){
                    listTotalRunTime.add(i.toFloat() * 5)
                }

                speedViewTotalRunTime.setTicks(listTotalRunTime)
                speedViewTotalRunTime.cancelSpeedAnimator()
                speedViewTotalRunTime.setLowSpeedPercent(0f)
                speedViewTotalRunTime.setMediumSpeedPercent(0f)
                speedViewTotalRunTime.setAverageSpeedPercent(0f)
                speedViewTotalRunTime.setAverageHighSpeedPercent(0f)
                speedViewTotalRunTime.setHighSpeedColor(Color.RED)*/

            })

    }

}
