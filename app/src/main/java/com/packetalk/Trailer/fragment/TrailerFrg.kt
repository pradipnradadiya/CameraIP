package com.packetalk.Trailer.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.Trailer.fragment.adapter.TrailerGaugeAdapter
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AddTrailerACt
import com.packetalk.util.AppLogger
import com.packetalk.util.invisible
import com.packetalk.util.setLoader
import com.packetalk.util.visible
import kotlinx.android.synthetic.main.frg_trailer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerFrg : BaseFragment() {
    lateinit var rootView: View
    var layoutManager: LinearLayoutManager? = null
    var adapter: TrailerGaugeAdapter? = null

    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_trailer, parent, false)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun init() {
        layoutManager = LinearLayoutManager(activity)
    }

    override fun initView() {
        rootView.recycleViewTrailer.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(rootView.recycleViewTrailer)
    }

    override fun postInitView() {
        rootView.loader.controller = setLoader()
    }

    override fun addListener() {

    }

    override fun loadData() {

        getTrailerList()
    }

    private fun getTrailerList() {

//        showProgressDialog("Trailer", "Please wait..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getTrailerGaugeList()
        callApi?.enqueue(object : Callback<TrailerGaugeItem> {
            override fun onResponse(
                call: Call<TrailerGaugeItem>,
                response: Response<TrailerGaugeItem>
            ) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        adapter = TrailerGaugeAdapter(activity, response.body()?.objectX)
                        rootView.recycleViewTrailer.adapter = adapter
                        val r = Runnable {
                            rootView.loader.invisible()
                            rootView.recycleViewTrailer.visible()
                            repeatCall()
                        }
                        val h = Handler()
                        h.postDelayed(r, 5000)
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

    override fun onPrepareOptionsMenu(menu: Menu) {
//        menu.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.trailer_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_trailer -> {
                startNewActivity(AddTrailerACt::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun repeatCall() {
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                loadData()
                repeatCall()//again call your method
            }
        }.start()
    }
}
