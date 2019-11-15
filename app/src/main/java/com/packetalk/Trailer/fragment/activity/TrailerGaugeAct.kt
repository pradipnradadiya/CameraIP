package com.packetalk.Trailer.fragment.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerSubGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.Object
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.util.AppLogger
import com.packetalk.view_model.trailer.TrailerGaugeDataModel
import kotlinx.android.synthetic.main.act_trailer_gauge.*

class TrailerGaugeAct : BaseActivity() {
    var adapter : TrailerSubGaugeAdapter? = null
    var layoutManager : LinearLayoutManager? = null
    private var arrSubGaugeList = ArrayList<Object>()
    var vitalType: String? = null
    var trailerName: String? = null
    override fun getLayoutResourceId(): Int {
        return R.layout.act_trailer_gauge
    }

    override fun init() {
        vitalType = intent.getStringExtra("vitalType")
        trailerName = intent.getStringExtra("trailerName")
        layoutManager = LinearLayoutManager(this@TrailerGaugeAct)
    }

    override fun initView() {
        bindToolBarBack("Trailer")
        recycleviewTrailerGauge.layoutManager = layoutManager
    }

    override fun postInitView() {
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(recycleviewTrailerGauge)
        adapter = TrailerSubGaugeAdapter(this@TrailerGaugeAct,arrSubGaugeList)
        recycleviewTrailerGauge.adapter = adapter
    }

    override fun addListener() {
        fabMap.setOnClickListener {

        }
    }

    override fun loadData() {
        val data = ViewModelProviders.of(this@TrailerGaugeAct).get(TrailerGaugeDataModel::class.java)
        data.getTrailer("","").observe(this,
            Observer<TrailerGaugeItem> { gaugeData ->
                AppLogger.e("data is----------"+gaugeData.objectX.toString())
                arrSubGaugeList.addAll(gaugeData.objectX!!)
                adapter?.notifyDataSetChanged()
            })
    }
}
