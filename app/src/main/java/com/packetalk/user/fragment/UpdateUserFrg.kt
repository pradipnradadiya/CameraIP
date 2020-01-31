package com.packetalk.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.user.model.CommonItem
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_update_user.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserFrg : BaseFragment() {
    lateinit var rootView: View


    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_update_user, parent, false)
        return rootView
    }

    override fun init() {

    }

    override fun initView() {
    }

    override fun postInitView() {
    }

    override fun addListener() {
        rootView.btnUpdateUser.setOnClickListener {
            updateUser()
        }
    }

    override fun loadData() {
    }

    private fun updateUser() {
        showProgressDialog("Update", "Please wait user is updated..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.updateUsers()
        callApi!!.enqueue(object : Callback<CommonItem> {
            override fun onResponse(call: Call<CommonItem>, response: Response<CommonItem>) {
                AppLogger.response(response.body().toString())
                hideProgressDialog()
            }

            override fun onFailure(call: Call<CommonItem>, t: Throwable) {
                hideProgressDialog()
                AppLogger.error(t.message.toString())
            }
        })

    }


    /* private fun updateUser() {
         showProgressDialog("Update", "Users updated..")
         val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
         val callApi = apiInterface?.updateUsers()

         callApi!!.enqueue(object : Callback<JsonObject> {
             override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                 AppLogger.response(response.body().toString())
                 hideProgressDialog()
                *//* if (response.isSuccessful) {
                    val str = JSONObject(response.body().toString())
                    if (str.getBoolean("ResponseResult")) {
                        activity?.showSuccessToast("Users updated")
                    } else {
                        activity?.showErrorToast("Users not updated")
                    }
                }*//*
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.error(t.message.toString())
                hideProgressDialog()
            }
        })
    }*/
}
