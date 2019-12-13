package com.packetalk.Trailer.fragment.activity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_trailer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HybridVitalGaugeAct : BaseActivity() {

    private var vitalType: String? = null
    private var trailerName: String? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.frg_trailer2
    }

    var layoutManager: LinearLayoutManager? = null
    var adapter: TrailerGaugeAdapter? = null

    override fun init() {
        vitalType = intent.getStringExtra("vitalType")
        trailerName = intent.getStringExtra("trailerName")
        layoutManager = LinearLayoutManager(this)
    }

    override fun initView() {
        bindToolBarBack(trailerName.toString())
        recycleViewTrailer.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(recycleViewTrailer)
    }

    override fun postInitView() {

    }

    override fun addListener() {

    }

    override fun loadData() {
        getHybridTrailerList(trailerName.toString())
    }

    private fun getHybridTrailerList(trailerName: String) {
        showProgressDialog("Trailer","Please wait..")
        val map = HashMap<String,String>()
        map["TrailerName"] = trailerName
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getHybridTrailerGaugeList(map)
        callApi?.enqueue(object : Callback<TrailerGaugeItem> {

            override fun onResponse(
                call: Call<TrailerGaugeItem>,
                response: Response<TrailerGaugeItem>
            ) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    hideProgressDialog()
                    if (response.body()?.responseResult!!) {
                        adapter =
                            TrailerGaugeAdapter(this@HybridVitalGaugeAct, response.body()?.objectX)
                        recycleViewTrailer.adapter = adapter
//                        val divider = SimpleDividerItemDecoration(resources)
//                        rootView.recycleViewTrailer.addItemDecoration(divider)
                    }
                }
            }

            override fun onFailure(call: Call<TrailerGaugeItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
                hideProgressDialog()
            }
        })
    }
}
