package com.packetalk.setting.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.retrofit.APIClient
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.ModemSettingAdapter
import com.packetalk.setting.model.modem_setting.ModemItem
import com.packetalk.util.AppLogger
import com.packetalk.util.invisible
import com.packetalk.util.setLoader
import kotlinx.android.synthetic.main.act_modem_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModemSettingAct : BaseActivity() {
    var layoutManager: LinearLayoutManager? = null
    var adapter: ModemSettingAdapter? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_modem_setting
    }

    override fun init() {
        bindToolBarBack("Modem Setting")
    }

    override fun initView() {
        loader.controller = setLoader()
        layoutManager = LinearLayoutManager(this@ModemSettingAct)
    }

    override fun postInitView() {
    }

    override fun addListener() {
    }

    override fun loadData() {
        getModemList()
    }

    private fun getModemList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.modemList()
        callApi!!.enqueue(object : Callback<ModemItem> {
            override fun onResponse(call: Call<ModemItem>, response: Response<ModemItem>) {
//                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        loader.invisible()
                        recycleViewModemSetting.layoutManager = layoutManager
                        adapter =
                            ModemSettingAdapter(this@ModemSettingAct, response.body()!!.objectX)
                        recycleViewModemSetting.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<ModemItem>, t: Throwable) {
//                AppLogger.error(t.message.toString())
            }

        })
    }

}
