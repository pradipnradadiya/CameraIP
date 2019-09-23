package com.packetalk.setting.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.retrofit.APIClient
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.ServerSettingAdapter
import com.packetalk.setting.model.server_setting.ServerItem
import com.packetalk.util.AppLogger
import com.packetalk.util.setLoader
import kotlinx.android.synthetic.main.act_server_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerSettingAct : BaseActivity() {
    var adapter: ServerSettingAdapter? = null
    var layoutManager: LinearLayoutManager? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_server_setting
    }

    override fun init() {
        bindToolBarBack("Server Setting")
    }

    override fun initView() {
        loader.controller = setLoader()
        layoutManager = LinearLayoutManager(this@ServerSettingAct)
    }

    override fun postInitView() {
    }

    override fun addListener() {
    }

    override fun loadData() {
        getServer()
    }

    private fun getServer() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getServerUrl()
        callApi!!.enqueue(object : Callback<ServerItem> {
            override fun onResponse(call: Call<ServerItem>, response: Response<ServerItem>) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        loader.visibility = View.INVISIBLE
                        recycleViewServer.layoutManager = layoutManager
                        adapter =
                            ServerSettingAdapter(this@ServerSettingAct, response.body()!!.objectX)
                        recycleViewServer.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<ServerItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
            }

        })
    }

}
