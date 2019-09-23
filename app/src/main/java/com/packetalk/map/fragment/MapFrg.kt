package com.packetalk.map.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.map.fragment.adapter.VideoCameraListAdapter
import com.packetalk.map.fragment.adapter.VideoCameraListAdapter.Companion.camPosition
import com.packetalk.map.fragment.model.MapItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.util.SharedPreferenceSession
import com.packetalk.util.getDisplayWidth
import com.packetalk.utility.MyWebView
import kotlinx.android.synthetic.main.dialog_map_camera_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFrg : BaseFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var rootView: View
    private var marker: Marker? = null
    var mapData: MapItem? = null
    var webCam: MyWebView? = null
    private lateinit var dialog: Dialog
    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_map, parent, false)
        return rootView
    }

    override fun init() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun initView() {
        dialog = Dialog(activity)
    }

    override fun postInitView() {
    }

    override fun addListener() {
    }

    override fun loadData() {
        getMapData()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        AppLogger.e("map load")
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        /*val gir = LatLng(21.169065, 70.596481)
        marker = mMap.addMarker(MarkerOptions().position(gir))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gir))
        */
        mMap.setOnMarkerClickListener { marker ->
            showDialog(marker.title)
            true
        }
    }

    private fun getMapData() {
        val session = activity?.let { SharedPreferenceSession(it) }
        val map = HashMap<String, String>()
        map["UserName"] = session!!.userName!!
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getMapData(map)
        callApi?.enqueue(object : Callback<MapItem> {
            override fun onResponse(call: Call<MapItem>, response: Response<MapItem>) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        mapData = response.body()
                        var onlyOnce = true
                        for ((i, value) in mapData?.objectX?.gPSLocations?.withIndex()!!) {
                            val data = LatLng(value.latitude, value.longitude)
                            marker = mMap.addMarker(
                                MarkerOptions().position(data).icon(
                                    BitmapDescriptorFactory.fromResource(
                                        R.drawable.baseline_router_black_48
                                    )
                                ).title("$i|${value.trailerNo.trim()}|${value.router.trim()}|${value.latitude}|${value.longitude}|${value.updatedAt.trim()}")
                            )
                            if (onlyOnce) {
                                val zoomLevel: Float = 16.0f; //This goes up to 21
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(data, zoomLevel))
                                onlyOnce = false
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MapItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(title: String) {
        val trailerData = title.split("|")
        val position: Int = trailerData[0].toInt()
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_map_camera_list)
        val tvTrailerName = dialog.findViewById(R.id.tvTrailerName) as TextView
        val tvRouterId = dialog.findViewById(R.id.tvRouterId) as TextView
        val tvLat = dialog.findViewById(R.id.tvLat) as TextView
        val tvLng = dialog.findViewById(R.id.tvLng) as TextView
        val recyclerViewMapCamera = dialog.findViewById(R.id.recyclerViewMapCamera) as RecyclerView
//        webCam = dialog.findViewById(R.id.webCamera) as MyWebView

        val tvUpdated = dialog.findViewById(R.id.tvUpdated) as TextView

        tvTrailerName.text = trailerData[1]
        tvRouterId.text = trailerData[2]
        tvLat.text = trailerData[3]
        tvLng.text = trailerData[4]
        tvUpdated.text = trailerData[5]

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewMapCamera.layoutManager = layoutManager
        val adapter = VideoCameraListAdapter(
            activity,
            mapData?.objectX?.gPSLocations?.get(position)?.camdetails
        )

        val width = activity!!.getDisplayWidth() - 40

        if (!mapData?.objectX?.gPSLocations?.get(position)?.camdetails?.isEmpty()!!) {
            recyclerViewMapCamera.adapter = adapter
            var cam = mapData?.objectX?.gPSLocations?.get(position)?.camdetails?.get(0)
            val url =
                "${AppConstants.HTTP_BIND}${cam?.serverURL}:${cam?.serverPort}/viewpanel/${cam?.cameraIDOnServer}/viewpanel&imagewidth=$width&imageheight=150"
            AppLogger.e(url)
            activity?.let {
                dialog.webCamera.load(url, it)
            }

            adapter.itemArrayList?.get(0)!!.isSelected = true

            dialog.imgZoom.setOnClickListener {
                val cameraItem: CameraDetailsFull? =
                    mapData!!.objectX.gPSLocations[position].camdetails[camPosition]
                val intent = Intent(activity, CameraDetailAct::class.java)
                intent.putExtra(AppConstants.CAMERA_DETAIL_KEY, cameraItem)
                activity?.startActivity(intent)
            }
        }

        adapter.onItemClick = { cameraDetail ->
            cameraDetail.isSelected = true
            adapter.notifyDataSetChanged()
            val url =
                "${AppConstants.HTTP_BIND}${cameraDetail.serverURL}:${cameraDetail.serverPort}/viewpanel/${cameraDetail.cameraIDOnServer}/viewpanel&imagewidth=300&imageheight=150"
            AppLogger.e(url)
            activity?.let {
                dialog.webCamera.load(url, it)
            }
        }

        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

}
