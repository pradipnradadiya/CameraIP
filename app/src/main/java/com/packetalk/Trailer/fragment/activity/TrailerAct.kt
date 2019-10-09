package com.packetalk.Trailer.fragment.activity

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.util.AppLogger
import com.packetalk.view_model.TrailerDataModel
import kotlinx.android.synthetic.main.act_trailer.*


class TrailerAct : BaseActivity() {
    var randNumber = 0
    override fun getLayoutResourceId(): Int {
        return R.layout.act_trailer
    }

    override fun init() {
        /*
        val rand = RandomNumberGenerator()
//        rand = ViewModelProvider
        val mFavViewModel = ViewModelProviders.of(this@TrailerAct).get(RandomNumberGenerator::class.java)
        val mFavViewModel2 = ViewModelProviders.of(this@TrailerAct).get(RandomNumberGenerator::class.java)
        randNumber = mFavViewModel.getMyRandomNumber()?.toInt() ?: 0
        val data = mFavViewModel2.getTrailerList()
        AppLogger.e(randNumber.toString())
        AppLogger.e(data.toString())
        meterView.labelConverter =
            SpeedometerView.LabelConverter { progress, maxProgress ->
                Math.round(progress).toInt().toString()
            }
        */
        val data = ViewModelProviders.of(this@TrailerAct).get(TrailerDataModel::class.java)
        data.getTrailer().observe(this,
            Observer<TrailerGaugeItem> { heroList ->
                AppLogger.e("data is----------"+heroList.objectX.toString())
            })

    }

    override fun initView() {
        // configure value range and ticks
        meterView.maxSpeed = 160.0
        meterView.majorTickStep = 20.0
        meterView.minorTicks = 2
        meterView.speed = randNumber.toDouble()
        // Configure value range colors
        meterView.addColoredRange(0.0, 30.0, Color.CYAN)
        meterView.addColoredRange(30.0, 80.0, Color.GREEN)
        meterView.addColoredRange(80.0, 120.0, Color.YELLOW)
        meterView.addColoredRange(120.0, 160.0, Color.RED)
    }

    override fun postInitView() {

    }

    override fun addListener() {

    }

    override fun loadData() {

    }

}
