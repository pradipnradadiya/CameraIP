package com.packetalk.map.fragment.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.map.fragment.adapter.MapCamListAdapter
import com.packetalk.util.AppConstants
import kotlinx.android.synthetic.main.act_map_camera_view.*

class MapCameraViewAct : BaseActivity() {

    var camDetails = ArrayList<CameraDetailsFull>()

    override fun getLayoutResourceId(): Int {
        return R.layout.act_map_camera_view
    }

    override fun init() {
        bindToolBarBack("Camera View")
        camDetails = intent.getSerializableExtra(AppConstants.CAMERA_DETAIL_KEY) as ArrayList<CameraDetailsFull>
    }

    override fun initView() {
        val linearLayoutManager2 = LinearLayoutManager(this@MapCameraViewAct)
        recycleviewCam?.layoutManager = linearLayoutManager2
        val cameraAdapter =
            MapCamListAdapter(this@MapCameraViewAct, camDetails)
        recycleviewCam?.adapter = cameraAdapter
    }

    override fun postInitView() {
    }

    override fun addListener() {
    }

    override fun loadData() {
    }


}
