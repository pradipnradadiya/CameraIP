package com.packetalk.Trailer.fragment
import android.os.Bundle
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
import com.packetalk.util.SimpleDividerItemDecoration
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
    }

    override fun addListener() {

        /*// Add label converter
        rootView.meterView.labelConverter =
            SpeedometerView.LabelConverter { progress, maxProgress ->
                Math.round(progress).toInt().toString()
            }*/

    }

    override fun loadData() {
        getTrailerList()

        /*  // configure value range and ticks
          rootView.meterView.maxSpeed = 160.0
          rootView.meterView.majorTickStep = 20.0
          rootView.meterView.minorTicks = 2
          rootView.meterView.speed = 90.0
          // Configure value range colors
          rootView.meterView.addColoredRange(30.0, 140.0, Color.GREEN);
          rootView.meterView.addColoredRange(140.0, 180.0, Color.YELLOW);
          rootView.meterView.addColoredRange(180.0, 400.0, Color.RED);*/
    }

    private fun getTrailerList() {
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
//                        val divider = SimpleDividerItemDecoration(resources)
//                        rootView.recycleViewTrailer.addItemDecoration(divider)
                    }
                }
            }

            override fun onFailure(call: Call<TrailerGaugeItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
            }

        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
//        menu.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
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

}
