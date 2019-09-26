package com.packetalk.setting.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.TrailerAdapter
import com.packetalk.setting.model.trailer.Object
import com.packetalk.setting.model.trailer.TrailerItem
import com.packetalk.setting.request.trailor.TrailerRequest
import com.packetalk.util.AppLogger
import com.packetalk.util.Validator
import com.packetalk.util.parseJsonObject
import com.packetalk.util.showSuccessToast
import kotlinx.android.synthetic.main.act_add_trailer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTrailerACt : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmit -> {
                if (btnSubmit.text == "Submit") {
                    val check = validateTrailer()
                    if (check){
                        addTrailer()
                    }

                } else if (btnSubmit.text == "Update") {
                    val check = validateTrailer()
                    if (check) {
                        updateTrailer()
                    }
                }
            }
            R.id.btnCancel -> {
                btnSubmit.text = getString(R.string.submit)
                edTrailerName.setText("")
                edDns.setText("")
            }
        }
    }

    private val trailerArr = ArrayList<Object>()
    var layoutManager: LinearLayoutManager? = null
    var adapter: TrailerAdapter? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_add_trailer
    }

    override fun init() {
        bindToolBarBack("Add Trailer")
        layoutManager = LinearLayoutManager(this@AddTrailerACt)
    }

    override fun initView() {
    }

    override fun postInitView() {
    }

    override fun addListener() {
        btnSubmit.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun loadData() {
        getTrailerList()
    }

    private fun validateTrailer(): Boolean {
        if (!Validator.checkEmptyInputText(edDns, getString(R.string.pls_enter_dns))) {
            return false
        } else if (!Validator.checkEmptyInputText(edTrailerName, getString(R.string.pls_enter_tnm))) {
            return false
        }
        return true
    }

    //using getting a list of trailer
    private fun getTrailerList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getTrailerList()
        callApi!!.enqueue(object : Callback<TrailerItem> {
            override fun onResponse(call: Call<TrailerItem>, response: Response<TrailerItem>) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        recycleViewTrailerList.layoutManager = layoutManager
                        adapter =
                            TrailerAdapter(this@AddTrailerACt, response.body()!!.objectX)
                        recycleViewTrailerList.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<TrailerItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
            }
        })
    }

    fun updateTrailer(
        data: Object
    ) {
        btnSubmit.text = getString(R.string.update)
        edDns.setText(data.dNS)
        edTrailerName.setText(data.trailerName)
        trailerArr.clear()
        trailerArr.add(data)
    }


    //using add trailer
    private fun addTrailer() {
        trailerArr.clear()
        val obj = Object(
            "",
            edDns.text.toString(),
            "",
            "",
            "",
            "",
            0.0,
            false,
            "",
            "",
            0,
            edTrailerName.text.toString(),
            ""
        )

        trailerArr.add(obj)
        val postData = TrailerRequest(trailerArr)
        AppLogger.e(postData.toString())
        showProgressDialog("Trailer", "Please wait..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.addTrailer(postData)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                val json = parseJsonObject(response.body().toString())
                if (json.getBoolean("ResponseResult")) {
                    showSuccessToast("Trailer added successfully")
                    getTrailerList()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.e(t.message.toString())
                hideProgressDialog()
            }
        })
    }

    //using update trailer
    private fun updateTrailer() {
        trailerArr[0].dNS = edDns.text.toString()
        trailerArr[0].trailerName = edTrailerName.text.toString()
        val postData = TrailerRequest(trailerArr)
        AppLogger.e(postData.toString())
        showProgressDialog("Trailer", "update trailer..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.updateTrailer(postData)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                val json = parseJsonObject(response.body().toString())
                if (json.getBoolean("ResponseResult")) {
                    showSuccessToast("Trailer update successfully")
                    getTrailerList()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.e(t.message.toString())
                hideProgressDialog()
            }
        })
    }
}
