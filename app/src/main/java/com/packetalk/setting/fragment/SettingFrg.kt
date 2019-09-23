package com.packetalk.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.setting.activity.*
import kotlinx.android.synthetic.main.frg_setting.*

class SettingFrg : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.linTrailer -> {
                startNewActivity(AddTrailerACt::class.java)
            }
            R.id.linGroup -> {
                startNewActivity(AddGroupAct::class.java)
            }
            R.id.linCamera -> {
                startNewActivity(AddCameraAct::class.java)
            }
            R.id.linMyCameraSetting -> {
                startNewActivity(MyCameraSettingAct::class.java)
            }
            R.id.linServerSetting -> {
                startNewActivity(ServerSettingAct::class.java)
            }
            R.id.linModemSetting -> {
                startNewActivity(ModemSettingAct::class.java)
            }
        }
    }

    private lateinit var rootView: View

    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_setting, parent, false)
        return rootView
    }

    override fun init() {
    }

    override fun initView() {
    }

    override fun postInitView() {
    }

    override fun addListener() {
        linTrailer.setOnClickListener(this)
        linGroup.setOnClickListener(this)
        linCamera.setOnClickListener(this)
        linMyCameraSetting.setOnClickListener(this)
        linServerSetting.setOnClickListener(this)
        linModemSetting.setOnClickListener(this)
    }

    override fun loadData() {
    }


}
